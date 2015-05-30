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

//	public static QDataset getNthDayClosePrice(Set<String> uniqueListOfStockIdentiers, int numOfDays, StockExchange stockExchange)
//	{
//		String fromDate = DateUtil.getFromBusinessDate(numOfDays);
//		MultisetQuery mq = (MultisetQuery) Queries.create(uniqueListOfStockIdentiers).dateRange(fromDate, fromDate).stockExchange(stockExchange);
//		QDataset qdataset = CONNECTION.getDataset(mq);
//		return qdataset;
//	}

	public static QDataset getNYearsClosePrices(String stockName, Integer numberOfYears, StockExchange stockExchange)
	{
		SimpleQuery query = Queries.create(stockName.toUpperCase()).collapse(Collapse.ANNUAL).stockExchange(stockExchange);
		query=query.numRows(numberOfYears);
		return getQdataSet(query);
	}

	public static QDataset getNDaysClosePrices(String stockName, int numOfDays, StockExchange stockExchange)
	{
		SimpleQuery query = Queries.create(stockName.toUpperCase()).numRows(numOfDays).stockExchange(stockExchange);
		return getQdataSet(query);
	}
	
	private static QDataset getQdataSet(SimpleQuery mq)
	{
		QDataset qdataset = CONNECTION.getDataset(mq);
		return qdataset;
	}

}
