package com.finanalyzer.domain.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.gs.collections.api.block.predicate.Predicate;


@PersistenceCapable
public class UnrealizedDetailDbObject {

	@Persistent
	private String stockName;
	
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
	
	@Persistent
	private float totalInvestment;
	
	@Persistent
	private float totalReturn;
	
	@Persistent
	private float totalReturnIfBank;

	@Persistent
	private float diff;
	
	@Persistent
	private float targetReturnPercent;
	
	@Persistent
	private float targetSellPrice;
	
	@Persistent
	private String targetDate;

	@Persistent
	private boolean isTargetReached;
	
	public static final Predicate<UnrealizedDetailDbObject> IS_TARGET_REACHED = new Predicate<UnrealizedDetailDbObject>() {
		
		@Override
		public boolean accept(UnrealizedDetailDbObject unrealizedDetailDbObject) {
			return unrealizedDetailDbObject.isTargetReached();
		}
	};
	
	
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
	
	public float getTotalInvestment() {
		return totalInvestment;
	}

	public void setTotalInvestment(float totalInvestment) {
		this.totalInvestment = totalInvestment;
	}

	public float getTotalReturn() {
		return totalReturn;
	}

	public void setTotalReturn(float totalReturn) {
		this.totalReturn = totalReturn;
	}

	public float getTotalReturnIfBank() {
		return totalReturnIfBank;
	}

	public void setTotalReturnIfBank(float totalReturnIfBank) {
		this.totalReturnIfBank = totalReturnIfBank;
	}

	public float getDiff() {
		return diff;
	}

	public void setDiff(float diff) {
		this.diff = diff;
	}
	
	public boolean isTargetReached() {
		return isTargetReached;
	}

	public void setTargetReached(boolean isTargetReached) {
		this.isTargetReached = isTargetReached;
	}

	public float getTargetReturnPercent() {
		return targetReturnPercent;
	}

	public void setTargetReturnPercent(float targetReturnPercent) {
		this.targetReturnPercent = targetReturnPercent;
	}

	public float getTargetSellPrice() {
		return targetSellPrice;
	}

	public void setTargetSellPrice(float targetSellPrice) {
		this.targetSellPrice = targetSellPrice;
	}

	public String getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(String targetDate) {
		this.targetDate = targetDate;
	}

	@Override
	public String toString()
	{
		return this.stockName+" Qty: "+this.getQuantity()+" Duration: "+this.getDuration()+" Buy Date: "+this.getBuyDate()+" Buy Price: "+this.getBuyPrice();
	}
	
}

