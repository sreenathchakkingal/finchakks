package com.finanalyzer.processors;

import java.io.Reader;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.fileupload.FileItemIterator;

import com.finanalyzer.api.StockQuandlApiAdapter;
import com.finanalyzer.db.StockIdConverstionUtil;
import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.domain.NDaysPrice;
import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.StockExchange;
import com.finanalyzer.domain.builder.ProfitAndLossBuilder;
import com.finanalyzer.domain.builder.StockBuilder;
import com.finanalyzer.domain.jdo.AllScripsDbObject;
import com.finanalyzer.domain.jdo.ProfitAndLossDbObject;
import com.finanalyzer.domain.jdo.UnrealizedDbObject;
import com.finanalyzer.domain.jdo.UnrealizedDetailDbObject;
import com.finanalyzer.domain.jdo.UnrealizedSummaryDbObject;
import com.finanalyzer.util.Adapter;
import com.finanalyzer.util.DateUtil;
import com.finanalyzer.util.ReaderUtil;
import com.finanalyzer.util.StringUtil;
import com.google.gson.JsonObject;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.Iterate;

public class UnRealizedPnLProcessor extends PnLProcessor 
{
	private static final Logger LOG = Logger.getLogger(UnRealizedPnLProcessor.class.getName());
	
	private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("##.##");
	private final String stockName;
	private FileItemIterator fileItemIterator;
	public static final Predicate<Stock> MATURITY_MORE_THAN_A_QUARTER = new Predicate<Stock>()
			{
		private static final long serialVersionUID = 1L;

		@Override
		public boolean accept(Stock stock)
		{
			return stock.getDifferenceBetweeBuyDateAndSellDate() > 3 * DateUtil.NUMBER_OF_DAYS_IN_MONTH; 
		}
			};

