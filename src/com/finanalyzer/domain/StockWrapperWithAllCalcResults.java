package com.finanalyzer.domain;

import java.util.List;

import com.finanalyzer.domain.jdo.AllScripsDbObject;
import com.finanalyzer.domain.jdo.NDaysHistoryDbObject;
import com.finanalyzer.domain.jdo.UnrealizedDetailDbObject;
import com.finanalyzer.domain.jdo.UnrealizedSummaryDbObject;

public class StockWrapperWithAllCalcResults {
	private AllScripsDbObject allScripsDbObject;
	private List<UnrealizedSummaryDbObject> unrealizedSummaryDbObjects;
	private List<UnrealizedDetailDbObject> unrealizedDetailDbObjects;
	private List<NDaysHistoryDbObject> nDaysHistoryDbObjects;
	
	public AllScripsDbObject getAllScripsDbObject() {
		return allScripsDbObject;
	}
	public void setAllScripsDbObject(AllScripsDbObject allScripsDbObject) {
		this.allScripsDbObject = allScripsDbObject;
	}
	public List<UnrealizedSummaryDbObject> getUnrealizedSummaryDbObjects() {
		return unrealizedSummaryDbObjects;
	}
	public void setUnrealizedSummaryDbObjects(
			List<UnrealizedSummaryDbObject> unrealizedSummaryDbObjects) {
		this.unrealizedSummaryDbObjects = unrealizedSummaryDbObjects;
	}
	public List<UnrealizedDetailDbObject> getUnrealizedDetailDbObjects() {
		return unrealizedDetailDbObjects;
	}
	public void setUnrealizedDetailDbObjects(
			List<UnrealizedDetailDbObject> unrealizedDetailDbObjects) {
		this.unrealizedDetailDbObjects = unrealizedDetailDbObjects;
	}
	public List<NDaysHistoryDbObject> getnDaysHistoryDbObjects() {
		return nDaysHistoryDbObjects;
	}
	public void setnDaysHistoryDbObjects(
			List<NDaysHistoryDbObject> nDaysHistoryDbObjects) {
		this.nDaysHistoryDbObjects = nDaysHistoryDbObjects;
	}
}
