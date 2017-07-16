package com.finanalyzer.domain.jdo;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;


@PersistenceCapable
public class UnrealizedDetailDbObject implements Serializable{

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
	
//	@Persistent
//	private float lowerReturnPercentTarget;
//
//	@Persistent
//	private float upperReturnPercentTarget;
//
//	@Persistent
//	private float lowerSellPriceTarget;
//	
//	@Persistent
//	private float upperSellPriceTarget;
//
//	@Persistent
//	private String achieveAfterDate;
//	
//	@Persistent
//	private String achieveByDate;
//
//	@Persistent
//	private boolean isTargetReached;
	
//	public static final Predicate<UnrealizedDetailDbObject> IS_TARGET_REACHED = new Predicate<UnrealizedDetailDbObject>() {
//		
//		@Override
//		public boolean accept(UnrealizedDetailDbObject unrealizedDetailDbObject) {
//			return unrealizedDetailDbObject.isTargetReached();
//		}
//	};
	
	
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
	
//	public boolean isTargetReached() {
//		return isTargetReached;
//	}
//
//	public void setTargetReached(boolean isTargetReached) {
//		this.isTargetReached = isTargetReached;
//	}
//
//	public float getLowerReturnPercentTarget() {
//		return lowerReturnPercentTarget;
//	}
//
//	public void setLowerReturnPercentTarget(float lowerReturnPercentTarget) {
//		this.lowerReturnPercentTarget = lowerReturnPercentTarget;
//	}
//
//	public float getUpperReturnPercentTarget() {
//		return upperReturnPercentTarget;
//	}
//
//	public void setUpperReturnPercentTarget(float upperReturnPercentTarget) {
//		this.upperReturnPercentTarget = upperReturnPercentTarget;
//	}
//
//	public float getLowerSellPriceTarget() {
//		return lowerSellPriceTarget;
//	}
//
//	public void setLowerSellPriceTarget(float lowerSellPriceTarget) {
//		this.lowerSellPriceTarget = lowerSellPriceTarget;
//	}
//
//	public float getUpperSellPriceTarget() {
//		return upperSellPriceTarget;
//	}
//
//	public void setUpperSellPriceTarget(float upperSellPriceTarget) {
//		this.upperSellPriceTarget = upperSellPriceTarget;
//	}
//
//	public String getAchieveAfterDate() {
//		return achieveAfterDate;
//	}
//
//	public void setAchieveAfterDate(String achieveAfterDate) {
//		this.achieveAfterDate = achieveAfterDate;
//	}
//
//	public String getAchieveByDate() {
//		return achieveByDate;
//	}
//
//	public void setAchieveByDate(String achieveByDate) {
//		this.achieveByDate = achieveByDate;
//	}

	@Override
	public String toString()
	{
		return this.stockName+" Qty: "+this.getQuantity()+" Duration: "+this.getDuration()+" Buy Date: "+this.getBuyDate()+" Buy Price: "+this.getBuyPrice();
	}
	
}

