package com.finanalyzer.helloworld;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.domain.builder.StopLossDbObjectBuilder;
import com.finanalyzer.domain.jdo.StopLossDbObject;
import com.finanalyzer.util.DateUtil;

public class Tester {
	private static Logger LOG =  Logger.getLogger(Tester.class.getName());

	public static void main(String[] args) throws Exception{
		StopLossDbObjectBuilder builder = new StopLossDbObjectBuilder().stockName("dd");
		if(true)
		{
			builder.lowerReturnPercentTarget(0.1f);
		}
		if(false)
		{
			builder.upperReturnPercentTarget(0.2f);
		}
		final StopLossDbObject object = builder.build();
		System.out.println("lower "+object.getLowerReturnPercentTarget());
		System.out.println("upper "+object.getUpperReturnPercentTarget());
		
	}

}
