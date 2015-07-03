package com.finanalyzer.processors;

import java.io.InputStream;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.StockBuilder;
import com.finanalyzer.helloworld.Employee;
import com.finanalyzer.util.CalculatorUtil;
import com.finanalyzer.util.DateUtil;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.function.IfFunction;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.set.mutable.SetAdapter;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.utility.Iterate;

public class PnLProcessor implements Processor<FastList<Stock>>
{
	public final InputStream statusInputStream;

	private static final  Predicate<Stock> IS_ZERO_BUY_PRICE = new Predicate<Stock>() {

		@Override
		public boolean accept(Stock stock) {
			
			return stock.getBuyPrice()==0.0;
		}
	};
	
	private static final Comparator<Stock> STOCK_NAME_COMPARATOR = new Comparator<Stock>()
	{
		@Override
		public int compare(Stock stock1, Stock stock2)
		{
			return stock1.getStockName().compareTo(stock2.getStockName());
		}
	};
	
	public PnLProcessor(InputStream statusInputStream)
	{
		this.statusInputStream = statusInputStream;
	}
	
	@Override
	public FastList<Stock> execute()
	{
		throw new RuntimeException("execute method not implemented");
	}
	
	protected FastList<Stock> handleBonusScneario(FastList<Stock> stockLines) 
	{
		final List<Stock> stockLinesWithOutBonus = getStocksWithAndWithoutBonus(stockLines).get(0);
		final List<Stock> stockLinesWithBonus = getStocksWithAndWithoutBonus(stockLines).get(1);
		final Multimap<String,Stock> stockLinesWithBonusGroupedByStockName = Iterate.groupBy(stockLinesWithBonus, Stock.STOCKNAME_SELECTOR);
		
		final FastList<Stock> stockLinesWithBonusAdjusted = FastList.newList();
		
		stockLinesWithBonusGroupedByStockName.forEachKey(new Procedure<String>() {
			@Override
			public void value(String stockName) {
				final FastList<Stock> newList = FastList.newList(stockLinesWithBonusGroupedByStockName.get(stockName));
				
				newList.sortThisBy(Stock.BUY_DATE_SELECTOR);
				int i=0;
				for (Stock stockAtIndex : newList)
				{
					if(stockAtIndex.getBuyPrice()==0.0f)
					{
						int sumOfQuantityTillIndex =0;
						for(int j=0; j<i;j++)
						{
							if (newList.get(j).getBuyPrice() != 0.0) {
								sumOfQuantityTillIndex = sumOfQuantityTillIndex+ newList.get(j).getQuantity();
							}
						}
						float ratio=stockAtIndex.getQuantity()/sumOfQuantityTillIndex;
						
						//increment by the ratio of bonus till i
						for(int j=0; j<i;j++)
						{
							if(newList.get(j).getBuyPrice()!=0.0)
							{
								
								final int oldQuantity = newList.get(j).getQuantity();
								final int newQuantity = (int)(oldQuantity+oldQuantity*ratio);
								newList.get(j).setQuantity(newQuantity);
								
								newList.get(j).setBuyPrice(newList.get(j).getBuyPrice()/(newQuantity/oldQuantity));
							}
						}
						//remove ith stock from the list
//						newList.remove(stockAtIndex);
					}
					i++;
				}
				 
				stockLinesWithBonusAdjusted.addAll(newList);
			}
		});
		
		FastList<Stock> result = FastList.newList();
		
		final FastList<Stock> bonusStocksRemoved = stockLinesWithBonusAdjusted.select(new Predicate<Stock>() {

			@Override
			public boolean accept(Stock stock) {
				return stock.getBuyPrice()!=0.0;
			}
		});
		
		result.addAll(bonusStocksRemoved);
		result.addAll(stockLinesWithOutBonus);
		return result;  
	}
	
	public FastList<Stock> fetchStockSummaryStatus(FastList<Stock> stockLines)
	{
		UnifiedMap<String, Stock> map = UnifiedMap.newMap();

		aggregateStocks(map, stockLines);
		
		FastList<Stock> stockSummary =FastList.newList(map.values());

		return stockSummary.sortThis(STOCK_NAME_COMPARATOR);
	}

	private void aggregateStocks(UnifiedMap<String, Stock> map, final List<Stock> stockLines) {
		Stock stockTemp;
		for (Stock stock : stockLines)
		{
			Stock existingStock = map.get(stock.getStockName());
			if (existingStock != null)
			{
				float existingInterestReturn =existingStock.getReturnTillDate();
				float interestReturn = stock.getReturnTillDate();
				float avgInterestReturn = (existingInterestReturn * existingStock.getQuantity() + interestReturn * stock.getQuantity())
						/ (existingStock.getQuantity() + stock.getQuantity());

				stockTemp = new StockBuilder().
						name(stock.getStockName()).
						totalInvestment(existingStock.getTotalInvestment() + stock.getTotalInvestment()).
						totalReturn(existingStock.getTotalReturn() + stock.getTotalReturn()).
						totalReturnIfBank(existingStock.getTotalReturnIfBank() + stock.getTotalReturnIfBank()).
						returnTillDate(avgInterestReturn).
						quantity(existingStock.getQuantity() + stock.getQuantity()).
						sellableQuantity(existingStock.getSellableQuantity() + stock.getSellableQuantity()).
						sellDate(stock.getSellDate()).buyDate(stock.getBuyDate()).
						build();

				map.put(stock.getStockName(), stockTemp);
			} else
			{
				map.put(stock.getStockName(), stock);
			}
		}
	}

	//tactical replace with better utilties in gs collection
	private List<List<Stock>> getStocksWithAndWithoutBonus(final FastList<Stock> stocks) {
		List<List<Stock>> stocksWithAndWithoutBonus = FastList.newList();
		final List<Stock> stocksWithBonus = FastList.newList(); 
		List<Stock> stocksWithoutBonus = FastList.newList();
		
		final UnifiedSet<String> stockNamesWithBonus = getStockNamesWithBonus(stocks);
		
		for (Stock stock : stocks)
		{
			if(stockNamesWithBonus.contains(stock.getStockName()))
			{
				stocksWithBonus.add(stock);	
			}
			else
			{
				stocksWithoutBonus.add(stock);	
			}
		}
		
		stocksWithAndWithoutBonus.add(stocksWithoutBonus); //order important as there is a index based splitting later on
		stocksWithAndWithoutBonus.add(stocksWithBonus);

		return stocksWithAndWithoutBonus;
	}

	private UnifiedSet<String> getStockNamesWithBonus(final FastList<Stock> stocks) {
		UnifiedSet<String> stockNamesWithBonus = UnifiedSet.newSet();
		stocks.collectIf(IS_ZERO_BUY_PRICE, Stock.STOCKNAME_SELECTOR, stockNamesWithBonus)	;
		return stockNamesWithBonus;
	}
	

}
