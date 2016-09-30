package com.finanalyzer.domain.builder;

import com.finanalyzer.domain.jdo.StopLossDbObject;
import com.finanalyzer.util.DateUtil;

public class StopLossDbObjectBuilder {
	
	private StopLossDbObject dbObject = new StopLossDbObject();

	public StopLossDbObjectBuilder stockName(String stockName) {
		this.dbObject.setStockName(stockName);
		return this;
	}
	
	public StopLossDbObjectBuilder lowerReturnPercentTarget(float lowerReturnPercentTarget) {
		this.dbObject.setLowerReturnPercentTarget(lowerReturnPercentTarget);
		return this;
	}
	
	public StopLossDbObjectBuilder upperReturnPercentTarget(float upperReturnPercentTarget) {
		this.dbObject.setUpperReturnPercentTarget(upperReturnPercentTarget);
		return this;
	}
	
	public StopLossDbObjectBuilder lowerSellPriceTarget(float lowerSellPriceTarget) {
		this.dbObject.setLowerSellPriceTarget(lowerSellPriceTarget);
		return this;
	}
	
	public StopLossDbObjectBuilder upperSellPriceTarget(float upperSellPriceTarget) {
		this.dbObject.setUpperSellPriceTarget(upperSellPriceTarget); 
		return this;
	}
	
	public StopLossDbObjectBuilder achieveAfterDate(String achieveAfterDate) {
		this.dbObject.setAchieveAfterDate(achieveAfterDate);
		return this;
	}
	
	public StopLossDbObjectBuilder achieveByDate(String achieveByDate) {
		this.dbObject.setAchieveByDate(achieveByDate);
		return this;
	}
	
	public StopLossDbObject build()
	{
		this.dbObject.setBusinessDate(DateUtil.todaysDate());
		return this.dbObject;
	}
	
	
}
