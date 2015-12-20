package com.finanalyzer.domain.jdo;

import java.util.Comparator;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.finanalyzer.domain.Stock;
import com.gs.collections.api.block.predicate.Predicate;

@PersistenceCapable
public class UnrealizedSummaryDbObject {

	@Persistent
	private String moneycontrolName;
	
	@Persistent
	private float returnTillDate;
	
	@Persistent
	private float quantity;
	
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
	
	public static final Predicate<UnrealizedSummaryDbObject> IS_BLACKLISTED = new Predicate<UnrealizedSummaryDbObject>() {
		
		@Override
		public boolean accept(UnrealizedSummaryDbObject unrealizedSummaryDbObject) {
			return unrealizedSummaryDbObject.isBlackListed();
		}
	};
	
	public UnrealizedSummaryDbObject(String moneycontrolName,
			float returnTillDate, float quantity, float totalInvestment,
			float totalReturn, float totalReturnIfBank, String industry, boolean isBlackListed) {
		this.moneycontrolName = moneycontrolName;
		this.returnTillDate = returnTillDate;
		this.quantity = quantity;
		this.totalInvestment = totalInvestment;
		this.totalReturn = totalReturn;
		this.totalReturnIfBank = totalReturnIfBank;
		this.industry = industry;
		this.isBlackListed = isBlackListed;
	}

	public String getMoneycontrolName() {
		return moneycontrolName;
	}

	public float getReturnTillDate() {
		return returnTillDate;
	}

	public float getQuantity() {
		return quantity;
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
}

