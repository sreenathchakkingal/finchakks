package com.finanalyzer.domain.jdo;

import java.util.Comparator;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.gs.collections.api.block.function.Function;

@PersistenceCapable
public class UnrealizedSummaryDiffDbObject {
	
	public static final Function<UnrealizedSummaryDiffDbObject, Float> RETURN_DIFF_EXTRACTOR = new Function<UnrealizedSummaryDiffDbObject, Float>() {

		@Override
		public Float valueOf(UnrealizedSummaryDiffDbObject arg0) {
			// TODO Auto-generated method stub
			return arg0.getDiffReturnTillDate();
		}
	};
	
	@Persistent
	private String stockName; //moneycontrol name
	
	@Persistent
	private float currentReturnTillDate;
	
	@Persistent
	private float prevReturnTillDate;
	
	@Persistent
	private float diffReturnTillDate;

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public float getCurrentReturnTillDate() {
		return currentReturnTillDate;
	}

	public void setCurrentReturnTillDate(float currentReturnTillDate) {
		this.currentReturnTillDate = currentReturnTillDate;
	}

	public float getPrevReturnTillDate() {
		return prevReturnTillDate;
	}

	public void setPrevReturnTillDate(float prevReturnTillDate) {
		this.prevReturnTillDate = prevReturnTillDate;
	}

	public float getDiffReturnTillDate() {
		return diffReturnTillDate;
	}

	public void setDiffReturnTillDate(float diffReturnTillDate) {
		this.diffReturnTillDate = diffReturnTillDate;
	}
	
	

}

