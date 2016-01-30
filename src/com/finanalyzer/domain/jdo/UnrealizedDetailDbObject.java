package com.finanalyzer.domain.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;


@PersistenceCapable
public class UnrealizedDetailDbObject {

	@Persistent
	private String stockName; //moneycontrolName
	
	@Persistent
	private float returnTillDate;
	
	@Persistent
	private String buyDate;
	
	@Persistent
	private float buyPrice;

	@Persistent
	private String duration;
	
	@Persistent
	private float sellPrice;

	@Persistent
	private float bankSellPrice;

	@Persistent
	private float quantity;
	
	@Persistent
	private float sellableQuantity;
	
	@Persistent
	private boolean isMoreThanAnYear;
	
	@Persistent
	private boolean isMaturityIsCloseToAnYear;
	
	public UnrealizedDetailDbObject()
	{
		
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public float getReturnTillDate() {
		return returnTillDate;
	}

	public void setReturnTillDate(float returnTillDate) {
		this.returnTillDate = returnTillDate;
	}

	public String getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}

	public float getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(float buyPrice) {
		this.buyPrice = buyPrice;
	}

	public float getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(float sellPrice) {
		this.sellPrice = sellPrice;
	}

	public float getBankSellPrice() {
		return bankSellPrice;
	}

	public void setBankSellPrice(float bankSellPrice) {
		this.bankSellPrice = bankSellPrice;
	}

	public float getQuantity() {
		return quantity;
	}

	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public float getSellableQuantity() {
		return sellableQuantity;
	}

	public void setSellableQuantity(float sellableQuantity) {
		this.sellableQuantity = sellableQuantity;
	}

	public boolean getIsMoreThanAnYear() {
		return isMoreThanAnYear;
	}

	public void setMoreThanAnYear(boolean isMoreThanAnYear) {
		this.isMoreThanAnYear = isMoreThanAnYear;
	}

	public boolean getIsMaturityIsCloseToAnYear() {
		return isMaturityIsCloseToAnYear;
	}

	public void setMaturityIsCloseToAnYear(boolean isMaturityIsCloseToAnYear) {
		this.isMaturityIsCloseToAnYear = isMaturityIsCloseToAnYear;
	}
	
	@Override
	public String toString()
	{
		return this.stockName+" Qty: "+this.getQuantity()+" Duration: "+this.getDuration()+" Buy Date: "+this.getBuyDate()+" Buy Price: "+this.getBuyPrice();
	}
	
}

