package com.finanalyzer.helloworld;


public class Employee {
	private boolean isGood;
	private String name;
	
	public Employee(boolean isGood, String name) {
		this.isGood = isGood;
		this.name = name;
	}
	public boolean isGood() {
		return isGood;
	}
	public void setGood(boolean isGood) {
		this.isGood = isGood;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
