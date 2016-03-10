package com.finanalyzer.endpoints;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.db.jdo.PMF;
import com.finanalyzer.domain.jdo.AllScripsDbObject;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.gs.collections.impl.list.mutable.FastList;

@Api(name = "allScripsControllerEndPoint", version = "v1")
public class AllScripsControllerEndPoint {
	
	@ApiMethod(name = "getScripDetails")
	public List<AllScripsDbObject> getScripDetails(@Named("nseId") String nseId)
	{
		JdoDbOperations<AllScripsDbObject> dbOperations = new JdoDbOperations<AllScripsDbObject>(AllScripsDbObject.class);
		final List<AllScripsDbObject> entries = dbOperations.getEntries(AllScripsDbObject.NSE_ID, FastList.newListWith(nseId));
		return entries;
	}
	
	@ApiMethod(name = "modifyScripDetails")
	public List<AllScripsDbObject> modifyScripDetails(AllScripsDbObject allScripsObject)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<AllScripsDbObject> matchingEntries = null;
		try
		{
			Query q = pm.newQuery(AllScripsDbObject.class, ":p.contains("+AllScripsDbObject.NSE_ID+")");
			matchingEntries = (List<AllScripsDbObject>)q.execute(FastList.newListWith(allScripsObject.getNseId()));
			final AllScripsDbObject allScripsDbObjectToBeModified = matchingEntries.get(0);
			allScripsDbObjectToBeModified.setMoneycontrolName(allScripsObject.getMoneycontrolName());
			allScripsDbObjectToBeModified.setWatchListed(allScripsObject.getIsWatchListed());
			allScripsDbObjectToBeModified.setBlackListed(allScripsObject.getIsBlackListed());
		}
		finally
		{
			pm.close();
		}
		return matchingEntries;
		
	}
}
