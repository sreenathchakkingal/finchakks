package com.finanalyzer.domain.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;


@PersistenceCapable
public class StopLossDbObject {

	public static final float DATE_DIFF_TOLERANCE = 0.0005f;
	public static final float PRICE_DIFF_TOLERANCE = 0.1f;
	public static final float RETURN_DIFF_TOLERANCE = 0.005f;
	
	@Persistent
	private String stockName;
	
	@Persistent
	private float targetReturnPercent;
	
	@Persistent
	private float targetSellPrice;
	
	@Persistent
	private String targetDate;

	public StopLossDbObject(String stockName, float targetReturnPercent,
			float targetSellPrice, String targetDate) {
		this.stockName = stockName;
		this.targetReturnPercent = targetReturnPercent;
		this.targetSellPrice = targetSellPrice;
		this.targetDate = targetDate;
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

	public float getTargetSellPrice() {
		return targetSellPrice;
	}

	public void setTargetSellPrice(float targetSellPrice) {
		this.targetSellPrice = targetSellPrice;
	}

	public String getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(String targetDate) {
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

