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
	private float lowerReturnPercentTarget;

	@Persistent
	private float upperReturnPercentTarget;

	@Persistent
	private float lowerSellPriceTarget;
	
	@Persistent
	private float upperSellPriceTarget;

	@Persistent
	private String achieveAfterDate;
	
	@Persistent
	private String achieveByDate;

	public StopLossDbObject(){}
	
	/*
	public StopLossDbObject(String stockName, float lowerReturnPercentTarget,
			float upperReturnPercentTarget, float lowerSellPriceTarget,
			float upperSellPriceTarget, String achieveAfterDate,
			String achieveByDate) 
	{
		this.stockName = stockName;
		this.lowerReturnPercentTarget = lowerReturnPercentTarget;
		this.upperReturnPercentTarget = upperReturnPercentTarget;
		this.lowerSellPriceTarget = lowerSellPriceTarget;
		this.upperSellPriceTarget = upperSellPriceTarget;
		this.achieveAfterDate = achieveAfterDate;
		this.achieveByDate = achieveByDate;
	}
*/
	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public float getLowerReturnPercentTarget() {
		return lowerReturnPercentTarget;
	}

	public void setLowerReturnPercentTarget(float lowerReturnPercentTarget) {
		//even if by mistake we set the value to 0.0 - it will be set as 0.01f. 
		//In the UnRealizedPnLProcessor the empty value check is by comparing with 0.0f. 
		
		this.lowerReturnPercentTarget =  lowerReturnPercentTarget==0.0f ? 0.1f : lowerReturnPercentTarget;
	}

	public float getUpperReturnPercentTarget() {
		return upperReturnPercentTarget;
	}

	public void setUpperReturnPercentTarget(float upperReturnPercentTarget) {
		//even if by mistake we set the value to 0.0 - it will be set as 0.01f. 
		//In the UnRealizedPnLProcessor the empty value check is by comparing with 0.0f.  

		this.upperReturnPercentTarget =  upperReturnPercentTarget==0.0f ? 0.1f : upperReturnPercentTarget;
		
	}

	public float getLowerSellPriceTarget() {
		return lowerSellPriceTarget;
	}

	public void setLowerSellPriceTarget(float lowerSellPriceTarget) {
		this.lowerSellPriceTarget = lowerSellPriceTarget;
	}

	public float getUpperSellPriceTarget() {
		return upperSellPriceTarget;
	}

	public void setUpperSellPriceTarget(float upperSellPriceTarget) {
		this.upperSellPriceTarget = upperSellPriceTarget;
	}
	
	public String getAchieveAfterDate() {
		return achieveAfterDate;
	}

	public void setAchieveAfterDate(String achieveAfterDate) {
		this.achieveAfterDate = achieveAfterDate;
	}

	public String getAchieveByDate() {
		return achieveByDate;
	}

	public void setAchieveByDate(String achieveByDate) {
		this.achieveByDate = achieveByDate;
	}

	@Override
	public String toString()
	{
		return this.stockName;
	}
	
}

