package com.finanalyzer.domain.builder;

import com.finanalyzer.domain.jdo.UnrealizedSummaryDiffDbObject;

public class UnrealizedSummaryDiffDbObjectBuilder {
	
	private UnrealizedSummaryDiffDbObject  unrealizedSummaryDiffDbObject = new UnrealizedSummaryDiffDbObject();

	public UnrealizedSummaryDiffDbObjectBuilder stockName(String stockName) {
		this.unrealizedSummaryDiffDbObject.setStockName(stockName);
		return this;
	}
	
	public UnrealizedSummaryDiffDbObjectBuilder prevReturnTillDate(float prevReturnTillDate) {
		this.unrealizedSummaryDiffDbObject.setPrevReturnTillDate(prevReturnTillDate);
		return this;
	}
	
	public UnrealizedSummaryDiffDbObjectBuilder currReturnTillDate(float currentReturnTillDate) {
		this.unrealizedSummaryDiffDbObject.setCurrentReturnTillDate(currentReturnTillDate);
		return this;
	}
	
	public UnrealizedSummaryDiffDbObjectBuilder diffReturnTillDate(float diffReturnTillDate) {
		this.unrealizedSummaryDiffDbObject.setDiffReturnTillDate(diffReturnTillDate);
		return this;
	}
	
	public UnrealizedSummaryDiffDbObjectBuilder absoluteDiffReturnTillDate(float absoluteDiffReturnTillDate) {
		this.unrealizedSummaryDiffDbObject.setAbsoluteDiffReturnTillDate(absoluteDiffReturnTillDate);
		return this;
	}
	
	public UnrealizedSummaryDiffDbObject build()
	{
	    return this.unrealizedSummaryDiffDbObject;
	}

}
