package com.finanalyzer.domain;

import java.util.List;

public class ModifiableStockAttributes {
	
	private String stockName;
	private boolean isWatchListed;
	private List<RatingObjectForUi> ratings;
	
	
	public ModifiableStockAttributes(String stockName, boolean isWatchListed,
			List<RatingObjectForUi> ratings) {
		this.stockName = stockName;
		this.isWatchListed = isWatchListed;
		this.ratings= ratings;
	}


	public String getStockName() {
		return stockName;
	}


	public void setStockName(String stockName) {
		this.stockName = stockName;
	}


	public boolean isWatchListed() {
		return isWatchListed;
	}


	public void setWatchListed(boolean isWatchListed) {
		this.isWatchListed = isWatchListed;
	}


	public List<RatingObjectForUi> getRatings() {
		return ratings;
	}


	public void setRatings(List<RatingObjectForUi> ratings) {
		this.ratings = ratings;
	}

}