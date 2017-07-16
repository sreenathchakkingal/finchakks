package com.finanalyzer.processors;

import java.util.List;

import com.finanalyzer.domain.jdo.AllScripsDbObject;

public class MaintainBlackListProcessor extends MaintainProcessor
{

	public MaintainBlackListProcessor(List<String> stockIds, boolean isWriteRequest, boolean isAddRequest) 
	{
		super(stockIds, isWriteRequest, isAddRequest, "isBlackListed", "true");
	}

	@Override	
	public void updateEntry(List<AllScripsDbObject> matchingEntries)
	{
		for (AllScripsDbObject allScripsDbObject : matchingEntries)
		{
			allScripsDbObject.setBlackListed(this.isAddRequest);	
		}
	}

	@Override
	public String getViewName() {
		return "maintainBlackList";
	}
}
