package com.finanalyzer.processors;

import java.util.List;

import com.finanalyzer.domain.jdo.AllScripsDbObject;

public class MaintainWatchListProcessor extends MaintainProcessor {

	public MaintainWatchListProcessor(List<String> stockIds, boolean isWriteRequest, boolean isAddRequest) 
	{
		super(stockIds, isWriteRequest, isAddRequest,  "isWatchListed", "true");
	}

	@Override
	public void updateEntry(List<AllScripsDbObject> matchingEntries) 
	{
		for (AllScripsDbObject allScripsDbObject : matchingEntries) 
		{
			allScripsDbObject.setWatchListed(this.isAddRequest);
		}
	}

	@Override
	public String getViewName() {
		return "maintainWatchList";
	}
}
