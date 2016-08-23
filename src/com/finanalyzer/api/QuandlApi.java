package com.finanalyzer.api; 

import java.util.Set;

import com.finanalyzer.api.SimpleQuery.Collapse;
import com.finanalyzer.domain.StockExchange;
import com.finanalyzer.util.DateUtil;

public class QuandlApi
{
	private static QuandlConnection CONNECTION= new QuandlConnection();

	public static QDataset getNDaysGains(Set<String> stockIdentifiers, int numOfDays, StockExchange stockExchange)
	{
		MultisetQuery mq = (MultisetQuery) Queries.create(stockIdentifiers).numRows(numOfDays).stockExchange(stockExchange).
		transform(SimpleQuery.Transform.RDIFF);
		return getQdataSet(mq);
	}

	public static QDataset getCumulativeValue(Set<String> stockIdentifiers, int numOfDays, StockExchange stockExchange)
	{
		MultisetQuery mq = (MultisetQuery) Queries.create(stockIdentifiers).numRows(numOfDays).stockExchange(stockExchange); 
		return getQdataSet(mq);
	}

	public static QDataset getLatestNClosePrices(Set<String> stockIdentifiers, int numberOfDays, StockExchange stockExchange)
	{
		MultisetQuery mq = (MultisetQuery) Queries.create(stockIdentifiers).numRows(numberOfDays).stockExchange(stockExchange);
		return getQdataSet(mq);
	}

	public static QDataset getNDaysClosePrices(String stockName, int numOfDays, StockExchange stockExchange)
	{
		String modifiedStockNameForQuandl= getModifiedStockName(stockName);
		SimpleQuery query = Queries.create(modifiedStockNameForQuandl).numRows(numOfDays).stockExchange(stockExchange);
		return getQdataSet(query);
	}
	
	//for ease of testing
	public static String getModifiedStockName(String stockName) 
	{
		return stockName.replaceAll("-", "_").toUpperCase();
	}

	private static QDataset getQdataSet(SimpleQuery mq)
	{
		QDataset qdataset = CONNECTION.getDataset(mq);
		return qdataset;
	}

}
