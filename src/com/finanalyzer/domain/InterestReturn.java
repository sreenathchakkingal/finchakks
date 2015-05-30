package com.finanalyzer.domain;


public class InterestReturn
{
	private final String dateFrom;
	private final String dateTo;
	private final float interestRateForThePeriod;

	public InterestReturn(String dateFrom, String dateTo, float interestRateForYear)
	{
		this.dateFrom = dateFrom;
		this.dateTo=dateTo;
		this.interestRateForThePeriod = interestRateForYear;
	}

	public String getDateFromToDateTo()
	{
		return this.dateFrom+" To "+this.dateTo;
	}

	public float getInterestRateForThePeriod()
	{
		return this.interestRateForThePeriod;
	}

	public String getDateFrom()
	{
		return this.dateFrom;
	}

	public String getDateTo()
	{
		return this.dateTo;
	}

	@Override
	public String toString()
	{
		return this.getDateFromToDateTo() + ":" + this.getInterestRateForThePeriod();
	}
}
