package com.finanalyzer.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.jquantlib.time.calendars.India;

public class DateUtil
{

	public static final India INDIA_CALENDAR = new India();
	public static final SimpleDateFormat MM_FORMAT = new SimpleDateFormat("MM");
	public static final String QUANDL_DATE_FORMAT = "yyyy-MM-dd";
	public static final SimpleDateFormat YYYY_MM_DD_FORMAT = new SimpleDateFormat(QUANDL_DATE_FORMAT);

	public static int NUMBER_OF_DAYS_IN_MONTH = 30;
	public static int NUMBER_OF_DAYS_IN_YEAR = 365;
	public static int NUMBER_OF_DAYS_IN_A_WEEK = 7;

	public static boolean isDateBeforeTargetDate(String targetDateAsString, float tolerance)
	{
		try
		{
			Date dateFrom = YYYY_MM_DD_FORMAT.parse(todaysDate());
			Date targetDate = YYYY_MM_DD_FORMAT.parse(targetDateAsString);
			return CalculatorUtil.isValueLessThanTarget(dateFrom.getTime(), targetDate.getTime(), tolerance);
		}
		catch (Exception e) {
			throw new RuntimeException("incorrect date format: "+targetDateAsString+" expected : YYYY_MM_DD format");
		}
	}
	
	public static boolean isDateAfterTargetDate(String targetDateAsString, float tolerance)
	{
		try
		{
			Date dateFrom = YYYY_MM_DD_FORMAT.parse(todaysDate());
			Date targetDate = YYYY_MM_DD_FORMAT.parse(targetDateAsString);
			return CalculatorUtil.isValueMoreThanTarget(dateFrom.getTime(), targetDate.getTime(), tolerance);
		}
		catch (Exception e) {
			throw new RuntimeException("incorrect date format: "+targetDateAsString+" expected : YYYY_MM_DD format");
		}
	}
	
	public static int differenceBetweenDates(String date1, String date2)
	{
		try
		{
			Date dateTo = YYYY_MM_DD_FORMAT.parse(date1);
			Date dateFrom = YYYY_MM_DD_FORMAT.parse(date2);
			long diff = dateTo.getTime() - dateFrom.getTime();
			return (int)(diff / 86400000L);
		}
		catch (Exception e) {}
		return 0;
	}
	
	public static String todaysDate()
	{
		return YYYY_MM_DD_FORMAT.format(new Date());
	}
	
	public static String approxDurationInMonthsAndYears(String fromDate)
	{
		final int differenceBetweenTwoDates = DateUtil.differenceBetweenDates(YYYY_MM_DD_FORMAT.format(new Date()), fromDate);
		String result = "";
		int numberOfYears = differenceBetweenTwoDates/365;
		result=numberOfYears+"Y:";
		
		int numberOfMonths = (differenceBetweenTwoDates%365)/30;
		result=result+numberOfMonths+"M";

		return result;
	}
	
	public static String convertToStandardFormat(String format, String date)
	{
		try
		{
			return YYYY_MM_DD_FORMAT.format(new SimpleDateFormat(format).parse(date));
		}
		catch (ParseException e) {}
		return null;
	}

	public static String getTodaysDate(String format)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(new Date());
	}

	//used only by yahoo - delete later.	
	public static String getFromDate(int number_of_days, String format)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_WEEK, -1 * number_of_days);
		if (calendar.get(Calendar.DAY_OF_WEEK) == 7) {
			calendar.add(Calendar.DAY_OF_WEEK, -1);
		}
		else if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
			calendar.add(Calendar.DAY_OF_WEEK, 1);
		}
		return dateFormat.format(calendar.getTime());
	}
	
	//used only by yahoo - delete later.
	public static String getFromMonthMinusOne(int numOfDays)
	{
		String date = getFromDate(numOfDays, "MM");
		return calculateMonthMinusOne(date);
	}

	//used only by yahoo - delete later.
	private static String calculateMonthMinusOne(String date)
	{
		int dateMinusOne = Integer.parseInt(date) - 1;
		if (dateMinusOne < 10) {
			return "0" + String.valueOf(dateMinusOne);
		}
		return String.valueOf(dateMinusOne);
	}

	//used only by yahoo - delete later.
	public static String getFromDay(int numOfDays)
	{
		return getFromDate(numOfDays, "d");
	}

	//used only by yahoo - delete later.
	public static String getFromYear(int numOfDays)
	{
		return getFromDate(numOfDays, "yyyy");
	}

	//used only by yahoo - delete later.
	public static String getCurrentMonthMinusOne()
	{
		return calculateMonthMinusOne(getTodaysDate("MM"));
	}

	//used only by yahoo - delete later.
	public static String getCurrentDay()
	{
		return getTodaysDate("d");
	}

	//used only by yahoo - delete later.
	public static String getCurrentyear()
	{
		return getTodaysDate("yyyy");
	}

}
