package com.finanalyzer.domain;

import java.util.List;

import com.finanalyzer.domain.jdo.UnrealizedDetailDbObject;
import com.finanalyzer.domain.jdo.UnrealizedSummaryDbObject;


public class UnrealizedWrapper {

	private UnrealizedSummaryDbObject summaryDbObject;
	private List<UnrealizedDetailDbObject> detailDbObjects;
	
	public UnrealizedWrapper(UnrealizedSummaryDbObject summaryDbObject, List<UnrealizedDetailDbObject> detailDbObjects) 
	{
		this.summaryDbObject = summaryDbObject;
		this.detailDbObjects = detailDbObjects;
	}

	public UnrealizedSummaryDbObject getSummaryDbObject() {
		return summaryDbObject;
	}

	public void setSummaryDbObject(UnrealizedSummaryDbObject summaryDbObject) {
		this.summaryDbObject = summaryDbObject;
	}

	public List<UnrealizedDetailDbObject> getDetailDbObjects() {
		return detailDbObjects;
	}

	public void setDetailDbObjects(List<UnrealizedDetailDbObject> detailDbObjects) {
		this.detailDbObjects = detailDbObjects;
	}

}
