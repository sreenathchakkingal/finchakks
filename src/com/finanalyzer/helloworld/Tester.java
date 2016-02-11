package com.finanalyzer.helloworld;

import java.util.logging.Logger;

import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.util.DateUtil;

public class Tester {
	private static Logger LOG =  Logger.getLogger(Tester.class.getName());

	public static void main(String[] args) throws Exception{
		System.out.println(DateUtil.getCurrentDay());
	}

}
