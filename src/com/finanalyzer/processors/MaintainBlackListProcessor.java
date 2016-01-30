package com.finanalyzer.processors;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.db.jdo.PMF;
import com.finanalyzer.domain.jdo.AllScripsDbObject;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.impl.list.mutable.FastList;

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
