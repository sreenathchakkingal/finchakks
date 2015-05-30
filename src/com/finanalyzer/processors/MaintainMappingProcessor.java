package com.finanalyzer.processors;

import java.util.List;

import com.finanalyzer.db.StockIdConverstionUtil;
import com.finanalyzer.domain.MappingStockId;

public class MaintainMappingProcessor implements Processor<List<MappingStockId>>
{
	private final String moneyControlId;
	private final String yahooId;
	private final String nseId;
	private final String[] selectedMappings;
	private final boolean isDelete;

	public MaintainMappingProcessor(String moneyControlId, String yahooId, String nseId, String[] selectedMappings, boolean isDelete)
	{
		this.moneyControlId=moneyControlId;
		this.yahooId=yahooId;
		this.nseId=nseId;
		this.selectedMappings = selectedMappings;
		this.isDelete=isDelete;
	}

	@Override
	public List<MappingStockId> execute()
	{
		StockIdConverstionUtil stockIdConverstionUtil = new StockIdConverstionUtil();
		if(this.isDelete)
		{
			stockIdConverstionUtil.deleteRecords(this.selectedMappings);
		}
		else
		{
			stockIdConverstionUtil.insertConversion(this.moneyControlId, this.yahooId, this.nseId);
		}
		return stockIdConverstionUtil.retrieveEntries();
	}

}
