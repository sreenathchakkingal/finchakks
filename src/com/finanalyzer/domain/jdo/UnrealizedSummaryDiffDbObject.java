package com.finanalyzer.domain.jdo;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.gs.collections.api.block.function.Function;

@PersistenceCapable
public class UnrealizedSummaryDiffDbObject implements Serializable{
	
	public static final Function<UnrealizedSummaryDiffDbObject, Float> RETURN_DIFF_EXTRACTOR = new Function<UnrealizedSummaryDiffDbObject, Float>() {

		@Override
		public Float valueOf(UnrealizedSummaryDiffDbObject summaryDiffDbObject) {
			return summaryDiffDbObject.getAbsoluteDiffReturnTillDate();
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

	@Persistent
	private float absoluteDiffReturnTillDate;

	
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

	public float getAbsoluteDiffReturnTillDate() {
		return absoluteDiffReturnTillDate;
	}

	public void setAbsoluteDiffReturnTillDate(float absoluteDiffReturnTillDate) {
		this.absoluteDiffReturnTillDate = absoluteDiffReturnTillDate;
	}
	
}

