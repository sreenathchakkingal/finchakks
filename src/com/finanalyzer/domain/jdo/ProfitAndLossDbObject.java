package com.finanalyzer.domain.jdo;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class ProfitAndLossDbObject implements Serializable{
	
	@Persistent
	private float averageReturn;

	@Persistent
	private float prevAverageReturn;
	
	@Persistent
	private float totalInvestment;
	
	@Persistent
	private float totalReturn;
	
	@Persistent
	private float prevTotalReturn;

	@Persistent
	private float totalReturnIfBank;
	
	@Persistent
	private float totalReturnVsIfBank;
	
	@Persistent
	private float prevTotalReturnVsIfBank;
	
	@Persistent
	private float diffInCurrentAndPrevAverageReturn;
	
	@Persistent
	private float diffInCurrentAndPrevTotalReturn;
	
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

	public float getPrevAverageReturn() {
		return prevAverageReturn;
	}

	public void setPrevAverageReturn(float prevAverageReturn) {
		this.prevAverageReturn = prevAverageReturn;
	}

	public float getPrevTotalReturn() {
		return prevTotalReturn;
	}

	public void setPrevTotalReturn(float prevTotalReturn) {
		this.prevTotalReturn = prevTotalReturn;
	}

	public float getPrevTotalReturnVsIfBank() {
		return prevTotalReturnVsIfBank;
	}

	public void setPrevTotalReturnVsIfBank(float prevTotalReturnVsIfBank) {
		this.prevTotalReturnVsIfBank = prevTotalReturnVsIfBank;
	}

	public float getDiffInCurrentAndPrevAverageReturn() {
		return diffInCurrentAndPrevAverageReturn;
	}

	public void setDiffInCurrentAndPrevAverageReturn(
			float diffInCurrentAndPrevAverageReturn) {
		this.diffInCurrentAndPrevAverageReturn = diffInCurrentAndPrevAverageReturn;
	}

	public float getDiffInCurrentAndPrevTotalReturn() {
		return diffInCurrentAndPrevTotalReturn;
	}

	public void setDiffInCurrentAndPrevTotalReturn(
			float diffInCurrentAndPrevTotalReturn) {
		this.diffInCurrentAndPrevTotalReturn = diffInCurrentAndPrevTotalReturn;
	}

}
