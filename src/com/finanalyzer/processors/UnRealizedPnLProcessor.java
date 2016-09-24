package com.finanalyzer.processors;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.fileupload.FileItemIterator;

import com.finanalyzer.api.StockQuandlApiAdapter;
import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.builder.StockBuilder;
import com.finanalyzer.domain.jdo.AllScripsDbObject;
import com.finanalyzer.domain.jdo.ProfitAndLossDbObject;
import com.finanalyzer.domain.jdo.StockExceptionDbObject;
import com.finanalyzer.domain.jdo.StopLossDbObject;
import com.finanalyzer.domain.jdo.UnrealizedDbObject;
import com.finanalyzer.domain.jdo.UnrealizedDetailDbObject;
import com.finanalyzer.domain.jdo.UnrealizedSummaryDbObject;
import com.finanalyzer.util.Adapter;
import com.finanalyzer.util.CalculatorUtil;
import com.finanalyzer.util.DateUtil;
import com.finanalyzer.util.ReaderUtil;
import com.finanalyzer.util.StringUtil;
import com.google.gson.JsonObject;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.Iterate;

public class UnRealizedPnLProcessor extends PnLProcessor 
{
	private static final Logger LOG = Logger.getLogger(UnRealizedPnLProcessor.class.getName());
	
	private String stockName;
	private FileItemIterator fileItemIterator;

	private String unrealizedDetailsContent;

	private boolean isUseUnrealizedDetailsContent;
	public static final Predicate<Stock> MATURITY_MORE_THAN_11_MONTHS = new Predicate<Stock>()
	{
		private static final long serialVersionUID = 1L;

		@Override
		public boolean accept(Stock stock)
		{
			return stock.getDifferenceBetweeBuyDateAndSellDate() > 11 * DateUtil.NUMBER_OF_DAYS_IN_MONTH; 
		}
	};

	private static final Comparator<Stock> NAME_DATE_PRICE_COMPARATOR = new Comparator<Stock>() 
	{
		@Override
		public int compare(Stock o1, Stock o2) 
		{
			if(o1.getStockName().equals(o2.getStockName()))
			{
				if(o1.getBuyDate().equals(o2.getBuyDate()))
				{
					return (int)(o1.getBuyPrice()-o2.getBuyPrice());	
				}
				return o1.getBuyDate().compareTo(o2.getBuyDate());
			}
			else
			{
				return o1.getStockName().compareTo(o2.getStockName());	
			}
		}
	};

	private final JdoDbOperations<StopLossDbObject> stopLossDbOperations = new JdoDbOperations<StopLossDbObject>(StopLossDbObject.class);


	public UnRealizedPnLProcessor(FileItemIterator fileItemIterator)
	{
		super(null);//hack remove refactor later;
		this.fileItemIterator=fileItemIterator;
	}
	
	public UnRealizedPnLProcessor(String unrealizedDetailsContent)
	{
		super(null);//hack remove refactor later;
		this.unrealizedDetailsContent=unrealizedDetailsContent;
		this.isUseUnrealizedDetailsContent=true; //if the copy paste works fine remove the other constructor fileItemIterator and this flag
	}
	
	//only for junits
	public UnRealizedPnLProcessor(){}

