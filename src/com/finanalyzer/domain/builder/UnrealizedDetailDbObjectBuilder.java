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

	public UnrealizedDetailDbObjectBuilder targetReturnPercent(float targetReturnPercent) {
		this.unrealizedDetailDbObject.setTargetReturnPercent(targetReturnPercent);
		return this;
	}
	
	public UnrealizedDetailDbObjectBuilder targetSellPrice(float targetSellPrice) {
		this.unrealizedDetailDbObject.setTargetSellPrice(targetSellPrice);
		return this;
	}

	public UnrealizedDetailDbObjectBuilder targetDate(String targetDate) {
		this.unrealizedDetailDbObject.setTargetDate(targetDate);
		return this;
	}
	
	public UnrealizedDetailDbObjectBuilder isTargetReached(boolean isTargetReached) {
		this.unrealizedDetailDbObject.setTargetReached(isTargetReached);
		return this;
	}
	
	public UnrealizedDetailDbObject build()
	{
	    return this.unrealizedDetailDbObject;
	}
	
	
}
