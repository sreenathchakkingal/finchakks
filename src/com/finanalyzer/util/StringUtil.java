package com.finanalyzer.util;

import java.util.List;

import com.gs.collections.impl.list.mutable.FastList;

public class StringUtil {

	public static boolean isInvalidValue(String response) {
		return response == null || "".equals(response) || "null".equalsIgnoreCase(response);
	}

	public static boolean isValidValue(String response) {
		return !isInvalidValue(response);
	}

	public static boolean isValidValue(String[] ratingsToBeRemoved) {
		return ratingsToBeRemoved != null && ratingsToBeRemoved.length > 0;
	}

	
	public static void filterOutInvalidValues(List<String> columnValues) {
		for (String columnValue : columnValues)
		{
			if(isInvalidValue(columnValue))
			{
				columnValues.remove(columnValue);
			}
		}
	}

}