	@Override
	public Pair<List<Stock>,List<StockExceptionDbObject>> execute() {
		List<Stock> stocks = FastList.newList();
		Stock stock;

		JdoDbOperations<UnrealizedDbObject> unrealizeddbOperations = new JdoDbOperations<UnrealizedDbObject>(UnrealizedDbObject.class);

		List<String> rowsWithoutHeaderAndTrailer = ReaderUtil.convertToList(this.fileItemIterator, true, 2);
		List<String> rowsWithoutHeaderAndTrailerFromContent = ReaderUtil.convertToList(this.unrealizedDetailsContent, true, 1);
		List<UnrealizedDbObject> entries = null;
		if(isUseUnrealizedDetailsContent && rowsWithoutHeaderAndTrailerFromContent.size()>1)
		{
			unrealizeddbOperations.deleteEntries();
			entries = unrealizeddbOperations.insertUnrealizedDataFromMoneycontrol(rowsWithoutHeaderAndTrailerFromContent, true);
		}
		else if (isSanityChecksPassed(rowsWithoutHeaderAndTrailer)) 
		{
			unrealizeddbOperations.deleteEntries();
			entries = unrealizeddbOperations.insertUnrealizedDataFromMoneycontrol(rowsWithoutHeaderAndTrailer);
		} 
		else
		{
			entries = unrealizeddbOperations.getEntries(AllScripsDbObject.MONEY_CONTROL_NAME);
		}
		
		JdoDbOperations<AllScripsDbObject> allScripsDbOperations = new JdoDbOperations<AllScripsDbObject>(AllScripsDbObject.class);//move this as instance variable ?
		List<StockExceptionDbObject> exceptionStocks = FastList.newList();
		for (UnrealizedDbObject dbObject : entries) {
			String moneycontrolName = dbObject.getMoneycontrolName();
			if(StringUtil.isValidValue(moneycontrolName))
			{
				final List<AllScripsDbObject> scrips = allScripsDbOperations.getEntries(AllScripsDbObject.MONEY_CONTROL_NAME,FastList.newListWith(moneycontrolName));
				
				if (!scrips.isEmpty()) 
				{
					final AllScripsDbObject aScrip = scrips.get(0);

					String buyDate = dbObject.getBuyDate();
					double buyPriceDouble = dbObject.getBuyPrice();
					long quantity = dbObject.getBuyQuantity();

					stock = new StockBuilder().name(aScrip.getNseId())
							.quantity((int) quantity).buyPrice((float) buyPriceDouble)
							.buyDate(buyDate).sellDate(DateUtil.todaysDate()).blackListed(aScrip.isBlackListed()).
							build();
					stocks.add(stock);
				} 
				else 
				{
					LOG.info("No mapping found: "+moneycontrolName);
					exceptionStocks.add(new StockExceptionDbObject(moneycontrolName, "No mapping found"));
				}
			}
		}

		Iterate.sortThis(stocks, NAME_DATE_PRICE_COMPARATOR);
		
		final List<Stock> bonusScnearioHandledStocks = handleBonusScneario(stocks);
		
		StockQuandlApiAdapter.stampLatestClosePriceAndDate(bonusScnearioHandledStocks);
		
		final Pair<List<Stock>,List<StockExceptionDbObject>> stocksAndExceptions = Tuples.pair(bonusScnearioHandledStocks, exceptionStocks);
		
		return stocksAndExceptions;
	}
			
	//hack remove later - 'manual' is passed from the UI.
	private boolean isSanityChecksPassed(List<String> rowsWithoutHeaderAndTrailer) {
		return rowsWithoutHeaderAndTrailer!=null && rowsWithoutHeaderAndTrailer.size()>1 && !"manual".equals(rowsWithoutHeaderAndTrailer.get(0));
	}


