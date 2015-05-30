package com.finanalyzer.domain;

public class MappingStockId
{
	private String moneyControlId;
	private String yahooId;
	private String nseId;
	
	public MappingStockId(String moneyControlId, String yahooId, String nseId)
	{
		this.moneyControlId = moneyControlId;
		this.yahooId = yahooId;
		this.nseId = nseId;
	}

	public String getMoneyControlId()
	{
		return moneyControlId;
	}

	public String getYahooId()
	{
		return yahooId;
	}

	public String getNseId()
	{
		return nseId;
	}
	
	
	
	
}
