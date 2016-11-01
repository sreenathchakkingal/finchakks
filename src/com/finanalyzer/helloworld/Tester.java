package com.finanalyzer.helloworld;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.domain.builder.StopLossDbObjectBuilder;
import com.finanalyzer.domain.jdo.StopLossDbObject;
import com.finanalyzer.util.CalculatorUtil;
import com.finanalyzer.util.DateUtil;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.utility.ListIterate;


public class Tester {
	public static void main(String[] args) throws Exception
	{
		System.out.println(CalculatorUtil.calculateInitialAmount(7209.57f, 10.0f, 20*365));
		
	}
}