			public JsonObject getStockInvestmentChart(Collection<Stock> stocksSummary)
			{
				JsonObject jsonData = new JsonObject();

				float totalReturns = 0.0f;
				float totalInvestment = 0.0f;

				for (Stock stock : stocksSummary)
				{
					totalReturns +=stock.getReturnTillDate();
					totalInvestment += Math.abs(stock.getTotalInvestment());
				}
				for (Stock stock : stocksSummary)
				{
					try
					{
						JsonObject returns = new JsonObject();
						returns.addProperty("invested", Float.valueOf(stock.getTotalInvestment() / totalInvestment * 100.0f));
						returns.addProperty("returns", stock.getReturnTillDate()/totalReturns*100.0d);

						jsonData.add(stock.getStockName(), returns);
					} catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				return jsonData;
			}

			public JsonObject getRequestedStockInvestmentChart(JsonObject stockInvestmentChart)
			{
				if (this.stockName==null || "".equals(this.stockName))
				{
					return stockInvestmentChart;
				}
				JsonObject jsonData = new JsonObject();
				try
				{
					jsonData.add(this.stockName, stockInvestmentChart.get(this.stockName));
				} catch (Throwable e)
				{
					e.printStackTrace();
				}
				return jsonData;
			}

			public List<StockExceptionDbObject> enrichStocksWithTargets(List<Stock> stocksSummary)
			{
				List<StockExceptionDbObject> stocksWithoutTargets = FastList.newList();
				
				for(Stock eachStock : stocksSummary)
				{
					final List<StopLossDbObject> matchingStopLossDbObjects = getMatchingStopLossDbObject(eachStock);
					if(!matchingStopLossDbObjects.isEmpty())
					{
						StopLossDbObject matchingStopLossDbObject = matchingStopLossDbObjects.get(0);
						
						eachStock.setStopLossDbObject(matchingStopLossDbObject);
						
						final float lowerReturnPercentTarget = matchingStopLossDbObject.getLowerReturnPercentTarget();
						final float upperReturnPercentTarget = matchingStopLossDbObject.getUpperReturnPercentTarget();
						final float lowerSellPriceTarget = matchingStopLossDbObject.getLowerSellPriceTarget();
						final float upperSellPriceTarget = matchingStopLossDbObject.getUpperSellPriceTarget();
						final String achieveByDate = matchingStopLossDbObject.getAchieveByDate();
						final String achieveAfterDate = matchingStopLossDbObject.getAchieveAfterDate();
						
						final float stockSellPrice = eachStock.getSellPrice();
						final float stockReturnTillDate = eachStock.getReturnTillDate();
						
						boolean isDateTargetMet=false;
						
						if(StringUtil.isValidValue(achieveByDate))
						{
							isDateTargetMet = DateUtil.isDateBeforeTargetDate(achieveByDate, StopLossDbObject.DATE_DIFF_TOLERANCE);
						}
						
						if(StringUtil.isValidValue(achieveAfterDate))
						{
							isDateTargetMet=isDateTargetMet || DateUtil.isDateAfterTargetDate(achieveAfterDate, StopLossDbObject.DATE_DIFF_TOLERANCE);;
						}

						if (StringUtil.isInvalidValue(achieveByDate) && StringUtil.isInvalidValue(achieveAfterDate))
						{
							isDateTargetMet=true;
						}
						
						boolean isValueCloseToTarget=false;
						
						//replace with !=0.0f in the next checkin else we -ve targets will be missed
						
						if(StopLossDbObject.isValidTarget(lowerReturnPercentTarget))
						{
							isValueCloseToTarget = CalculatorUtil.isValueLessThanTarget(stockReturnTillDate, lowerReturnPercentTarget,StopLossDbObject.RETURN_DIFF_TOLERANCE);
						}
						if(StopLossDbObject.isValidTarget(upperReturnPercentTarget))
						{
							isValueCloseToTarget = isValueCloseToTarget || CalculatorUtil.isValueMoreThanTarget(stockReturnTillDate, upperReturnPercentTarget,StopLossDbObject.RETURN_DIFF_TOLERANCE);
						}					
						if(StopLossDbObject.isValidTarget(lowerSellPriceTarget))
						{
							isValueCloseToTarget = isValueCloseToTarget || CalculatorUtil.isValueLessThanTarget(stockSellPrice, lowerSellPriceTarget,StopLossDbObject.PRICE_DIFF_TOLERANCE);
						}
						if(StopLossDbObject.isValidTarget(upperSellPriceTarget))
						{
							isValueCloseToTarget = isValueCloseToTarget || CalculatorUtil.isValueMoreThanTarget(stockSellPrice, upperSellPriceTarget,StopLossDbObject.PRICE_DIFF_TOLERANCE);
						}
						
						eachStock.setReachedStopLossTarget(isValueCloseToTarget && isDateTargetMet);
					}
					else
					{
						stocksWithoutTargets.add(new StockExceptionDbObject(eachStock.getStockName(), "No Targets Set"));
					}
				}
				return stocksWithoutTargets;
			}

			//for testing	
			protected List<StopLossDbObject> getMatchingStopLossDbObject(Stock eachStock) {
				return stopLossDbOperations.getEntries(AllScripsDbObject.STOCK_NAME,
																		FastList.newListWith(eachStock.getStockName()));
			}
			
			public void persistResults(List<Stock> stocksDetail, List<Stock> stocksSummary, ProfitAndLossDbObject profitAndLoss, List<StockExceptionDbObject> exceptionStocks) 
			{
				final List<UnrealizedDetailDbObject> unrealizedDetailDbObjects = Adapter.stockToUnrealizedDetailDbObject(stocksDetail);
				final JdoDbOperations<UnrealizedDetailDbObject> dbDetailOperations = new JdoDbOperations<UnrealizedDetailDbObject>(UnrealizedDetailDbObject.class);
				dbDetailOperations.deleteEntries();
				dbDetailOperations.insertEntries(unrealizedDetailDbObjects);

				final List<UnrealizedSummaryDbObject> unrealizedSummaryDbObjects = Adapter.stockToUnrealizedSummaryDbObject(stocksSummary);
				final JdoDbOperations<UnrealizedSummaryDbObject> dbSummaryOperations = new JdoDbOperations<UnrealizedSummaryDbObject>(UnrealizedSummaryDbObject.class);
				dbSummaryOperations.deleteEntries();
				dbSummaryOperations.insertEntries(unrealizedSummaryDbObjects);
				
				final JdoDbOperations<ProfitAndLossDbObject> profitAndLossOperations = new JdoDbOperations<ProfitAndLossDbObject>(ProfitAndLossDbObject.class);
				profitAndLossOperations.deleteEntries();
				profitAndLossOperations.insertEntries(FastList.newListWith(profitAndLoss));
				
				final JdoDbOperations<StockExceptionDbObject> exceptionStocksOperations = new JdoDbOperations<StockExceptionDbObject>(StockExceptionDbObject.class);
				exceptionStocksOperations.deleteEntries();
				exceptionStocksOperations.insertEntries(exceptionStocks);
			}

}

