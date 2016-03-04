package com.finanalyzer.domain.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class StockExceptionDbObject {
	
	@Persistent
	private String stockName; //moneycontrol name
	
	@Persistent
	private String exceptionComment;

	public StockExceptionDbObject(String stockName, String exceptionComment) {
		this.stockName = stockName;
		this.exceptionComment = exceptionComment;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public String getExceptionComment() {
		return exceptionComment;
	}

	public void setExceptionComment(String exceptionComment) {
		this.exceptionComment = exceptionComment;
	}
	
	@Override
	public String toString()
	{
		return stockName;
	}
}
