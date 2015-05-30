package com.finanalyzer.db;

import java.util.List;

import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.StockBuilder;
import com.finanalyzer.util.DateUtil;
import com.finanalyzer.util.ReaderUtil;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.gs.collections.impl.list.mutable.FastList;

public class UnRealizedUtil extends AbstractCoreDb

{
	private static String STOCK_NAME = "stockName";
	private static String BUY_DATE = "buyDate";
	private static String BUY_PRICE = "buyPrice";
	private static String QUANTITY = "quantity";
	private static final String UNREALIZED = "Unrealized";
	static DatastoreService DATASTORE = DatastoreServiceFactory.getDatastoreService();

	public void insertData(List<String> rawDataDFromMoneyControl)
	{
		for (String row : rawDataDFromMoneyControl)
		{
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

			Entity unrealizedData = new Entity(UNREALIZED);
			unrealizedData.setProperty(STOCK_NAME, nseId);
			unrealizedData.setProperty(BUY_DATE, buyDate);
			unrealizedData.setProperty(BUY_PRICE, buyPrice);
			unrealizedData.setProperty(QUANTITY, quantity);
			DATASTORE.put(unrealizedData);
		}
	}

	public List<Stock> retrieveAllRecords()
	{
		List<Entity> entities = this.retrieveAllEntities();
		List<Stock> stocks = FastList.newList();
		Stock stock;
		for (Entity entity : entities)
		{
			String nseId  = (String) entity.getProperty(STOCK_NAME);
			String buyDate  = (String) entity.getProperty(BUY_DATE);
			double buyPriceDouble = (Double)entity.getProperty(BUY_PRICE);
			long quantity  = (Long) entity.getProperty(QUANTITY);

			stock = new StockBuilder().name(nseId).quantity((int)quantity).buyPrice((float) buyPriceDouble).buyDate(buyDate).
					build();

			stocks.add(stock);		
		}
		return stocks;
	}

	@Override
	public String getTableName()
	{
		return UNREALIZED;
	}

}
