package com.finanalyzer.domain.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class UnrealizedDbObject {
	
	@Persistent
	private String moneycontrolName;
	
	@Persistent
	private String buyDate;
	
	@Persistent
	private String buyPrice;
	
	@Persistent
	private String buyQuantity;

	public UnrealizedDbObject(String moneycontrolName, String buyDate, double buyPrice, int buyQuantity) {
		this.moneycontrolName = moneycontrolName;
		this.buyDate = buyDate;
		this.buyPrice = String.valueOf(buyPrice);
		this.buyQuantity = String.valueOf(buyQuantity);
	}

	public String getMoneycontrolName() {
		return moneycontrolName;
	}

	public String getBuyDate() {
		return buyDate;
	}

	public double getBuyPrice() {
		return Double.valueOf(buyPrice);
	}

	public long getBuyQuantity() {
		return Long.valueOf(buyQuantity);
	}
	
	@Override
	public String toString()
	{
		return this.getMoneycontrolName();
	}

}

