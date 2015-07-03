package com.finanalyzer.processors;

import java.io.InputStream;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.finanalyzer.api.StockQuandlApiAdapter;
import com.finanalyzer.db.AllScripsUtil;
import com.finanalyzer.db.StockRatingsDb;
import com.finanalyzer.db.WatchListUtil;
import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.domain.NDaysPrice;
import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.StockExchange;
import com.finanalyzer.domain.StockRatingValue;
import com.finanalyzer.domain.jdo.AllScripsDbObject;
import com.finanalyzer.util.CalculatorUtil;
import com.finanalyzer.util.ReaderUtil;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;

public class QuandlNDaysPricesProcessor implements Processor<List<Stock>>
{
	private static final Logger LOG = Logger.getLogger(QuandlNDaysPricesProcessor.class.getName());
	private static final NumberFormat PERCENTAGE_FORMAT = null;
	protected InputStream stocksInputStream;
	protected final int numOfDays;
	private int simpleMovingAverage;
	protected String source;
	private static final Comparator<Stock> SIMPLE_AVG_NET_GAINS_COMPARATOR = new Comparator<Stock>()
	{
		@Override
		public int compare(Stock stock1, Stock stock2)
		{
			if(stock1.isException() || stock2.isException())
			{
				return Integer.MAX_VALUE; 
			}
			int simpleMovingAvgDiff = (int) (stock1.getSimpleMovingAverageAndSellDeltaNormalized() * 1000 - stock2
					.getSimpleMovingAverageAndSellDeltaNormalized() * 1000);
			if (simpleMovingAvgDiff == 0)
			{
				int netGainDiff = (int) (stock1.getNetNDaysGain() * 1000 - stock2.getNetNDaysGain() * 1000);
				if (netGainDiff == 0)
				{
					return stock1.getStockName().compareTo(stock2.getStockName());
				}
				return netGainDiff;
			} else
			{
				return simpleMovingAvgDiff;
			}

		}
	};

	public QuandlNDaysPricesProcessor(InputStream stocksInputStream, String numOfDays, String source)
	{
		this.stocksInputStream = stocksInputStream;
		this.numOfDays = Integer.valueOf(numOfDays);
		this.source = source;
	}

	public QuandlNDaysPricesProcessor(String numOfDays, String simpleMovingAverage)
	{
		this.numOfDays = Integer.valueOf(numOfDays);
		this.simpleMovingAverage = Integer.valueOf(simpleMovingAverage);
	}

	@Override
	public List<Stock> execute()
	{
		JdoDbOperations<AllScripsDbObject> dbOperations = new JdoDbOperations<AllScripsDbObject>(AllScripsDbObject.class);
		final List<AllScripsDbObject> watchListedScrips = dbOperations.getEntries("isWatchListed", FastList.newListWith(String.valueOf(true)));
		FastList<Stock> stocks = FastList.newList();
		
		for (AllScripsDbObject allScripsDbObject : watchListedScrips)
		{
			final Stock stock = new Stock(allScripsDbObject.getNseId(), StockExchange.NSE);
			stock.addNames( StockExchange.BSE, allScripsDbObject.getBseId());
			stock.setNumOfDays(this.numOfDays);
			stock.setStockRatingValue(new StockRatingValue(allScripsDbObject.getRatingNameToValue()));
			stocks.add(stock);
		}
		
//		FastList<Stock> stocks = FastList.newListWith(
//				new Stock("500331", StockExchange.BSE), new Stock("500790", StockExchange.BSE), new Stock("517354", StockExchange.BSE));
		 
		StockQuandlApiAdapter.stampNDaysClosePrices(stocks, this.simpleMovingAverage);
//		StockRatingsDb stockRatingsDb = new StockRatingsDb();
//		stockRatingsDb.stampStockRatingValue(stocks);
		Collections.sort(stocks, SIMPLE_AVG_NET_GAINS_COMPARATOR);
		return stocks;
	}
	
//	@Override
//	public List<Stock> execute()
//	{
//		WatchListUtil watchListUtil = new WatchListUtil();
//		FastList<Stock> stocks = watchListUtil.retrieveWatchListAsStocks(StockExchange.BSE);
////		FastList<Stock> stocks = FastList.newListWith(
////				new Stock("500331", StockExchange.BSE), new Stock("500790", StockExchange.BSE), new Stock("517354", StockExchange.BSE));
//		 
//		for (Stock stock : stocks)
//		{
//			stock.setNumOfDays(this.numOfDays);
//		}
//		
//		StockQuandlApiAdapter.stampNDaysClosePrices(stocks, this.simpleMovingAverage);
//		AllScripsUtil allScripsUtil = AllScripsUtil.getInstance();
//		allScripsUtil.convertBseIdToNse(stocks);
//		StockRatingsDb stockRatingsDb = new StockRatingsDb();
//		stockRatingsDb.stampStockRatingValue(stocks);
//		Collections.sort(stocks, SIMPLE_AVG_NET_GAINS_COMPARATOR);
//		return stocks;
//	}
}
