package com.finanalyzer.helloworld;

import java.util.List;

public class EmployeeWrapper {
	private final List<Employee> goodEmployees;
	private final List<Employee> badEmployees;
	
	public EmployeeWrapper(List<Employee> goodEmployees, List<Employee> badEmployees) {
		this.goodEmployees = goodEmployees;
		this.badEmployees = badEmployees;
	}

	public List<Employee> getGoodEmployees() {
		return goodEmployees;
	}

	public List<Employee> getBadEmployees() {
		return badEmployees;
	}
}
