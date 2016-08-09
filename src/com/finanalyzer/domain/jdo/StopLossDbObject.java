package com.finanalyzer.domain.jdo;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;


@PersistenceCapable
public class StopLossDbObject {

	@Persistent
	private String stockName;
	
	@Persistent
	private float targetReturnPercent;
	
	@Persistent
	private String targetSellPrice;
	
	@Persistent
	private Date targetDate;

	public StopLossDbObject()
	{
		
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public float getTargetReturnPercent() {
		return targetReturnPercent;
	}

	public void setTargetReturnPercent(float targetReturnPercent) {
		this.targetReturnPercent = targetReturnPercent;
	}

	public String getTargetSellPrice() {
		return targetSellPrice;
	}

	public void setTargetSellPrice(String targetSellPrice) {
		this.targetSellPrice = targetSellPrice;
	}

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	@Override
	public String toString()
	{
		return this.stockName+
				" Target %: "+this.getTargetReturnPercent()+
				" Target Sell Price: "+this.getTargetSellPrice()+
				" Target Date: "+this.getTargetDate();
	}
	
}

