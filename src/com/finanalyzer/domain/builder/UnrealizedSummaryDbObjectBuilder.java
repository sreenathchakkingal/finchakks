package com.finanalyzer.domain.builder;

import com.finanalyzer.domain.jdo.UnrealizedSummaryDbObject;

public class UnrealizedSummaryDbObjectBuilder {
	
	private UnrealizedSummaryDbObject  unrealizedSummaryDbObject = new UnrealizedSummaryDbObject();

	public UnrealizedSummaryDbObjectBuilder stockName(String stockName) {
		this.unrealizedSummaryDbObject.setStockName(stockName);
		return this;
	}
	
	public UnrealizedSummaryDbObjectBuilder returnTillDate(float returnTillDate) {
		this.unrealizedSummaryDbObject.setReturnTillDate(returnTillDate);
		return this;
	}
	
	public UnrealizedSummaryDbObjectBuilder quantity(float quantity) {
		this.unrealizedSummaryDbObject.setQuantity(quantity);
		return this;
	}
	
	public UnrealizedSummaryDbObjectBuilder sellableQuantity(float sellableQuantity) {
		this.unrealizedSummaryDbObject.setSellableQuantity(sellableQuantity);
		return this;
	}
	
	public UnrealizedSummaryDbObjectBuilder totalInvestment(float totalInvestment) {
		this.unrealizedSummaryDbObject.setTotalInvestment(totalInvestment);
		return this;
	}
	
	public UnrealizedSummaryDbObjectBuilder totalReturn(float totalReturn) {
		this.unrealizedSummaryDbObject.setTotalReturn(totalReturn);
		return this;
	}

	public UnrealizedSummaryDbObjectBuilder totalReturnIfBank(float totalReturnIfBank) {
		this.unrealizedSummaryDbObject.setTotalReturnIfBank(totalReturnIfBank);
		return this;
	}
	
	public UnrealizedSummaryDbObjectBuilder industry(String industry) {
		this.unrealizedSummaryDbObject.setIndustry(industry);
		return this;
	}
	
	public UnrealizedSummaryDbObjectBuilder isBlackListed(boolean isBlackListed) {
		this.unrealizedSummaryDbObject.setBlackListed(isBlackListed);
		return this;
	}
	
	public UnrealizedSummaryDbObjectBuilder impactOnAverageReturn(float impactOnAverageReturn) {
		this.unrealizedSummaryDbObject.setImpactOnAverageReturn(impactOnAverageReturn);
		return this;
	}
	
	public UnrealizedSummaryDbObjectBuilder diff(float diff) {
		this.unrealizedSummaryDbObject.setDiff(diff);
		return this;
	}
	
	public UnrealizedSummaryDbObjectBuilder sellPrice(float sellPrice) {
		this.unrealizedSummaryDbObject.setSellPrice(sellPrice);
		return this;
	}
	
	public UnrealizedSummaryDbObjectBuilder lowerReturnPercentTarget(float lowerReturnPercentTarget) {
		this.unrealizedSummaryDbObject.setLowerReturnPercentTarget(lowerReturnPercentTarget);
		return this;
	}
	
	public UnrealizedSummaryDbObjectBuilder upperReturnPercentTarget(float upperReturnPercentTarget) {
		this.unrealizedSummaryDbObject.setUpperReturnPercentTarget(upperReturnPercentTarget);
		return this;
	}
	
	public UnrealizedSummaryDbObjectBuilder lowerSellPriceTarget(float lowerSellPriceTarget) {
		this.unrealizedSummaryDbObject.setLowerSellPriceTarget(lowerSellPriceTarget);
		return this;
	}
	
	public UnrealizedSummaryDbObjectBuilder upperSellPriceTarget(float upperSellPriceTarget) {
		this.unrealizedSummaryDbObject.setUpperSellPriceTarget(upperSellPriceTarget); 
		return this;
	}
	
	public UnrealizedSummaryDbObjectBuilder achieveAfterDate(String achieveAfterDate) {
		this.unrealizedSummaryDbObject.setAchieveAfterDate(achieveAfterDate);
		return this;
	}
	
	public UnrealizedSummaryDbObjectBuilder achieveByDate(String achieveByDate) {
		this.unrealizedSummaryDbObject.setAchieveByDate(achieveByDate);
		return this;
	}
	public UnrealizedSummaryDbObjectBuilder isTargetReached(boolean isTargetReached) {
		this.unrealizedSummaryDbObject.setTargetReached(isTargetReached);
		return this;
	}
	
	public UnrealizedSummaryDbObject build()
	{
	    return this.unrealizedSummaryDbObject;
	}

}
