package com.finanalyzer.processors;

import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.builder.ProfitAndLossBuilder;
import com.finanalyzer.domain.builder.StockBuilder;
import com.finanalyzer.domain.jdo.ProfitAndLossDbObject;
import com.finanalyzer.domain.jdo.StockExceptionDbObject;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.utility.Iterate;

public class PnLProcessor implements Processor<Pair< List<Stock>, List<StockExceptionDbObject> >>
{
	public InputStream statusInputStream;

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
	
	//only for junits
	public PnLProcessor(){}
	
	public PnLProcessor(InputStream statusInputStream)
	{
		this.statusInputStream = statusInputStream;
	}
	
	@Override
	public Pair<List<Stock>, List<StockExceptionDbObject>> execute()
	{
		throw new RuntimeException("execute method not implemented");
	}
	
	protected FastList<Stock> handleBonusScneario(List<Stock> stockLines) 
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
						float sumOfQuantityTillIndex =0;
						for(int j=0; j<i;j++)
						{
							if (newList.get(j).getBuyPrice() != 0.0) {
								sumOfQuantityTillIndex = sumOfQuantityTillIndex+ newList.get(j).getQuantity();
							}
						}
						
						float ratio=(float) stockAtIndex.getQuantity()/sumOfQuantityTillIndex;
						
						//increment by the ratio of bonus till i
						for(int j=0; j<i;j++)
						{
							if(newList.get(j).getBuyPrice()!=0.0)
							{
								
								final float oldQuantity = newList.get(j).getQuantity();
								final float newQuantity = oldQuantity+oldQuantity*ratio;
								newList.get(j).setQuantity(newQuantity);
								
								newList.get(j).setBuyPrice(newList.get(j).getBuyPrice()/(newQuantity/oldQuantity));
							}
						}
					}
					i++;
				}
				 
				stockLinesWithBonusAdjusted.addAll(newList);
			}
		});
		
		FastList<Stock> result = FastList.newList();
		
		final FastList<Stock> bonusStocksRemoved = stockLinesWithBonusAdjusted.reject(IS_ZERO_BUY_PRICE);
		
		result.addAll(bonusStocksRemoved);
		result.addAll(stockLinesWithOutBonus);
		return result;  
	}
	
	public List<Stock> fetchStockSummaryStatus(List<Stock> stockLines)
	{

		final List<Stock> stockSummary = aggregateStocks(stockLines);
			
		Collections.sort(stockSummary, STOCK_NAME_COMPARATOR);
		return stockSummary;
		
	}

	public void calculateImpactOfEachStock(List<Stock> stockSummary, ProfitAndLossDbObject profitAndLoss) 
	{
		for (int i=0; i<stockSummary.size();i++)
		{
			float totalInvestment = 0.0f;
			float weighedAverageReturn = 0.0f;
			for (int j=0; j<stockSummary.size();j++)
			{
				if (i!=j)
				{
					totalInvestment = totalInvestment + stockSummary.get(j).getTotalInvestment();
					weighedAverageReturn = weighedAverageReturn + stockSummary.get(j).getReturnTillDate() * 
							stockSummary.get(j).getTotalInvestment() * .01f;
				}
			}
			stockSummary.get(i).setImpactOnAverageReturn(profitAndLoss.getAverageReturn()-weighedAverageReturn/totalInvestment);
		}
	}

	private List<Stock> aggregateStocks(final List<Stock> stockLines) {
		Stock stockTemp;
		
		UnifiedMap<String, Stock> map = UnifiedMap.newMap();
		
		for (Stock stock : stockLines)
		{
			Stock existingStock = map.get(stock.getStockName());
			
			if (existingStock != null)
			{
				float existingInterestReturn =existingStock.getReturnTillDate();
				float interestReturn = stock.getReturnTillDate();
				float weightedAvgInterestReturn = (existingInterestReturn * existingStock.getTotalInvestment() + 
						interestReturn * stock.getTotalInvestment())
						/ (existingStock.getTotalInvestment() + stock.getTotalInvestment());

				stockTemp = new StockBuilder().
						name(stock.getStockName()).
						totalInvestment(existingStock.getTotalInvestment() + stock.getTotalInvestment()).
						totalReturn(existingStock.getTotalReturn() + stock.getTotalReturn()).
						totalReturnIfBank(existingStock.getTotalReturnIfBank() + stock.getTotalReturnIfBank()).
						returnTillDate(weightedAvgInterestReturn).
						quantity(existingStock.getQuantity() + stock.getQuantity()).
						sellableQuantity(existingStock.getSellableQuantity() + stock.getSellableQuantity()).
						sellDate(stock.getSellDate()).buyDate(stock.getBuyDate()).blackListed(stock.isBlackListed()).
						sellPrice(existingStock.getSellPrice()).
						diff(existingStock.getDiff()+stock.getDiff()).
						build();

				map.put(stock.getStockName(), stockTemp);
			} 
			else
			{
				map.put(stock.getStockName(), stock);
			}
			
		}
		List<Stock> aggregatedStock = FastList.newList();
		aggregatedStock.addAll(map.values());
		
		return aggregatedStock;
	}

	//tactical replace with better utilities in gs collection
	private List<List<Stock>> getStocksWithAndWithoutBonus(final List<Stock> stocks) {
		List<List<Stock>> stocksWithAndWithoutBonus = FastList.newList();
		final List<Stock> stocksWithBonus = FastList.newList(); 
		List<Stock> stocksWithoutBonus = FastList.newList();
		
		final List<String> stockNamesWithBonus = getStockNamesWithBonus(stocks);
		
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

	public ProfitAndLossDbObject getProfitAndLoss(List<Stock> stocksSummary) {
		float totalReturnVsIfBank = 0.0f;
		float totalReturnIfBank = 0.0f;
		float totalReturn = 0.0f;
		float totalInvestment = 0.0f;
		float weighedAverageReturn = 0.0f;
		
		for(Stock eachStockSummary : stocksSummary)
		{
			totalReturnVsIfBank = totalReturnVsIfBank+eachStockSummary.getTotalReturn()-eachStockSummary.getTotalReturnIfBank();
			totalReturnIfBank = totalReturnIfBank+eachStockSummary.getTotalReturnIfBank();
			totalReturn = totalReturn + eachStockSummary.getTotalReturn();
			totalInvestment = totalInvestment + eachStockSummary.getTotalInvestment();
			weighedAverageReturn = weighedAverageReturn + eachStockSummary.getReturnTillDate() * eachStockSummary.getTotalInvestment() * .01f;
		}
		
		return new ProfitAndLossBuilder()
		.averageReturn(weighedAverageReturn/totalInvestment)
		.totalInvestment(totalInvestment)
		.totalReturn(totalReturn)
		.totalReturnIfBank(totalReturnIfBank)
		.totalReturnVsIfBank(totalReturnVsIfBank)
		.build();
		
	}
	
	private List<String> getStockNamesWithBonus(final List<Stock> stocks) {
		List<String> stockNamesWithBonus = FastList.newList();
		Iterate.collectIf(stocks, IS_ZERO_BUY_PRICE, Stock.STOCKNAME_SELECTOR, stockNamesWithBonus);
		return stockNamesWithBonus;
	}
	

}
