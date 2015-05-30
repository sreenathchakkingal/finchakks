package com.finanalyzer.db;

import java.util.List;

import com.google.appengine.api.datastore.Entity;

public class StockInfoUtil extends AbstractCoreDb
{
	private final String NET_RETURNS = "netReturns";
	private final String UPS = "ups";
	private final String DOWNS = "downs";
	private final String INDUSTRY = "industry";
	private final String STOCK_INFO = "stockInfo";

	public void insertData(List<String> rawDataDFromFile)
	{
		for (String row : rawDataDFromFile)
		{
			String[] stockAttributes = row.split(",");
			String name = stockAttributes[0];
			float netReturns = Float.valueOf(stockAttributes[1]);	
			int ups = Integer.valueOf(stockAttributes[2]);
			int downs = Integer.valueOf(stockAttributes[3]);
			String industry = stockAttributes[5];

			Entity stockInfo = new Entity(this.getTableName());
			stockInfo.setProperty(this.STOCK_NAME, name);
			stockInfo.setProperty(this.NET_RETURNS, netReturns);
			stockInfo.setProperty(this.UPS, ups);
			stockInfo.setProperty(this.DOWNS, downs);
			stockInfo.setProperty(this.INDUSTRY, industry);
			this.DATASTORE.put(stockInfo);
		}
	}

	@Override
	public String getTableName()
	{
		return this.STOCK_INFO;
	}
}

