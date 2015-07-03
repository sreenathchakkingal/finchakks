package com.finanalyzer.api;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.mortbay.log.Log;

import com.finanalyzer.domain.DateValueObject;
import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.StockExchange;
import com.finanalyzer.processors.UnRealizedPnLProcessor;
import com.finanalyzer.util.StringUtil;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.tuple.Tuples;

public class StockQuandlApiAdapter
{
	private static final Logger LOG = Logger.getLogger(StockQuandlApiAdapter.class.getName());
	
	public static void stampNYearsClosePrices(Stock stock, Integer numberOfYears)
	{
		String stockName = stock.getStockName();
		QDataset qDataSet = QuandlApi.getNYearsClosePrices(stockName, numberOfYears, stock.getStockExchange());
		List<DateValueObject> dateValueObjects = transposeSingleQDataSet(qDataSet);
		Map<String, List<DateValueObject>> transposedQDataSet=UnifiedMap.newMapWith(Tuples.pair(stock.getStockName(), dateValueObjects));
		enrichNClosePriceDetails(stock,transposedQDataSet);
	}

	//multi-data set implementation - remove it after the single data set implementation is successful
//	public static void stampLatestClosePriceAndDate(List<Stock> stocks)
//	{
//		final StockExchange stockExchange = stocks.get(0).getStockExchange();
//		UnifiedSet<Stock> uniqueSetOfStocks = UnifiedSet.newSet(stocks);
//		UnifiedSet<String> stockIdentifiers = uniqueSetOfStocks.collect(STOCK_NAME_COLLECTOR);
//		QDataset qDataSet = QuandlApi.getLatestNClosePrices(stockIdentifiers, 5, stockExchange);
//		Map<String, List<DateValueObject>> transposedQDataSet = transposeMultiQDataSet(qDataSet, true, stockExchange);
//		enrichSellDetails(stocks,transposedQDataSet);
//	}

	public static void stampSimpleMovingAverage(List<Stock> stocks, int numberOfDays)
	{
		StockExchange stockExchange = stocks.get(0).getStockExchange();
		UnifiedSet<Stock> uniqueSetOfStocks = UnifiedSet.newSet(stocks);
		UnifiedSet<String> uniqueListOfStockIdentiers = uniqueSetOfStocks.collect(Stock.STOCKNAME_SELECTOR);
		QDataset qDataSet = QuandlApi.getCumulativeValue(uniqueListOfStockIdentiers, numberOfDays, stockExchange);
		Map<String, List<DateValueObject>> transposedQDataSet = transposeMultiQDataSet(qDataSet, stockExchange);
		enrichSimpleMovingAverage(stocks, transposedQDataSet, numberOfDays);
	}

	public static void stampNDaysGains(List<Stock> stocks, int numOfDays)
	{
		StockExchange stockExchange = stocks.get(0).getStockExchange();//assumption when a list of stocks is passed all of them will the same stockExchangeids
		UnifiedSet<String> uniqueListOfStockIdentiers = UnifiedSet.newSet(stocks).collect(Stock.STOCKNAME_SELECTOR);
		QDataset qDataSet = QuandlApi.getNDaysGains(uniqueListOfStockIdentiers,numOfDays, stockExchange);
		Map<String, List<DateValueObject>> transposedQDataSet = transposeMultiQDataSet(qDataSet, stockExchange);
		enrichNDaysGains(stocks, transposedQDataSet);
	}

	private static void enrichSellDetails(List<Stock> stocks, Map<String, List<DateValueObject>> result)
	{
		for (Stock stock : stocks)
		{
			List<DateValueObject> dateValues = result.get(stock.getStockName());
			stock.setSellDate(dateValues.get(0).getDate());
			stock.setSellPrice(dateValues.get(0).getValue());
		}
	}

	private static void enrichSimpleMovingAverage(List<Stock> stocks, Map<String, List<DateValueObject>> result, int numberOfDays)
	{
		for (Stock stock : stocks)
		{
			float sumOfClosePricesOfAllDaysForEachStock = 0.0f;
			List<DateValueObject> dateValues = result.get(stock.getStockName());	
			for (DateValueObject dateValue : dateValues)
			{
				sumOfClosePricesOfAllDaysForEachStock=sumOfClosePricesOfAllDaysForEachStock+dateValue.getValue();
			}
			stock.setSimpleMovingAverage(sumOfClosePricesOfAllDaysForEachStock / numberOfDays);
		}
	}

