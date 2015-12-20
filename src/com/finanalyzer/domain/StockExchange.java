package com.finanalyzer.domain;

public enum StockExchange
{
	BSE("BSE.BOM", 4, "BSE/BOM"), NSE("NSE.", 5, "NSE/"), MONEY_CONTROL(null, 0, null);

	private String quandlMultisetPrefix;
	private int quandlClosePriceColumnPosition;
	private String singleDatePrefix;

	private StockExchange(String quandlMultisetPrefix, int quandlClosePriceColumnPosition, String singleDatePrefix)
	{
		this.quandlMultisetPrefix=quandlMultisetPrefix;
		this.quandlClosePriceColumnPosition=quandlClosePriceColumnPosition;
		this.singleDatePrefix=singleDatePrefix;
	}

	public String getQuandlMultisetPrefix()
	{
		return this.quandlMultisetPrefix;
	}

	public int getQuandlClosePriceColumnPosition()
	{
		return this.quandlClosePriceColumnPosition;
	}
	
	public String getSingleDatePrefix()
	{
		return this.singleDatePrefix;
	}

}
