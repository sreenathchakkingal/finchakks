package com.finanalyzer.db;

import java.util.List;

import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.StockExchange;
import com.google.appengine.api.datastore.Entity;
import com.gs.collections.impl.list.mutable.FastList;

public class AllScripsUtil extends AbstractCoreDb
{

	private static final String ALL_SCRIPS = "allScrips";

	private final String ISIN = "isin";
	private final String BSE_ID = "bseId";
	private final String FAIR_VALUE = "fairValue";
	private final String INDUSTRY = "industry";
	private static AllScripsUtil instance =null;

	private AllScripsUtil(){}

	public static AllScripsUtil getInstance()
	{
		if(instance==null)
		{
			instance=new AllScripsUtil();
		}
		return instance;
	}

	public void insertData(List<String> rawDataDFromFile)
	{
		for (String row : rawDataDFromFile)
		{
			//			System.out.println("inserting "+counter++ + row);
			String[] stockAttributes = row.split(",");
			String isin = stockAttributes[0];
			String stockName = stockAttributes[1];
			String bseId = stockAttributes[2];
			String nseId = stockAttributes[3];
			int fairValue = Integer.valueOf(stockAttributes[4]);
			String industry = stockAttributes[5];

			Entity stockInfo = new Entity(this.getTableName());
			stockInfo.setProperty(this.ISIN, isin);
			stockInfo.setProperty(this.STOCK_NAME, stockName);
			stockInfo.setProperty(this.BSE_ID, bseId);
			stockInfo.setProperty(this.NSE_ID, nseId);
			stockInfo.setProperty(this.FAIR_VALUE, fairValue);
			stockInfo.setProperty(this.INDUSTRY, industry);
			this.DATASTORE.put(stockInfo);
		}
	}

	public String getBseIdFromNseId(String nseId)
	{
		final List<Entity> allEntities = this.retrieveAllEntities();
		for (Entity entity : allEntities)
		{
			if (nseId.equals(entity.getProperty(this.NSE_ID)))
			{
				return (String)entity.getProperty(this.BSE_ID);
			}
		}
		return "";
	}

	public String getNseIdFromBseId(String bseId)
	{
		final List<Entity> allEntities = this.retrieveAllEntities();
		for (Entity entity : allEntities)
		{
			if (bseId.equals(entity.getProperty(this.BSE_ID)))
			{
				return (String)entity.getProperty(this.NSE_ID);
			}
		}
		return "";
	}

	@Override
	public String getTableName()
	{
		return ALL_SCRIPS;

	}

	public void convertBseIdToNse(FastList<Stock> stocks)
	{
		for (Stock stock : stocks)
		{
			String bseId = stock.getStockName();
			final String nseId = this.getNseIdFromBseId(bseId);
			stock.setStockName(nseId);
			stock.setStockExchange(StockExchange.NSE);
		}
	}

	public void convertNseIdToBse(FastList<Stock> stocks)
	{
		for (Stock stock : stocks)
		{
			String nseId = stock.getStockName();
			final String bseId = this.getBseIdFromNseId(nseId);
			stock.setStockName(bseId);
			stock.setStockExchange(StockExchange.BSE);
		}

	}

}

