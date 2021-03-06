package com.finanalyzer.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;




import com.finanalyzer.domain.DateValueObject;
import com.finanalyzer.endpoints.MaintainanceControllerEndPoint;
import com.gs.collections.impl.list.mutable.FastList;

public class ReaderUtil
{
	private static final String N_A = "N/A";
	public static final String COMMA = ",";
	private static final int INDEX_OF_DATE_IN_EXCELSHEET = 0;
	private static final int INDEX_OF_CLOSEVALUE_IN_YAHOO_SHEET = 4;
	private static final int INDEX_OF_CLOSEVALUE_IN_QUANDL_SHEET = 5;

	private static final int INDEX_OF_DIVIDENT_IN_EXCELSHEET = 1;
	private static final int INDEX_OF_GRAPH_OPINION_IN_EXCELSHEET = 6;
	private static final int INDEX_OF_REPORTED_NET_PROFIT_IN_EXCELSHEET = 7;
	private static final int INDEX_OF_DEBT_TO_EQUITY_IN_EXCELSHEET = 8;

	private static final Logger LOG = Logger.getLogger(ReaderUtil.class.getName());
	
	public static List<String> convertToList(Object object, boolean isRemoveHeader, int numberOfTrailersToBeRemoved)
	{
		List<String> rows = convertToList(object);
		int startIndex = isRemoveHeader ? 1 : 0;
		int endIndex = rows.size()-numberOfTrailersToBeRemoved;//for realized this needs to be rows.size-1 -hack remove this later on 
		if (endIndex> startIndex)
		{
			return rows.subList(startIndex, endIndex);	
		}
		return rows;
	}

	public static List<String> convertToList(Object object)
	{
		if(object instanceof FileItemIterator)
		{
			return converFileItemIteratorToList((FileItemIterator)object);	
		}

		if(object instanceof BufferedReader)
		{
			return converBufferedReaderToList((BufferedReader)object);	
		}

		if(object instanceof InputStream)
		{
			return converInputReaderToList((InputStream)object);	
		}
		if (object instanceof String)
		{
			return convertStringContentToList((String)object);
		}

		return FastList.newList();
	}

	private static List<String> converFileItemIteratorToList(FileItemIterator fileItemIterator)
	{
		List<String> rows = FastList.newList();
		try
		{
			while (fileItemIterator.hasNext())
			{
				FileItemStream item = fileItemIterator.next();
				InputStream inputStream = item.openStream();
				rows.addAll(converInputReaderToList(inputStream));	
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return rows;
	}

	private static List<String> converBufferedReaderToList(BufferedReader br)
	{
		List<String> rows = FastList.newList();
		if (br != null)
		{
			try
			{
				String currentLine;
				while ((currentLine = br.readLine()) != null)
				{
					rows.add(currentLine);
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return rows;
	}

	private static List<String> converInputReaderToList(InputStream inputStream)
	{
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		return converBufferedReaderToList(bufferedReader);
	}
	
	private static List<String> convertStringContentToList(String content)
	{
		List<String> rows = Arrays.asList(content.split("\n"));
		return rows;
	}

	public static String parseForDate(String row)
	{
		try
		{
			return row.split(",")[INDEX_OF_DATE_IN_EXCELSHEET];
		} catch (Exception e)
		{
		}
		return "";
	}

	public static Float parseForClosePrice(String row)
	{
		try
		{
			return Float.valueOf(row.split(",")[INDEX_OF_CLOSEVALUE_IN_YAHOO_SHEET]);
		} catch (Exception e)
		{
		}
		return Float.valueOf(0.0f);
	}

	public static Float parseForClosePriceFromQuandl(String row)
	{
		try
		{
			return Float.valueOf(row.split(",")[INDEX_OF_CLOSEVALUE_IN_QUANDL_SHEET]);
		} catch (Exception e)
		{
		}
		return Float.valueOf(0.0f);
	}


	public static Float parseForDividend(String row)
	{
		return Float.valueOf(row.split(",")[INDEX_OF_DIVIDENT_IN_EXCELSHEET]);
	}

	public static String parseStockName(String stockAttribute)
	{
		return stockAttribute.contains(" - ") ? stockAttribute.substring(0, stockAttribute.indexOf(" - ")) : stockAttribute;
	}

	public static String fetchGraphOpinion(String row)
	{
		return (row == null) || (row.split(",").length <= INDEX_OF_GRAPH_OPINION_IN_EXCELSHEET) ? N_A
				: row.split(",")[INDEX_OF_GRAPH_OPINION_IN_EXCELSHEET];
	}

	public static String fetchReportedNetProfitOpinion(String row)
	{
		return (row == null) || (row.split(",").length <= INDEX_OF_REPORTED_NET_PROFIT_IN_EXCELSHEET) ? N_A
				: row.split(",")[INDEX_OF_REPORTED_NET_PROFIT_IN_EXCELSHEET];
	}

	public static String fetchDebtToEquityOpinion(String row)
	{
		return (row == null) || (row.split(",").length <= INDEX_OF_DEBT_TO_EQUITY_IN_EXCELSHEET) ? N_A
				: row.split(",")[INDEX_OF_DEBT_TO_EQUITY_IN_EXCELSHEET];
	}

//	public static String removeCommanBetweenQuotes(String row)
//	{
//		StringBuffer resultString = new StringBuffer();
//		try
//		{
//			Pattern regex = Pattern.compile("(.*)\"(.*),(.*)\"(.*)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
//			Matcher regexMatcher = regex.matcher(row);
//			int counter=0;
//			boolean isToBeReplace = regexMatcher.find();
//			while (isToBeReplace)
//			{
//				regexMatcher.appendReplacement(resultString, "$1$2$3$4");
//				row=resultString.toString();
//				isToBeReplace=regex.matcher(resultString.toString()).find(); 
//				System.out.println("counter: " + counter++ +" : "+isToBeReplace );
//			}
//			regexMatcher.appendTail(resultString);
//		} catch (Exception ex)
//		{
//			ex.printStackTrace();
//		}
//		return resultString.toString();
//	}
	
	public static String[]  removeCommanBetweenQuotes(String row)
	{
	      Pattern p = Pattern.compile(",(?=([^\"]*\"[^\"]*\")*(?![^\"]*\"))");
          // Split input with the pattern
          String[] fields = p.split(row);
          for (int i = 0; i < fields.length; i++) {
              // Get rid of residual double quotes
        	  fields[i]= fields[i].replace("\"", "").replace(",", "");
          }
          return fields;
	}

	public static String convertBufferedReaderToString(BufferedReader bufferedReader)
	{
		StringBuffer sb = new StringBuffer();
		if (bufferedReader != null)
		{
			try
			{
				String currentLine;
				while ((currentLine = bufferedReader.readLine()) != null)
				{
					sb.append(currentLine);
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static List<DateValueObject> parseHistoricalClosePrices(String historicalClosePrices, Integer numberOfYears)
	{
		List<String> rows = Arrays.asList(historicalClosePrices.split("\n"));
		final int size = rows.size();
		if (numberOfYears !=null)
		{
			int endIndex =numberOfYears > size ? size :  numberOfYears;
			rows=rows.subList(0, endIndex);
		}
		final List<DateValueObject> dateValueObjects= FastList.newList();
		for (String row : rows)
		{
			final String[] columns = row.split("\t");
			dateValueObjects.add(new DateValueObject(columns[0], Float.valueOf(columns[4])));
		}
		return dateValueObjects;
	}

}
