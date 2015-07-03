package com.finanalyzer.helloworld;

import java.util.Comparator;
import java.util.List;

import javax.persistence.Tuple;

import com.finanalyzer.domain.StockRatingValuesEnum;
import com.finanalyzer.domain.jdo.AllScripsDbObject;
import com.finanalyzer.util.CalculatorUtil;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.multimap.list.FastListMultimap;

public class Tester {

	
	public static void main(String[] args) {
		List<Employee> newList = FastList.newListWith(new Employee(0), new Employee(1), new Employee(2), new Employee(3));
		
		for (int i=0; i<newList.size();i++)
		{
			if(newList.get(i).getId()==2)
			{
				newList.remove(i);
			}
		}
		System.out.println(newList);
		
	}
	

}
