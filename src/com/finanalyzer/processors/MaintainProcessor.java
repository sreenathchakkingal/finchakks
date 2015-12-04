package com.finanalyzer.processors;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.db.jdo.PMF;
import com.finanalyzer.domain.jdo.AllScripsDbObject;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.impl.list.mutable.FastList;

public abstract class MaintainProcessor implements Processor<List<String>>
{

	public final  boolean isAddRequest;
	public final boolean isWriteRequest;
	public final List<String> stocksIds;
	public final Predicate predicate;
	
	public MaintainProcessor(List<String> stockIds, boolean isWriteRequest, boolean isAddRequest, Predicate predicate) 
	{
		this.stocksIds=stockIds;
		this.isWriteRequest = isWriteRequest;
		this.isAddRequest = isAddRequest;
		this.predicate = predicate;
	}

	@Override
	public List<String> execute()
	{
		if(isWriteRequest)
		{
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try
			{
				Query q = pm.newQuery(AllScripsDbObject.class, ":p.contains("+AllScripsDbObject.NSE_ID+")");
				final List<AllScripsDbObject> matchingEntries = (List<AllScripsDbObject>)q.execute(this.stocksIds);
				updateEntry(matchingEntries);	
			}
			
			finally
			{
				pm.close();				
			}
		}
		
		return retreiveAllMatchingRecords();
	}

	private List<String> retreiveAllMatchingRecords() {
		JdoDbOperations<AllScripsDbObject> dbOperations = new JdoDbOperations<AllScripsDbObject>(AllScripsDbObject.class);
		final FastList<AllScripsDbObject> entries = FastList.newList(dbOperations.getEntries(AllScripsDbObject.NSE_ID));
		return entries.collectIf(this.predicate, AllScripsDbObject.STOCK_NAME_COLLECTOR);
	}

	public abstract void  updateEntry(List<AllScripsDbObject> matchingEntries);
	
}
