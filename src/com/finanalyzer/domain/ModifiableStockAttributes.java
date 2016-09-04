package com.finanalyzer.domain;

import java.util.List;

public class ModifiableStockAttributes {
	
	private String stockName;
	private String moneycontrolName;
	private boolean isWatchListed;
	float lowerReturnPercentTarget;
	float upperReturnPercentTarget;
	private List<RatingObjectForUi> ratings;
	
	
	public ModifiableStockAttributes(String stockName,String moneycontrolName, boolean isWatchListed,
			float lowerReturnPercentTarget, float upperReturnPercentTarget,
			List<RatingObjectForUi> ratings) {
		this.stockName = stockName;
		this.moneycontrolName=moneycontrolName;
		this.isWatchListed = isWatchListed;
		this.lowerReturnPercentTarget=lowerReturnPercentTarget;
		this.upperReturnPercentTarget=upperReturnPercentTarget;
		this.ratings= ratings;
	}


	public String getStockName() {
		return stockName;
	}


	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public String getMoneycontrolName() {
		return moneycontrolName;
	}

	public void setMoneycontrolName(String moneycontrolName) {
		this.moneycontrolName = moneycontrolName;
	}

	public boolean isWatchListed() {
		return isWatchListed;
	}


	public void setWatchListed(boolean isWatchListed) {
		this.isWatchListed = isWatchListed;
	}

	public float getLowerReturnPercentTarget() {
		return lowerReturnPercentTarget;
	}


	public void setLowerReturnPercentTarget(float lowerReturnPercentTarget) {
		this.lowerReturnPercentTarget = lowerReturnPercentTarget;
	}


	public float getUpperReturnPercentTarget() {
		return upperReturnPercentTarget;
	}


	public void setUpperReturnPercentTarget(float upperReturnPercentTarget) {
		this.upperReturnPercentTarget = upperReturnPercentTarget;
	}


	public List<RatingObjectForUi> getRatings() {
		return ratings;
	}


	public void setRatings(List<RatingObjectForUi> ratings) {
		this.ratings = ratings;
	}

}