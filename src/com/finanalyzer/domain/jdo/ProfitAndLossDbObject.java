package com.finanalyzer.domain.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class ProfitAndLossDbObject {
	
	@Persistent
	private float averageReturn;
	
	@Persistent
	private float totalInvestment;
	
	@Persistent
	private float totalReturn;
	
	@Persistent
	private float totalReturnIfBank;
	
	@Persistent
	private float totalReturnVsIfBank;
	
	public float getAverageReturn() {
		return averageReturn;
	}
	
	public void setAverageReturn(float averageReturn) {
		this.averageReturn = averageReturn;
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
	
	public float getTotalReturnVsIfBank() {
		return totalReturnVsIfBank;
	}
	
	public void setTotalReturnVsIfBank(float totalReturnVsIfBank) {
		this.totalReturnVsIfBank = totalReturnVsIfBank;
	}
	
}
