package com.finanalyzer.domain;

import java.util.List;

import com.finanalyzer.domain.jdo.NDaysHistoryFlattenedDbObject;

public class NDaysHistoryFlattenedWrapper {
	private List<NDaysHistoryFlattenedDbObject> stocksWithoutMinimum;
	private List<NDaysHistoryFlattenedDbObject> stocksMinimum;
	
	public NDaysHistoryFlattenedWrapper(
			List<NDaysHistoryFlattenedDbObject> stocksWithoutMinimum,
			List<NDaysHistoryFlattenedDbObject> stocksMinimum) {
		this.stocksWithoutMinimum = stocksWithoutMinimum;
		this.stocksMinimum = stocksMinimum;
	}
	
	public List<NDaysHistoryFlattenedDbObject> getStocksWithoutMinimum() {
		return stocksWithoutMinimum;
	}
	
	public void setStocksWithoutMinimum(
			List<NDaysHistoryFlattenedDbObject> stocksWithoutMinimum) {
		this.stocksWithoutMinimum = stocksWithoutMinimum;
	}
	
	public List<NDaysHistoryFlattenedDbObject> getStocksMinimum() {
		return stocksMinimum;
	}
	
	public void setStocksMinimum(List<NDaysHistoryFlattenedDbObject> stocksMinimum) {
		this.stocksMinimum = stocksMinimum;
	}
	
}