	private static Map<String, List<DateValueObject>> transposeMultiQDataSet(QDataset qDataSet, StockExchange stockExchange)
	{
		return transposeMultiQDataSet(qDataSet, false, stockExchange);
	}

	//isPickAllRecords=false => transpose first valid record
	private static Map<String, List<DateValueObject>> transposeMultiQDataSet(QDataset qDataSet, boolean isPickOnlyOneRecord, StockExchange stockExchange)
	{
		List<String> columns = qDataSet.getformattedColumnNames(stockExchange);
		List<QEntry> qEntries = qDataSet.getDataset();
		Map<String, List<DateValueObject>> result = UnifiedMap.newMap();
		for(int i=1;i<columns.size();i++)
		{
			String eachColumn = columns.get(i);
			List<DateValueObject> dateValues = FastList.newList();
			String date;
			Float value;
			for (QEntry eachEntry : qEntries)
			{
				String valueAtColumn = eachEntry.valueAtColumn(i);
				boolean isValidValue = !StringUtil.isInvalidValue(valueAtColumn);
				if(isValidValue)
				{
					value = Float.valueOf(valueAtColumn);
					date=eachEntry.getFormattedDate();
					dateValues.add(new DateValueObject(date, value));
				}
				if(isPickOnlyOneRecord && isValidValue)
				{
					break;
				}
			}
			result.put(eachColumn, dateValues);
		}
		return result;
	}

	private static List<DateValueObject> transposeSingleQDataSet(QDataset qDataSet)
	{
		List<QEntry> qEntries = qDataSet.getDataset();
		List<DateValueObject> dateValues = FastList.newList();
		String date;
		Float value;
		for (QEntry eachEntry : qEntries)
		{
			String valueAtColumn = eachEntry.valueAtColumn(1);
			boolean isValidValue = !StringUtil.isInvalidValue(valueAtColumn);
			if(isValidValue)
			{
				value = Float.valueOf(valueAtColumn);
				date=eachEntry.getFormattedDate();
				dateValues.add(new DateValueObject(date, value));
			}
		}
		return dateValues;
	}

	private static void enrichNDaysGains(List<Stock> stocks, Map<String, List<DateValueObject>> transposedQDataSet)
	{
		LinkedHashMap<String, Float> dateToCloseValue;
		for(Stock stock : stocks)
		{
			List<DateValueObject> dateValueObjects = transposedQDataSet.get(stock.getStockName());
			dateToCloseValue = new LinkedHashMap<>();
			for (DateValueObject dateValueObject : dateValueObjects)
			{
				Float gainForOneDay = dateValueObject.getValue() * 100;
				String date = dateValueObject.getDate();
				dateToCloseValue.put(date, gainForOneDay);
			}
			stock.setnDaysGains(dateToCloseValue);
			//todo : set nDaysGains as the datetoValueObject directly instead for conerting dateValueObject back to map
		}
	}

	private static void enrichNClosePriceDetails(Stock stock, Map<String, List<DateValueObject>> transposedQDataSet)
	{
		List<DateValueObject> dateValueObjects = transposedQDataSet.get(stock.getStockName());
		stock.setDateToClosePrice(dateValueObjects);
	}

	public static void stampLatestClosePriceAndDate(List<Stock> stocks)
	{
			stampNDaysClosePrices(stocks, 5);
	}
	
	public static void stampNDaysClosePrices(List<Stock> stocks, int numOfDays)
	{
		for (Stock eachStock: stocks)
		{
			try
			{
				stampNDaysClosePrices(eachStock, numOfDays);	
			}
			catch(Throwable t){
				LOG.warning("setIsException "+eachStock.getStockName());
				eachStock.setIsException();
			}
		}
		
	}

	private static void stampNDaysClosePrices(Stock stock, int numOfDays)
	{
//		QDataset qDataSet = QuandlApi.getNDaysClosePrices(stock.getStockName(), numOfDays, stock.getStockExchange()); chakks
		QDataset qDataSet = QuandlApi.getNDaysClosePrices(stock.getStockExchangeStocknameMap().get(StockExchange.BSE), numOfDays, StockExchange.BSE); 
		List<DateValueObject> dateValueObjects = transposeSingleQDataSet(qDataSet);
		stock.setDateToClosePrice(dateValueObjects);
	}
}
