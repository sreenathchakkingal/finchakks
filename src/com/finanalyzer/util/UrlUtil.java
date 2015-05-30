package com.finanalyzer.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Set;

import com.finanalyzer.domain.StockExchange;
import com.gs.collections.impl.list.mutable.FastList;

public class UrlUtil
{
	private static final String YAHOO_URL_HEADER = "http://ichart.finance.yahoo.com/table.csv?s=";
	public static final String QUANDL_URL_HEADER = "https://www.quandl.com/api/v1/datasets/NSE/";
	// https://www.quandl.com/api/v1/datasets/NSE/MARICO.csv?auth_token=VHyaJFRgExWGKQYtacdP&trim_start=2014-09-10&trim_end=2014-09-15
	// https://www.quandl.com/api/v1/datasets/BSE/BOM500469.csv?trim_start=2014-09-19&trim_end=2014-09-24&auth_token=VHyaJFRgExWGKQYtacdP

	private static final String BO = ".BO";
	private static final String XLS_TRIM_START = ".csv?auth_token=VHyaJFRgExWGKQYtacdP&trim_start=";
	private static final String TRIM_END = "&trim_end=";
	private static final String YAHOO_CSV = "&ignore=.csv";
	private static final String QUANDL_DATE_FORMAT = "yyyy-MM-dd";
	private static final int NUMBER_OF_RETRIES = 5;

	//	public static List<String> getHistoricPricesFromQuandl(int numOfDays, String nseStockId)
	//	{
	//		List<String> rows = getResponseAsList(getQuandlDownloadUrl(numOfDays, nseStockId));
	//		return rows;
	//	}

	public static List<String> getHistoricPricesFromYahoo(int numOfDays, String yahooStockId)
	{
		List<String> rows = getResponseAsList(getYahooUrlForFromToDate(numOfDays, yahooStockId));
		return rows;
	}

	public static String getResponseAsString(String url)
	{
		String response = null;
		boolean isTryAgain = true;
		int counter=0;
		while(isTryAgain)
		{
			BufferedReader bufferedReader = getBufferedReader(url);
			response = ReaderUtil.convertBufferedReaderToString(bufferedReader);
			if(StringUtil.isInvalidValue(response))
			{
				isTryAgain=counter++<NUMBER_OF_RETRIES;
				try
				{
					Thread.sleep(5*1000);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				System.out.println("retrying: "+counter);
			}
			else
			{
				break;
			}
		}

		return response;
	}

	private static List<String> getResponseAsList(String url)
	{
		BufferedReader bufferedReader = getBufferedReader(url);
		List<String> response = ReaderUtil.convertToList(bufferedReader);
		return response;
	}

	private static BufferedReader getBufferedReader(String url)
	{
		int counter =0;
		BufferedReader bufferedReader = null;
		boolean isTryAgain = true;

		while(isTryAgain)
		{
			try
			{
				URL downloadUrl = new URL(url);
				URLConnection connection = downloadUrl.openConnection();
				connection.setConnectTimeout(5*60*1000);  
				connection.setReadTimeout(5*60*1000);  
				InputStreamReader inputStream = new InputStreamReader(connection.getInputStream());
				bufferedReader = new BufferedReader(inputStream);
				isTryAgain=false;
			} catch (Throwable e)
			{
				isTryAgain=counter++ < NUMBER_OF_RETRIES;
				try
				{
					Thread.sleep(5*1000);
				} catch (InterruptedException e1)
				{
					e1.printStackTrace();
				}
				System.out.println("retrying#: "+counter);
				e.printStackTrace();
			}
		}

		return bufferedReader;
	}

	//	private static String getQuandlDownloadUrl(int numOfDays, String quandlStockId)
	//	{
	//		String quandlUrlForDownload = QUANDL_URL_HEADER + quandlStockId + XLS_TRIM_START + DateUtil.getFromDate(numOfDays, QUANDL_DATE_FORMAT)
	//				+ TRIM_END + DateUtil.getTodaysDate(QUANDL_DATE_FORMAT);
	//
	//		return quandlUrlForDownload;
	//
	//	}

	private static String getYahooUrlForFromToDate(int numOfDays, String yahooStockId)
	{
		String yahooUrlForDownload = YAHOO_URL_HEADER + yahooStockId + BO + "&a=" + DateUtil.getFromMonthMinusOne(numOfDays) + "&b="
				+ DateUtil.getFromDay(numOfDays) + "&c=" + DateUtil.getFromYear(numOfDays) + "&d=" + DateUtil.getCurrentMonthMinusOne() + "&e="
				+ DateUtil.getCurrentDay() + "&f=" + DateUtil.getCurrentyear() + "&g=d" + YAHOO_CSV;

		return yahooUrlForDownload;
	}

	public static String getYahooUrlForTenYearsClosePrices(String yahooStockId)
	{
		String yahooUrlForDownload = YAHOO_URL_HEADER + yahooStockId + ".BO" + "&a=00" + "&b=01" + "&c=2000" + "&d="
				+ DateUtil.getCurrentMonthMinusOne() + "&e=" + DateUtil.getCurrentDay() + "&f=" + DateUtil.getCurrentyear() + "&g=m" + YAHOO_CSV;

		return yahooUrlForDownload;
	}

	public static String getYahooUrlForTenYearsDividends(String yahooStockId)
	{
		String yahooUrlForDownload = YAHOO_URL_HEADER + yahooStockId + BO + "&a=00" + "&b=01" + "&c=2000" + "&d="
				+ DateUtil.getCurrentMonthMinusOne() + "&e=" + DateUtil.getCurrentDay() + "&f=" + DateUtil.getCurrentyear() + "&g=v" + YAHOO_CSV;

		return yahooUrlForDownload;
	}

	public static String prefixIndexAndSuffixClosePriceIndex(Set<String> qCodes, StockExchange stockExchange)
	{
		StringBuffer transformedQCodes = new StringBuffer();
		List<String> qCodesAsList =FastList.newList(qCodes);
		for (int i=0; i<qCodesAsList.size();i++)
		{
			transformedQCodes.append(stockExchange.getQuandlMultisetPrefix());
			transformedQCodes.append(qCodesAsList.get(i));
			transformedQCodes.append(".");
			transformedQCodes.append(stockExchange.getQuandlClosePriceColumnPosition());
			boolean isLastIteration = i==qCodes.size()-1;
			if(isLastIteration)
			{
				transformedQCodes.append("&");
			}
			else
			{
				transformedQCodes.append(",");
			}
		}
		return transformedQCodes.toString();
	}

}
