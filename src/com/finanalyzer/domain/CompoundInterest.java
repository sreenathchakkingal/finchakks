package com.finanalyzer.domain;

public class CompoundInterest {
	private float initialAmount;
	private float interestRate;
	private int durationInDays;
	private float finalAmount;

	public CompoundInterest(float initialAmount, float interestRate,int durationInDays, float finalAmount) {
		this.initialAmount = initialAmount;
		this.interestRate = interestRate;
		this.durationInDays = durationInDays;
		this.finalAmount = finalAmount;
	}

	public float getInitialAmount() {
		return initialAmount;
	}

	public void setInitialAmount(float initialAmount) {
		this.initialAmount = initialAmount;
	}

	public float getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(float interestRate) {
		this.interestRate = interestRate;
	}

	public int getDurationInDays() {
		return durationInDays;
	}

	public void setDurationInDays(int durationInDays) {
		this.durationInDays = durationInDays;
	}

	public float getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(float finalAmount) {
		this.finalAmount = finalAmount;
	}

}
