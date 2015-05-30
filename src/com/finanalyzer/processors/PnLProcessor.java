package com.finanalyzer.processors;

import java.io.InputStream;
import java.util.Comparator;

import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.StockBuilder;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;

public class PnLProcessor implements Processor<FastList<Stock>>
{
	public final InputStream statusInputStream;

	public PnLProcessor(InputStream statusInputStream)
	{
		this.statusInputStream = statusInputStream;
	}
	
	@Override
	public FastList<Stock> execute()
	{
		throw new RuntimeException("execute method not implemented");
	}
	

	public FastList<Stock> arrangeStocks(FastList<Stock> stocks)
	{
		return stocks.sortThis(new Comparator<Stock>()
		{
			@Override
			public int compare(Stock stock1, Stock stock2)
			{
				if(stock1.getStockName().equals(stock2.getStockName()))
				{
					if(stock1.getBuyDate().equals(stock2.getBuyDate()))
					{
						return stock1.getQuantity()-stock2.getQuantity();
					}
					return stock1.getBuyDate().compareTo(stock2.getBuyDate());
				}
				else
				{
					return stock1.getStockName().compareTo(stock2.getStockName());
				}
			}
		});
	}
	
	public FastList<Stock> fetchStockSummaryStatus(FastList<Stock> stocks)
	{
		UnifiedMap<String, Stock> map = UnifiedMap.newMap();
		Stock stockTemp = null;
		for (Stock stock : stocks)
		{
			Stock existingStock = map.get(stock.getStockName());
			if (existingStock != null)
			{
//				float existingInterestReturn = ((InterestReturn) existingStock.getInterestReturns().get(0)).getInterestRateForThePeriod();
				float existingInterestReturn =existingStock.getReturnTillDate();
//				float interestReturn = ((InterestReturn) stock.getInterestReturns().get(0)).getInterestRateForThePeriod();
				float interestReturn = stock.getReturnTillDate();
				float avgInterestReturn = (existingInterestReturn * existingStock.getQuantity() + interestReturn * stock.getQuantity())
						/ (existingStock.getQuantity() + stock.getQuantity());

//				InterestReturn avgInterestReturnWrapper = new InterestReturn("", avgInterestReturn);
				
				stockTemp = new StockBuilder().
						name(stock.getStockName()).
						totalInvestment(existingStock.getTotalInvestment() + stock.getTotalInvestment()).
						totalReturn(existingStock.getTotalReturn() + stock.getTotalReturn()).
						totalReturnIfBank(existingStock.getTotalReturnIfBank() + stock.getTotalReturnIfBank()).
//						interestReturns(FastList.newListWith(new InterestReturn[] { avgInterestReturnWrapper })).
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
		FastList<Stock> stockSummary =FastList.newList(map.values());
		
		return stockSummary.sortThis(new Comparator<Stock>()
		{
			@Override
			public int compare(Stock stock1, Stock stock2)
			{
				return stock1.getStockName().compareTo(stock2.getStockName());
			}
		});
	}

}
