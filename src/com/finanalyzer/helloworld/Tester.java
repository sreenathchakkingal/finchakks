package com.finanalyzer.helloworld;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.util.DateUtil;

public class Tester {
	private static Logger LOG =  Logger.getLogger(Tester.class.getName());

	public static void main(String[] args) throws Exception{
		List<String> someList = new ArrayList();
		someList.add("one");
		someList.add("two");
		LOG.info(""+someList);
		System.out.println("sout "+someList);	
	}

}
