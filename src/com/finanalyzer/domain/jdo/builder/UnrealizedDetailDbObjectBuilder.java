package com.finanalyzer.domain.jdo.builder;

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
	
	
	public UnrealizedDetailDbObjectBuilder sellDate(String sellDate) {
		this.unrealizedDetailDbObject.setSellDate(sellDate);
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
	
	
	public UnrealizedDetailDbObject build()
	{
	    return this.unrealizedDetailDbObject;
	}
	
	
}
