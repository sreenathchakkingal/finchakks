package com.finanalyzer.processors;

import java.io.InputStream;
import java.util.Comparator;
import java.util.List;

import com.finanalyzer.api.StockQuandlApiAdapter;
import com.finanalyzer.db.WatchListUtil;
import com.finanalyzer.domain.Stock;
import com.finanalyzer.util.DateUtil;
import com.finanalyzer.util.ReaderUtil;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.impl.list.mutable.FastList;

public class NMonthsLowProcessor
implements Processor<List<Stock>>
{
	private final int numberOfDays;
	private final List<String> stocks;
	private static final Predicate<Stock> LATESTCLOSEPRICE_lESSTHAN_SIMPLEMOVINGAVERAGE= new Predicate<Stock>() {
		@Override
		public boolean accept(Stock stock) {
			double tolerance = stock.getSellPrice()*.05;
			return stock.getSellPrice()<=stock.getSimpleMovingAverage()+tolerance;
		}
	};

	private static final Comparator<Stock> SIMPLE_MOVING_AVG_COMPARATOR = new Comparator<Stock>() {
		@Override
		public int compare(Stock stock1, Stock stock2) {
			return (int)(stock2.getSimpleMovingAverageAndSellDeltaNormalized()-stock1.getSimpleMovingAverageAndSellDeltaNormalized());
		}
	};

	public NMonthsLowProcessor(String numberOfMonthsAsString, InputStream stocksAsInputStream)
	{
		this.numberOfDays = Integer.valueOf(numberOfMonthsAsString).intValue()* DateUtil.NUMBER_OF_DAYS_IN_MONTH;
		this.stocks = ReaderUtil.convertToList(stocksAsInputStream);
	}

	@Override
	public List<Stock> execute()
	{
		WatchListUtil watchListUtil = new WatchListUtil();
		FastList<Stock> stocks = watchListUtil.retrieveWatchListAsStocks();
		StockQuandlApiAdapter.stampSimpleMovingAverage(stocks, this.numberOfDays);
		StockQuandlApiAdapter.stampLatestClosePriceAndDate(stocks);
		FastList<Stock> filteredStocks = stocks.select(LATESTCLOSEPRICE_lESSTHAN_SIMPLEMOVINGAVERAGE);
		filteredStocks.sortThis(SIMPLE_MOVING_AVG_COMPARATOR);
		return filteredStocks;
	}

	//  public List<Stock> execute()
	//  {
		//	  FastList<Stock> stocksWhichAreNRunningNMonthLowPrices = FastList.newList();
	//    for (String stockName : this.stocks)
	//    {
	//      List<String> historicPrices = UrlUtil.getHistoricPricesFromYahoo(this.numberOfMonths * 30, stockName);
	//      StockBuilder stockBuilder = new StockBuilder().name(stockName);
	//      if (!historicPrices.isEmpty())
	//      {
	//        String firstRow = (String)historicPrices.get(1);
	//        String lastRow = (String)historicPrices.get(historicPrices.size() - 1);
	//        
	//        double latestClosePrice = ReaderUtil.parseForClosePrice(firstRow).doubleValue();
	//        double lastClosePrice = ReaderUtil.parseForClosePrice(lastRow).doubleValue();
	//        
	//        String latestDate = ReaderUtil.parseForDate(firstRow);
	//        String lastDate = ReaderUtil.parseForDate(lastRow);
	//        if ((latestClosePrice >= 0.2d) && (latestClosePrice <= lastClosePrice))
	//        {
	//          Stock stock = stockBuilder.buyPrice(lastClosePrice).sellPrice(latestClosePrice)
	//            .buyDate(lastDate).sellDate(latestDate).build();
	//          stocksWhichAreNRunningNMonthLowPrices.add(stock);
	//        }
	//      }
	//      else
	//      {
	//        Stock stock = stockBuilder.setIsException(true).build();
	//        stocksWhichAreNRunningNMonthLowPrices.add(stock);
	//      }
	//    }
	//    return stocksWhichAreNRunningNMonthLowPrices.sortThis(new Comparator<Stock>()
	//	{
	//		@Override
	//		public int compare(Stock stock1, Stock stock2)
	//		{
	//			return (int) (((stock2.getBuyPrice()-stock2.getSellPrice())/stock2.getBuyPrice()*100)-
	//					(stock1.getBuyPrice()-stock1.getSellPrice())/stock1.getBuyPrice()*100 );
	//		}
	//	});
	//  }

}
