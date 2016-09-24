package com.finanalyzer.helloworld;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.domain.builder.StopLossDbObjectBuilder;
import com.finanalyzer.domain.jdo.StopLossDbObject;
import com.finanalyzer.util.DateUtil;
import com.gs.collections.impl.list.mutable.FastList;

public class Tester {
	private static Logger LOG =  Logger.getLogger(Tester.class.getName());

	public static void main(String[] args) throws Exception{
		List<String> rows = FastList.newListWith("one", "two", "three", "total");
		System.out.println(rows.subList(0, rows.size()-2));
		
	}

}
