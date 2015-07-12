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

public class MaintainWatchListProcessor implements Processor<List<String>>
{

	private boolean isAddRequest;
	private boolean isWriteRequest;
	private final List<String> stocksIds;
	
	private static final Function<AllScripsDbObject, String> STOCK_NAME_COLLECTOR = new Function<AllScripsDbObject, String>() {

		@Override
		public String valueOf(AllScripsDbObject allScripsDbObject) {
			return allScripsDbObject.getNseId();
		}
	};
	
	public static final Predicate<AllScripsDbObject> IS_WATCHLISTED = new Predicate<AllScripsDbObject>() {

		@Override
		public boolean accept(AllScripsDbObject allScripsDbObject) {
			return allScripsDbObject.isWatchListed();
		}
	};
	
	public MaintainWatchListProcessor(List<String> stockIds, boolean isWriteRequest, boolean isAddRequest) 
	{
		this.stocksIds=stockIds;
		this.isWriteRequest = isWriteRequest;
		this.isAddRequest = isAddRequest;
	}

	@Override
	public List<String> execute()
	{
		if(isWriteRequest)
		{
			updateEntry();	
		}
		
		JdoDbOperations<AllScripsDbObject> dbOperations = new JdoDbOperations<AllScripsDbObject>(AllScripsDbObject.class);
		final FastList<AllScripsDbObject> entries = FastList.newList(dbOperations.getEntries(AllScripsDbObject.NSE_ID));
		return entries.collectIf(IS_WATCHLISTED, STOCK_NAME_COLLECTOR);
	}

	private void updateEntry() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try
		{
			Query q = pm.newQuery(AllScripsDbObject.class, ":p.contains("+AllScripsDbObject.NSE_ID+")");
			final List<AllScripsDbObject> matchingEntries = (List<AllScripsDbObject>)q.execute(this.stocksIds);
			for (AllScripsDbObject allScripsDbObject : matchingEntries)
			{
				if(this.isAddRequest)
				{
					allScripsDbObject.setWatchListed(true);	
				}
				else
				{
					allScripsDbObject.setWatchListed(false);
				}
			}
		}
		finally
		{
			pm.close();				
		}
	}
	
//	@Override
//	public List<String> execute()
//	{
//		WatchListUtil watchListUtil = new WatchListUtil();
//		watchListUtil.removeFromWatchList(this.stocksToBeRemoved);
//		watchListUtil.addToWatchList(this.stockId);
//		return watchListUtil.retrieveWatchList();
//
//	}

	// public static void doItOnce(InputStream stocksAsInputStream)
	// {
	// List<String> watchListFromFile =
	// ReaderUtil.converInputReaderToList(stocksAsInputStream);
	// WatchListUtil.addAllOneTime(watchListFromFile);
	//
	// }

}