			private static final Comparator<Stock> NAME_DATE_PRICE_COMPARATOR = new Comparator<Stock>() {
				@Override
				public int compare(Stock o1, Stock o2) {
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


			public UnRealizedPnLProcessor(FileItemIterator fileItemIterator, String stockName)
			{
				super(null);//hack remove refactor later;
				this.fileItemIterator=fileItemIterator;
				this.stockName = stockName==null ? null : stockName.toUpperCase();
			}


	@Override
	public Pair<List<Stock>, List<Stock>> execute() {
		List<Stock> stocks = FastList.newList();
		Stock stock;

		JdoDbOperations<UnrealizedDbObject> unrealizeddbOperations = new JdoDbOperations<UnrealizedDbObject>(UnrealizedDbObject.class);

		List<String> rowsWithoutHeaderAndTrailer = ReaderUtil.convertToList(this.fileItemIterator, true, true);
		List<UnrealizedDbObject> entries = null;
		if (isSanityChecksPassed(rowsWithoutHeaderAndTrailer)) 
		{
			unrealizeddbOperations.deleteEntries();
			entries = unrealizeddbOperations.insertUnrealizedDataFromMoneycontrol(rowsWithoutHeaderAndTrailer);
		} 
		else
		{
			entries = unrealizeddbOperations.getEntries(AllScripsDbObject.MONEY_CONTROL_NAME);
		}
		
		JdoDbOperations<AllScripsDbObject> allScripsDbOperations = new JdoDbOperations<AllScripsDbObject>(AllScripsDbObject.class);
		List<Stock> exceptionStocks = FastList.newList();
		for (UnrealizedDbObject dbObject : entries) {
			String moneycontrolName = dbObject.getMoneycontrolName();
			if(StringUtil.isValidValue(moneycontrolName))
			{
				String buyDate = dbObject.getBuyDate();
				double buyPriceDouble = dbObject.getBuyPrice();
				long quantity = dbObject.getBuyQuantity();

				stock = new StockBuilder().name(moneycontrolName)
						.quantity((int) quantity).buyPrice((float) buyPriceDouble)
						.buyDate(buyDate).sellDate(DateUtil.todaysDate()).build();

				final List<AllScripsDbObject> scrips = allScripsDbOperations.getEntries(AllScripsDbObject.MONEY_CONTROL_NAME,FastList.newListWith(moneycontrolName));

				if (!scrips.isEmpty()) 
				{
					final AllScripsDbObject aScrip = scrips.get(0);
					if (StringUtil.isValidValue(aScrip.getBseId())) 
					{
						stock.addNames(StockExchange.BSE, aScrip.getBseId());
						stock.setBlackListed(aScrip.isBlackListed());
						stocks.add(stock);
					}
				} 
				else 
				{
					stock.setIsException("No mapping found");
					exceptionStocks.add(stock);
				}
			}
		}

		Iterate.sortThis(stocks, NAME_DATE_PRICE_COMPARATOR);
		
		final List<Stock> bonusScnearioHandledStocks = handleBonusScneario(stocks);
		
		StockQuandlApiAdapter.stampLatestClosePriceAndDate(bonusScnearioHandledStocks);
		
		final Pair<List<Stock>, List<Stock>> stocksAndExceptions = Tuples.pair(bonusScnearioHandledStocks, exceptionStocks);
		
		return stocksAndExceptions;
	}
			
	//hack remove later - 'manual' is passed from the UI.
	private boolean isSanityChecksPassed(List<String> rowsWithoutHeaderAndTrailer) {
		return (!"manual".equals(rowsWithoutHeaderAndTrailer.get(0)));
	}


			public JsonObject getStockInvestmentChart(Collection<Stock> stocksSummary)
			{
				JsonObject jsonData = new JsonObject();

				float totalReturns = 0.0f;
				float totalInvestment = 0.0f;

//				List<Stock> stocksWithMaturityMoreThanAQuarter = ((FastList<Stock>)stocksSummary).select(MATURITY_MORE_THAN_A_QUARTER);

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

			@SuppressWarnings("serial")
			public List<Stock> getRequestedStockStatusInfo(List<Stock> stocksSummary)
			{
				if (this.stockName==null || "".equals(this.stockName))
				{
					return stocksSummary;
				}
				
				final List<Stock> requestedStockSummary =(List<Stock>) Iterate.selectWith(stocksSummary, new Predicate2<Stock, String>()
						{
					@Override
					public boolean accept(Stock stock, String interestedStock)
					{
						return interestedStock.equalsIgnoreCase(stock.getStockName());
					}
						}, this.stockName);
				
				return requestedStockSummary;
				
				
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

			public NDaysPrice getLatestClosePrice(List<String> rows, String stock)
			{
				Map<String, String> dateToCloseValue = new UnifiedMap<String, String>();
				if (rows.get(1).split(",").length > 5)
				{
					double closePrice = ReaderUtil.parseForClosePrice(rows.get(1)).doubleValue();
					dateToCloseValue.put(ReaderUtil.parseForDate(rows.get(1)), DOUBLE_FORMAT.format(closePrice));
				}
				return new NDaysPrice(stock, dateToCloseValue);
			}

			public void persistResults(List<Stock> stocksDetail, List<Stock> stocksSummary, ProfitAndLossDbObject profitAndLoss) 
			{
				final List<UnrealizedDetailDbObject> unrealizedDetailDbObjects = Adapter.stockToUnrealizedDetailDbObject(stocksDetail);
				JdoDbOperations<UnrealizedDetailDbObject> dbDetailOperations = new JdoDbOperations<UnrealizedDetailDbObject>(UnrealizedDetailDbObject.class);
				dbDetailOperations.deleteEntries();
				dbDetailOperations.insertEntries(unrealizedDetailDbObjects);

				final List<UnrealizedSummaryDbObject> unrealizedSummaryDbObjects = Adapter.stockToUnrealizedSummaryDbObject(stocksSummary);
				JdoDbOperations<UnrealizedSummaryDbObject> dbSummaryOperations = new JdoDbOperations<UnrealizedSummaryDbObject>(UnrealizedSummaryDbObject.class);
				dbSummaryOperations.deleteEntries();
				dbSummaryOperations.insertEntries(unrealizedSummaryDbObjects);
				
				JdoDbOperations<ProfitAndLossDbObject> profitAndLossOperations = new JdoDbOperations<ProfitAndLossDbObject>(ProfitAndLossDbObject.class);
				profitAndLossOperations.insertEntries(FastList.newListWith(profitAndLoss));
			}

}

