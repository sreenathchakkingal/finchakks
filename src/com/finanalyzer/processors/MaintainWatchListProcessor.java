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
