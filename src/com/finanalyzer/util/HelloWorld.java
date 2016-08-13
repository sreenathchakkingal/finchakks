package com.finanalyzer.util;

import com.finanalyzer.domain.builder.StopLossDbObjectBuilder;
import com.finanalyzer.domain.jdo.StopLossDbObject;

public class HelloWorld
{
	public static void main(String[] args)
	{
		System.out.println("hwllo");
		StopLossDbObjectBuilder builder = new StopLossDbObjectBuilder();
		final StopLossDbObject stock1 = builder.stockName("DUMMY1").achieveAfterDate("DATE1").build();
		final StopLossDbObject stock2 = builder.stockName("DUMMY1").achieveByDate("DATE2").build();
		System.out.println(stock2.getAchieveAfterDate());
	}
}
