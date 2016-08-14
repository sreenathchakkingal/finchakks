package com.finanalyzer.domain.builder;

import com.finanalyzer.domain.jdo.UnrealizedDetailDbObject;

public class UnrealizedDetailDbObjectBuilder {
	
	private UnrealizedDetailDbObject unrealizedDetailDbObject = new UnrealizedDetailDbObject();

	public UnrealizedDetailDbObjectBuilder stockName(String stockName) {
		this.unrealizedDetailDbObject.setStockName(stockName);
		return this;
	}
	
	public UnrealizedDetailDbObjectBuilder returnTillDate(float returnTillDate) {
		this.unrealizedDetailDbObject.setReturnTillDate(returnTillDate);
		return this;
	}
	
	public UnrealizedDetailDbObjectBuilder buyDate(String buyDate) {
		this.unrealizedDetailDbObject.setBuyDate(buyDate);
		return this;
	}
	
	public UnrealizedDetailDbObjectBuilder buyPrice(float buyPrice) {
		this.unrealizedDetailDbObject.setBuyPrice(buyPrice);
		return this;
	}
	
	
	public UnrealizedDetailDbObjectBuilder duration(String duration) {
		this.unrealizedDetailDbObject.setDuration(duration);
		return this;
	}
	
	public UnrealizedDetailDbObjectBuilder sellPrice(float sellPrice) {
		this.unrealizedDetailDbObject.setSellPrice(sellPrice);
		return this;
	}
	
	public UnrealizedDetailDbObjectBuilder bankSellPrice(float bankSellPrice) {
		this.unrealizedDetailDbObject.setBankSellPrice(bankSellPrice);
		return this;
	}
	
	public UnrealizedDetailDbObjectBuilder quantity(float quantity) {
		this.unrealizedDetailDbObject.setQuantity(quantity);
		return this;
	}
	
	public UnrealizedDetailDbObjectBuilder sellableQuantity(float sellableQuantity) {
		this.unrealizedDetailDbObject.setSellableQuantity(sellableQuantity);
		return this;
	}
	
	public UnrealizedDetailDbObjectBuilder isMoreThanAnYear(boolean isMoreThanAnYear) {
		this.unrealizedDetailDbObject.setMoreThanAnYear(isMoreThanAnYear);
		return this;
	}
	
	public UnrealizedDetailDbObjectBuilder isMaturityIsCloseToAnYear(boolean isMaturityIsCloseToAnYear) {
		this.unrealizedDetailDbObject.setMaturityIsCloseToAnYear(isMaturityIsCloseToAnYear);
		return this;
	}
	
	public UnrealizedDetailDbObjectBuilder totalInvestment(float totalInvestment) {
		this.unrealizedDetailDbObject.setTotalInvestment(totalInvestment);
		return this;
	}

	public UnrealizedDetailDbObjectBuilder totalReturn(float totalReturn) {
		this.unrealizedDetailDbObject.setTotalReturn(totalReturn);
		return this;
	}
	
	public UnrealizedDetailDbObjectBuilder totalReturnIfBank(float totalReturnIfBank) {
		this.unrealizedDetailDbObject.setTotalReturnIfBank(totalReturnIfBank);
		return this;
	}
	
	public UnrealizedDetailDbObjectBuilder diff(float diff) {
		this.unrealizedDetailDbObject.setDiff(diff);
		return this;
	}

//	public UnrealizedDetailDbObjectBuilder lowerReturnPercentTarget(float lowerReturnPercentTarget) {
//		this.unrealizedDetailDbObject.setLowerReturnPercentTarget(lowerReturnPercentTarget);
//		return this;
//	}
//	
//	public UnrealizedDetailDbObjectBuilder upperReturnPercentTarget(float upperReturnPercentTarget) {
//		this.unrealizedDetailDbObject.setUpperReturnPercentTarget(upperReturnPercentTarget);
//		return this;
//	}
//	
//	public UnrealizedDetailDbObjectBuilder lowerSellPriceTarget(float lowerSellPriceTarget) {
//		this.unrealizedDetailDbObject.setLowerSellPriceTarget(lowerSellPriceTarget);
//		return this;
//	}
//	
//	public UnrealizedDetailDbObjectBuilder upperSellPriceTarget(float upperSellPriceTarget) {
//		this.unrealizedDetailDbObject.setUpperSellPriceTarget(upperSellPriceTarget); 
//		return this;
//	}
//	
//	public UnrealizedDetailDbObjectBuilder achieveAfterDate(String achieveAfterDate) {
//		this.unrealizedDetailDbObject.setAchieveAfterDate(achieveAfterDate);
//		return this;
//	}
//	
//	public UnrealizedDetailDbObjectBuilder achieveByDate(String achieveByDate) {
//		this.unrealizedDetailDbObject.setAchieveByDate(achieveByDate);
//		return this;
//	}
//	public UnrealizedDetailDbObjectBuilder isTargetReached(boolean isTargetReached) {
//		this.unrealizedDetailDbObject.setTargetReached(isTargetReached);
//		return this;
//	}
	
	public UnrealizedDetailDbObject build()
	{
	    return this.unrealizedDetailDbObject;
	}
	
	
}
