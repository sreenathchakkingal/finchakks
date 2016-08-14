package com.finanalyzer.domain.jdo;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.gs.collections.api.block.predicate.Predicate;

@PersistenceCapable
public class UnrealizedSummaryDbObject {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private String stockName; //moneycontrol name
	
	@Persistent
	private float returnTillDate;
	
	@Persistent
	private float quantity;
	
	@Persistent
	private float sellableQuantity;
		
	@Persistent
	private float totalInvestment;
	
	@Persistent
	private float totalReturn;
	
	@Persistent
	private float totalReturnIfBank;

	@Persistent
	private String industry;

	@Persistent
	private boolean isBlackListed;
	
	@Persistent
	private float impactOnAverageReturn;
	
	@Persistent
	private float diff;

	@Persistent
	private float sellPrice;
	
	@Persistent
	private float lowerReturnPercentTarget;

	@Persistent
	private float upperReturnPercentTarget;

	@Persistent
	private float lowerSellPriceTarget;
	
	@Persistent
	private float upperSellPriceTarget;

	@Persistent
	private String achieveAfterDate;
	
	@Persistent
	private String achieveByDate;

	@Persistent
	private boolean isTargetReached;
	
	public static final Predicate<UnrealizedSummaryDbObject> IS_BLACKLISTED = new Predicate<UnrealizedSummaryDbObject>() {
		
		@Override
		public boolean accept(UnrealizedSummaryDbObject unrealizedSummaryDbObject) {
			return unrealizedSummaryDbObject.isBlackListed();
		}
	};
	
	public static final Predicate<UnrealizedSummaryDbObject> IS_TARGET_REACHED = new Predicate<UnrealizedSummaryDbObject>() {
		
		@Override
		public boolean accept(UnrealizedSummaryDbObject unrealizedSummaryDbObject) {
			return unrealizedSummaryDbObject.isTargetReached();
		}
	};
	
	public float getReturnTillDate() {
		return returnTillDate;
	}

	public float getQuantity() {
		return quantity;
	}

	public float getSellableQuantity() {
		return sellableQuantity;
	}

	public float getTotalInvestment() {
		return totalInvestment;
	}

	public float getTotalReturn() {
		return totalReturn;
	}

	public float getTotalReturnIfBank() {
		return totalReturnIfBank;
	}

	public String getIndustry() {
		return industry;
	}

	public boolean isBlackListed() {
		return isBlackListed;
	}

	public void setReturnTillDate(float returnTillDate) {
		this.returnTillDate = returnTillDate;
	}

	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

	public void setSellableQuantity(float sellableQuantity) {
		this.sellableQuantity = sellableQuantity;
	}
	
	public void setTotalInvestment(float totalInvestment) {
		this.totalInvestment = totalInvestment;
	}

	public void setTotalReturn(float totalReturn) {
		this.totalReturn = totalReturn;
	}

	public void setTotalReturnIfBank(float totalReturnIfBank) {
		this.totalReturnIfBank = totalReturnIfBank;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public void setBlackListed(boolean isBlackListed) {
		this.isBlackListed = isBlackListed;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	
	public float getImpactOnAverageReturn() {
		return impactOnAverageReturn;
	}
	
	public float getDiff() {
		return diff;
	}

	public void setDiff(float diff) {
		this.diff = diff;
	}

	public void setImpactOnAverageReturn(float impactOnAverageReturn) {
		this.impactOnAverageReturn = impactOnAverageReturn;
	}

	public float getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(float sellPrice) {
		this.sellPrice = sellPrice;
	}

	public float getLowerReturnPercentTarget() {
		return lowerReturnPercentTarget;
	}

	public void setLowerReturnPercentTarget(float lowerReturnPercentTarget) {
		this.lowerReturnPercentTarget = lowerReturnPercentTarget;
	}

	public float getUpperReturnPercentTarget() {
		return upperReturnPercentTarget;
	}

	public void setUpperReturnPercentTarget(float upperReturnPercentTarget) {
		this.upperReturnPercentTarget = upperReturnPercentTarget;
	}

	public float getLowerSellPriceTarget() {
		return lowerSellPriceTarget;
	}

	public void setLowerSellPriceTarget(float lowerSellPriceTarget) {
		this.lowerSellPriceTarget = lowerSellPriceTarget;
	}

	public float getUpperSellPriceTarget() {
		return upperSellPriceTarget;
	}

	public void setUpperSellPriceTarget(float upperSellPriceTarget) {
		this.upperSellPriceTarget = upperSellPriceTarget;
	}

	public String getAchieveAfterDate() {
		return achieveAfterDate;
	}

	public void setAchieveAfterDate(String achieveAfterDate) {
		this.achieveAfterDate = achieveAfterDate;
	}

	public String getAchieveByDate() {
		return achieveByDate;
	}

	public void setAchieveByDate(String achieveByDate) {
		this.achieveByDate = achieveByDate;
	}

	public boolean isTargetReached() {
		return isTargetReached;
	}

	public void setTargetReached(boolean isTargetReached) {
		this.isTargetReached = isTargetReached;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}

