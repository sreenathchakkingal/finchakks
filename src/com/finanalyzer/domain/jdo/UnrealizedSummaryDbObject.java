package com.finanalyzer.domain.jdo;

import java.util.Comparator;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.finanalyzer.domain.Stock;
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
	
	public static final Predicate<UnrealizedSummaryDbObject> IS_BLACKLISTED = new Predicate<UnrealizedSummaryDbObject>() {
		
		@Override
		public boolean accept(UnrealizedSummaryDbObject unrealizedSummaryDbObject) {
			return unrealizedSummaryDbObject.isBlackListed();
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

	public void setImpactOnAverageReturn(float impactOnAverageReturn) {
		this.impactOnAverageReturn = impactOnAverageReturn;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}

