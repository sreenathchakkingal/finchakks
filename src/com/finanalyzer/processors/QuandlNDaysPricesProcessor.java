package com.finanalyzer.processors;

import java.io.InputStream;
import java.text.NumberFormat;
import java.util.Collection;
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
import com.finanalyzer.domain.jdo.UnrealizedDbObject;
import com.finanalyzer.util.CalculatorUtil;
import com.finanalyzer.util.ReaderUtil;
import com.finanalyzer.util.StringUtil;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.utility.Iterate;

public class QuandlNDaysPricesProcessor implements Processor<List<Stock>>
{
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
			int netGainDiff = (int) (stock1.getNetNDaysGain() * 1000 - stock2.getNetNDaysGain() * 1000);
			int totalDiff = simpleMovingAvgDiff+netGainDiff;
			if (totalDiff == 0)
			{
				return stock1.getStockName().compareTo(stock2.getStockName());
			}
			return totalDiff;
		}
	};
	
	private static final Function<UnrealizedDbObject, String> GROUP_BY_STOCKNAME = new Function<UnrealizedDbObject, String>() {

		@Override
		public String valueOf(UnrealizedDbObject unrealizedDbObject) {
			return unrealizedDbObject.getMoneycontrolName();
		}
	};
	
	private static final Function<UnrealizedDbObject, String> GROUP_BY_INDUSTRY = new Function<UnrealizedDbObject, String>() {

		@Override
		public String valueOf(UnrealizedDbObject unrealizedDbObject) {
			return unrealizedDbObject.getIndustry();
		}
	};

	private static final Function0<Float> INITAL_VALUE = new Function0<Float>() {
		
		@Override
		public Float value() {
			return Float.valueOf(0.0f);
		}
	};
	
	private static final Function2<Float, UnrealizedDbObject, Float> BUY_PRICE_AGGREGATOR = new Function2<Float, UnrealizedDbObject, Float>() {

		@Override
		public Float value(Float intialAmount, UnrealizedDbObject stock) {
			return intialAmount+(float)stock.getBuyPrice()*stock.getBuyQuantity();
		}

	};
	
	private static final FloatFunction<UnrealizedDbObject> TOTAL_INVESMENT_AGGREGATOR = new FloatFunction<UnrealizedDbObject>() {
		@Override
		public float floatValueOf(UnrealizedDbObject unrealizedDbObject) {
			return (float)unrealizedDbObject.getBuyPrice()*unrealizedDbObject.getBuyQuantity();
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
		JdoDbOperations<UnrealizedDbObject> unrealizedDbOperations = new JdoDbOperations<UnrealizedDbObject>(UnrealizedDbObject.class);
		final FastList<AllScripsDbObject> allScrips = FastList.newList(dbOperations.getEntries());
		final List<AllScripsDbObject> watchListedScrips = allScrips.select(AllScripsDbObject.IS_WATCHLISTED);
		final List<AllScripsDbObject> allScripsWithValidMoneyControlName = allScrips.select(AllScripsDbObject.MONEYCONTROL_NAME_EXISTS);
		
		FastList<Stock> stocks = FastList.newList();
		
		final FastList<UnrealizedDbObject> unrealizedDbObjects = FastList.newList(unrealizedDbOperations.getEntries());
		final MutableMap<String, Float> investmentAggregatedByStockname = unrealizedDbObjects.aggregateBy(GROUP_BY_STOCKNAME, INITAL_VALUE, BUY_PRICE_AGGREGATOR);
		
		
		for (UnrealizedDbObject unrealizedDbObject: unrealizedDbObjects)
		{
			String moneycontrolName = unrealizedDbObject.getMoneycontrolName();
			for (AllScripsDbObject allScripsDbObject : allScripsWithValidMoneyControlName)
			{
				if(moneycontrolName.equals(allScripsDbObject.getMoneycontrolName()))
				{
					unrealizedDbObject.setIndustry(allScripsDbObject.getIndustry());
					break;
				}

			}
		}
		
		final MutableMap<String, Float> investmentAggregatedByIndustry= unrealizedDbObjects.aggregateBy(GROUP_BY_INDUSTRY, INITAL_VALUE, BUY_PRICE_AGGREGATOR);
		
		final float totalInvestment = (float)unrealizedDbObjects.sumOfFloat(TOTAL_INVESMENT_AGGREGATOR);

		for (AllScripsDbObject allScripsDbObject : watchListedScrips)
		{
			final Stock stock = new Stock(allScripsDbObject.getNseId(), StockExchange.NSE);
			stock.addNames( StockExchange.BSE, allScripsDbObject.getBseId());
			stock.setIndustry(allScripsDbObject.getIndustry());
			stock.setNumOfDays(this.numOfDays);
			stock.setStockRatingValue(new StockRatingValue(allScripsDbObject.getRatingNameToValue()));
			
			final String moneycontrolName = allScripsDbObject.getMoneycontrolName();
			
			if(investmentAggregatedByStockname.get(moneycontrolName)!=null)
			{
				final Float investmentRatio = investmentAggregatedByStockname.get(moneycontrolName)/totalInvestment;
				stock.setInvestmentRatio(investmentRatio);
			}
			
			String industry = allScripsDbObject.getIndustry();

			if(StringUtil.isValidValue(industry))
			{
				final Float investmentInIndustry = investmentAggregatedByIndustry.get(industry);
				if(investmentInIndustry !=null)
				{
					final Float industryInvestmentRatio=investmentInIndustry/totalInvestment;
					stock.setIndustryInvestmentRatio(industryInvestmentRatio);					
				}
			}
			
			stocks.add(stock);
		}
		
//		FastList<Stock> stocks = FastList.newListWith(
//				new Stock("500331", StockExchange.BSE), new Stock("500790", StockExchange.BSE), new Stock("517354", StockExchange.BSE));
		 
		StockQuandlApiAdapter.stampNDaysClosePrices(stocks, this.simpleMovingAverage);
		
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
