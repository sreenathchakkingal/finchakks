package com.finanalyzer.domain;

import java.util.List;

import com.finanalyzer.domain.jdo.NDaysHistoryFlattenedDbObject;

public class NDaysHistoryFlattenedWrapper {
	private List<NDaysHistoryFlattenedDbObject> nDaysWatchlistedStocks;
	private List<NDaysHistoryFlattenedDbObject> nDaysMinOrMaxStocks;
	
	public NDaysHistoryFlattenedWrapper(
			List<NDaysHistoryFlattenedDbObject> stocksWithoutMinimum,
			List<NDaysHistoryFlattenedDbObject> stocksMinimum) {
		this.nDaysWatchlistedStocks = stocksWithoutMinimum;
		this.nDaysMinOrMaxStocks = stocksMinimum;
	}

	public List<NDaysHistoryFlattenedDbObject> getnDaysWatchlistedStocks() {
		return nDaysWatchlistedStocks;
	}

	public void setnDaysWatchlistedStocks(
			List<NDaysHistoryFlattenedDbObject> nDaysWatchlistedStocks) {
		this.nDaysWatchlistedStocks = nDaysWatchlistedStocks;
	}

	public List<NDaysHistoryFlattenedDbObject> getnDaysMinOrMaxStocks() {
		return nDaysMinOrMaxStocks;
	}

	public void setnDaysMinOrMaxStocks(
			List<NDaysHistoryFlattenedDbObject> nDaysMinOrMaxStocks) {
		this.nDaysMinOrMaxStocks = nDaysMinOrMaxStocks;
	}
	
	
}
