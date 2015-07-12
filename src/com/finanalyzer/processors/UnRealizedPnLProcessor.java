package com.finanalyzer.processors;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.commons.fileupload.FileItemIterator;

import com.finanalyzer.api.QuandlConnection;
import com.finanalyzer.api.StockQuandlApiAdapter;
import com.finanalyzer.db.StockIdConverstionUtil;
import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.db.jdo.PMF;
import com.finanalyzer.domain.NDaysPrice;
import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.StockBuilder;
import com.finanalyzer.domain.StockExchange;
import com.finanalyzer.domain.jdo.AllScripsDbObject;
import com.finanalyzer.domain.jdo.UnrealizedDbObject;
import com.finanalyzer.util.DateUtil;
import com.finanalyzer.util.ReaderUtil;
import com.finanalyzer.util.StringUtil;
import com.google.gson.JsonObject;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
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
	public FastList<Stock> execute() {
		FastList<Stock> stocks = FastList.newList();
		Stock stock;

		JdoDbOperations<UnrealizedDbObject> unrealizeddbOperations = new JdoDbOperations<UnrealizedDbObject>(UnrealizedDbObject.class);

		List<String> rowsWithoutHeaderAndTrailer = ReaderUtil.convertToList(this.fileItemIterator, true, true);

		if (!rowsWithoutHeaderAndTrailer.isEmpty()) 
		{
			unrealizeddbOperations.deleteEntries();
			unrealizeddbOperations.insertUnrealizedDataFromMoneycontrol(rowsWithoutHeaderAndTrailer);
			try {
				Thread.sleep(3*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} 
		
		List<UnrealizedDbObject> entries = unrealizeddbOperations.getEntries(AllScripsDbObject.MONEY_CONTROL_NAME);
		
		JdoDbOperations<AllScripsDbObject> allScripsDbOperations = new JdoDbOperations<AllScripsDbObject>(AllScripsDbObject.class);
		Set<String> noMapping = UnifiedSet.newSet();
		for (UnrealizedDbObject dbObject : entries) {
			String moneycontrolName = dbObject.getMoneycontrolName();
			if(StringUtil.isValidValue(moneycontrolName))
			{
				String buyDate = dbObject.getBuyDate();
				double buyPriceDouble = dbObject.getBuyPrice();
				long quantity = dbObject.getBuyQuantity();

				stock = new StockBuilder().name(moneycontrolName)
						.quantity((int) quantity).buyPrice((float) buyPriceDouble)
						.buyDate(buyDate).build();

				final List<AllScripsDbObject> scrips = allScripsDbOperations.getEntries(AllScripsDbObject.MONEY_CONTROL_NAME,moneycontrolName);

				if (!scrips.isEmpty()) {
					if (StringUtil.isValidValue(scrips.get(0).getBseId())) 
					{
						stock.addNames(StockExchange.BSE, scrips.get(0).getBseId());
					}
				} 
				else 
				{
					noMapping.add(moneycontrolName);
				}
				stocks.add(stock);		
			}
		}

		if (!noMapping.isEmpty()) {
			throw new RuntimeException("no mapping found for " + noMapping);
		}
		
		stocks.sortThis(NAME_DATE_PRICE_COMPARATOR);
		final FastList<Stock> handleBonusScneario = handleBonusScneario(stocks);
		
		StockQuandlApiAdapter.stampLatestClosePriceAndDate(handleBonusScneario);
		return handleBonusScneario;
	}
			
//			@Override chakks
//			public FastList<Stock> execute()
//			{
//				UnRealizedUtil unRealizedUtil = new UnRealizedUtil();
//
//				List<String> rowsWithoutHeaderAndTrailer =ReaderUtil.convertToList(this.fileItemIterator, true, true);
//				if(!rowsWithoutHeaderAndTrailer.isEmpty())
//				{
//					unRealizedUtil = new UnRealizedUtil();
//					unRealizedUtil.removeAllEntities();
//					unRealizedUtil.insertData(rowsWithoutHeaderAndTrailer);
//				}
//				FastList<Stock> stocks = (FastList<Stock>) unRealizedUtil.retrieveAllRecords();
//				AllScripsUtil allScripsUtil = AllScripsUtil.getInstance();
//				allScripsUtil.convertNseIdToBse(stocks);
//
//				StockQuandlApiAdapter.stampLatestClosePriceAndDate(stocks);
//				allScripsUtil.convertBseIdToNse(stocks);
//				stocks.sortThis(NAME_QTY_DATE_COMPARATOR);
//				return stocks;
//			}

			//	public FastList<Stock> execute()
			//	{
			//		List<String> rows = ReaderUtil.converInputReaderToList(this.statusInputStream);
			//		FastList<Stock> stocks = FastList.newList();
			//		for (String row : rows)
			//		{
			//			Stock stock = convertRowToStockObject(row);
			//			if (stock != null)
			//			{
			//				stocks.add(stock);
			//			}
			//		}
			//		for (Stock stock : stocks)
			//		{
			//			try
			//			{
			//				if("Net".equals(this.source))
			//				{
			//					IdConverterUtil.stampYahooId(stocks, this.idMappingInputStream);
			//					stampSellDateAndLatestClosePrice(stock);
			//				}
			//
			//			} catch (Throwable e)
			//			{
			//				e.printStackTrace();
			//			}
			//		}
			//		
			//		return stocks;
			//	}

			//	private String stampSellDateAndLatestClosePrice(Stock stock)
			//	{
			//		List<String> historicPrices = UrlUtil.getHistoricPricesFromYahoo(DateUtil.NUMBER_OF_DAYS_IN_MONTH, stock.getStockName());
			//		NDaysPrice latestDayPrice = getLatestClosePrice(historicPrices, stock.getStockName());
			//		String sellDate = null;
			//		double latestClosePrice = 0.0D;
			//		for (Map.Entry<String, String> entry : latestDayPrice.getDateToCloseValue().entrySet())
			//		{
			//			sellDate = (String) entry.getKey();
			//			latestClosePrice = Double.valueOf((String) entry.getValue()).doubleValue();
			//		}
			//		stock.setSellDate(sellDate);
			//		stock.setSellPrice(latestClosePrice);
			//		return sellDate;
			//	}

			public JsonObject getStockInvestmentChart(Collection<Stock> stocksSummary)
			{
				JsonObject jsonData = new JsonObject();

				float totalReturns = 0.0f;
				float totalInvestment = 0.0f;

				List<Stock> stocksWithMaturityMoreThanAQuarter = ((FastList<Stock>)stocksSummary).select(MATURITY_MORE_THAN_A_QUARTER);

				for (Stock stock : stocksWithMaturityMoreThanAQuarter)
				{
					totalReturns +=stock.getReturnTillDate();
					totalInvestment += Math.abs(stock.getTotalInvestment());
				}
				for (Stock stock : stocksWithMaturityMoreThanAQuarter)
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
			public List<Stock> getRequestedStockStatusInfo(MutableList<Stock> stocksSummary)
			{
				if (this.stockName==null || "".equals(this.stockName))
				{
					return stocksSummary;
				}
				return stocksSummary.selectWith(new Predicate2<Stock, String>()
						{
					@Override
					public boolean accept(Stock stock, String interestedStock)
					{
						return interestedStock.equalsIgnoreCase(stock.getStockName());
					}
						}, this.stockName);
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

			private Stock convertRowToStockObject(String row)
			{
				Stock stock = null;
				String[] stockAttributes = ReaderUtil.removeCommanBetweenQuotes(row).split(",");
				String name = ReaderUtil.parseStockName(stockAttributes[0]);
				StockIdConverstionUtil stockIdConverstionUtil = new StockIdConverstionUtil();
				String nseId = stockIdConverstionUtil.getNseIdForMoneyControlId(name);
				int quantityColumnPosition = 5;
				int quantity =  Integer.valueOf(stockAttributes[quantityColumnPosition]).intValue();

				int invoicePriceColumnPosition =  6;
				float buyPrice = Float.valueOf(stockAttributes[invoicePriceColumnPosition]);

				int invoiceDateColumnPosition = 7;
				String invoiceDate=stockAttributes[invoiceDateColumnPosition];

				String buyDate;
				if(invoiceDate.contains("/"))
				{
					buyDate = DateUtil.convertToStandardFormat("d/M/yyyy",invoiceDate);
				}
				else
				{
					buyDate = DateUtil.convertToStandardFormat("dd-MM-yyyy",invoiceDate);
				}

				//			double sellPrice = Double.valueOf(stockAttributes[1]).doubleValue();
				//			String sellDate = DateUtil.getTodaysDate(DateUtil.YYYY_MM_DD_FORMAT);

				stock = new StockBuilder().name(nseId).quantity(quantity).buyPrice(buyPrice).buyDate(buyDate).
						//					sellDate(sellDate).sellPrice(sellPrice).
						build();
				return stock;
			}
}

