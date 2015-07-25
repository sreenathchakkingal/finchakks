package com.finanalyzer.processors;

import java.util.Arrays;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.finanalyzer.db.StockIdConverstionUtil;
import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.db.jdo.PMF;
import com.finanalyzer.domain.MappingStockId;
import com.finanalyzer.domain.jdo.AllScripsDbObject;
import com.finanalyzer.util.StringUtil;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.impl.list.mutable.FastList;

public class MaintainMappingProcessor implements Processor<List<AllScripsDbObject>>
{
	private final String moneyControlId;
	private final String yahooId;
	private final String nseId;
	private final String[] selectedMappings;
	private final boolean isDelete;
	private String bseId;

	public MaintainMappingProcessor(String moneyControlId, String yahooId, String nseId, String bseId, String[] selectedMappings, boolean isDelete)
	{
		this.moneyControlId=moneyControlId;
		this.yahooId=yahooId;
		this.nseId=nseId;
		this.bseId=bseId;
		this.selectedMappings = selectedMappings;
		this.isDelete=isDelete;
	}

	@Override
	public List<AllScripsDbObject> execute()
	{
		JdoDbOperations<AllScripsDbObject> dbOperations = new JdoDbOperations<AllScripsDbObject>(AllScripsDbObject.class);
		if(this.isDelete)
		{
			final List<AllScripsDbObject> entries = dbOperations.getEntries(AllScripsDbObject.NSE_ID,Arrays.asList(this.selectedMappings));
			for(AllScripsDbObject dbObject : entries)
			{
				dbObject.setMoneycontrolName(null);	
				dbObject.setYahooName(null);
			}
		}
		else
		{
			updateOrInsert();
		}
		FastList<AllScripsDbObject> allScripsDbObjects = FastList.newList(dbOperations.getEntries(AllScripsDbObject.NSE_ID));
		return allScripsDbObjects.select(AllScripsDbObject.MONEYCONTROL_NAME_EXISTS);
	}

	private void updateOrInsert() {
		if(StringUtil.isValidValue(this.moneyControlId)&& StringUtil.isValidValue(this.nseId)) //update
		{
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try
			{
				Query q = pm.newQuery(AllScripsDbObject.class, ":p.contains("+AllScripsDbObject.NSE_ID+")");
				List<AllScripsDbObject> allScripsDbObjects  = (List<AllScripsDbObject>)q.execute(this.nseId);
				if(!allScripsDbObjects.isEmpty())
				{
					allScripsDbObjects.get(0).setMoneycontrolName(this.moneyControlId);	
					allScripsDbObjects.get(0).setNseId(this.nseId);
					allScripsDbObjects.get(0).setYahooName(this.yahooId);
					allScripsDbObjects.get(0).setBseId(this.bseId);
				}
				else //insert
				{
					pm.makePersistent(new AllScripsDbObject(this.nseId,this.bseId, this.moneyControlId, this.yahooId));
				}
			}
			finally
			{
				pm.close();
			}
		}
	}
	
//	@Override chakks
//	public List<MappingStockId> execute()
//	{
//		StockIdConverstionUtil stockIdConverstionUtil = new StockIdConverstionUtil();
//		if(this.isDelete)
//		{
//			stockIdConverstionUtil.deleteRecords(this.selectedMappings);
//		}
//		else
//		{
//			stockIdConverstionUtil.insertConversion(this.moneyControlId, this.yahooId, this.nseId);
//		}
//		return stockIdConverstionUtil.retrieveEntries();
//	}

}
