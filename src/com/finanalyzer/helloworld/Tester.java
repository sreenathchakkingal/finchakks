package com.finanalyzer.helloworld;

import java.util.Comparator;

import com.finanalyzer.domain.StockRatingValuesEnum;
import com.finanalyzer.domain.jdo.AllScripsDbObject;

public class Tester {

	
	private static final Comparator<Employee> EMPLOYEE_ID_COMPARATOR = new Comparator<Employee>() {
		@Override
		public int compare(Employee o1, Employee o2) {
			return o1.getId()-o2.getId();
		}
	};
	
	public static void main(String[] args) {
		
		System.out.println(AllScripsDbObject.class.getSimpleName());

	}

}
