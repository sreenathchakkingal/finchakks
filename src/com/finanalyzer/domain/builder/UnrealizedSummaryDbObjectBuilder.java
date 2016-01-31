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
	
	public UnrealizedSummaryDbObject build()
	{
	    return this.unrealizedSummaryDbObject;
	}

}
