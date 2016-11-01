package com.finanalyzer.domain;

public class CompoundInterest {
	private float initalAmount;
	private float interestRate;
	private long durationInDays;
	private float finalAmount;

	public CompoundInterest(float initalAmount, float interestRate,long durationInDays, float finalAmount) {
		this.initalAmount = initalAmount;
		this.interestRate = interestRate;
		this.durationInDays = durationInDays;
		this.finalAmount = finalAmount;
	}

	public float getInitalAmount() {
		return initalAmount;
	}

	public void setInitalAmount(float initalAmount) {
		this.initalAmount = initalAmount;
	}

	public float getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(float interestRate) {
		this.interestRate = interestRate;
	}

	public long getDurationInDays() {
		return durationInDays;
	}

	public void setDurationInDays(long durationInDays) {
		this.durationInDays = durationInDays;
	}

	public float getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(float finalAmount) {
		this.finalAmount = finalAmount;
	}
}
