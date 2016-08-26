package com.finanalyzer.domain;

public class ModifiableStockAttributes {
	
	private String stockName;
	private String isWatchListed;
	private String stockRatingGraph;
	private String stockRatingDebtToEquity;
	
	
	public ModifiableStockAttributes(String stockName, String isWatchListed,
			String stockRatingGraph, String stockRatingDebtToEquity) {
		this.stockName = stockName;
		this.isWatchListed = isWatchListed;
		this.stockRatingGraph = stockRatingGraph;
		this.stockRatingDebtToEquity = stockRatingDebtToEquity;
	}
	
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	public String getIsWatchListed() {
		return isWatchListed;
	}
	public void setIsWatchListed(String isWatchListed) {
		this.isWatchListed = isWatchListed;
	}
	public String getStockRatingGraph() {
		return stockRatingGraph;
	}
	public void setStockRatingGraph(String stockRatingGraph) {
		this.stockRatingGraph = stockRatingGraph;
	}
	public String getStockRatingDebtToEquity() {
		return stockRatingDebtToEquity;
	}
	public void setStockRatingDebtToEquity(String stockRatingDebtToEquity) {
		this.stockRatingDebtToEquity = stockRatingDebtToEquity;
	}
	
	

}
