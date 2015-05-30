package com.finanalyzer.helloworld;

import java.util.ArrayList;

public class Employee {

	private final int id;
	
	public Employee(int id) {
		this.id=id;
	}

	public int getId() {
		return this.id;
	}
	
	public String toString()
	{
		return String.valueOf(this.id);
	}
}
