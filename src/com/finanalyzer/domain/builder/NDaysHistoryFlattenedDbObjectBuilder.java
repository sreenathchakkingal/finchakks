package com.finanalyzer.domain.builder;

import com.finanalyzer.domain.jdo.NDaysHistoryFlattenedDbObject;
import com.finanalyzer.domain.jdo.UnrealizedDetailDbObject;

public class NDaysHistoryFlattenedDbObjectBuilder {
	
	private NDaysHistoryFlattenedDbObject nDaysHistoryFlattenedDbObject = new NDaysHistoryFlattenedDbObject();

	public NDaysHistoryFlattenedDbObjectBuilder stockName(String stockName) {
		this.nDaysHistoryFlattenedDbObject.setStockName(stockName);
		return this;
	}
	
	public NDaysHistoryFlattenedDbObjectBuilder stockRatingValue(String stockRatingValue) {
		this.nDaysHistoryFlattenedDbObject.setStockRatingValue(stockRatingValue);
		return this;
	}

	public NDaysHistoryFlattenedDbObjectBuilder investmentRatio(float investmentRatio) {
		this.nDaysHistoryFlattenedDbObject.setInvestmentRatio(investmentRatio);
		return this;
	}
	
	public NDaysHistoryFlattenedDbObjectBuilder industryInvestmentRatio(float industryInvestmentRatio) {
		this.nDaysHistoryFlattenedDbObject.setIndustryInvestmentRatio(industryInvestmentRatio);
		return this;
	}
		
	public NDaysHistoryFlattenedDbObjectBuilder sellPrice(float sellPrice) {
		this.nDaysHistoryFlattenedDbObject.setSellPrice(sellPrice);
		return this;
	}
	
	public NDaysHistoryFlattenedDbObjectBuilder simpleMovingAverage(float simpleMovingAverage) {
		this.nDaysHistoryFlattenedDbObject.setSimpleMovingAverage(simpleMovingAverage);
		return this;
	}
	
	public NDaysHistoryFlattenedDbObjectBuilder simpleMovingAverageAndSellDeltaNormalized(float simpleMovingAverageAndSellDeltaNormalized) {
		this.nDaysHistoryFlattenedDbObject.setSimpleMovingAverageAndSellDeltaNormalized(simpleMovingAverageAndSellDeltaNormalized);
		return this;
	}
	
	public NDaysHistoryFlattenedDbObjectBuilder netNDaysGain(float netNDaysGain) {
		this.nDaysHistoryFlattenedDbObject.setNetNDaysGain(netNDaysGain);
		return this;
	}
		
	public NDaysHistoryFlattenedDbObjectBuilder industry(String industry) {
		this.nDaysHistoryFlattenedDbObject.setIndustry(industry);
		return this;
	}
	
	public NDaysHistoryFlattenedDbObjectBuilder nDay1Gain(float nDay1Gain) {
		this.nDaysHistoryFlattenedDbObject.setnDay1Gain(nDay1Gain);
		return this;
	}

	public NDaysHistoryFlattenedDbObjectBuilder nDay2Gain(float nDay2Gain) {
		this.nDaysHistoryFlattenedDbObject.setnDay2Gain(nDay2Gain);
		return this;
	}
	
	public NDaysHistoryFlattenedDbObjectBuilder nDay3Gain(float nDay3Gain) {
		this.nDaysHistoryFlattenedDbObject.setnDay3Gain(nDay3Gain);
		return this;
	}

	public NDaysHistoryFlattenedDbObjectBuilder nDay4Gain(float nDay4Gain) {
		this.nDaysHistoryFlattenedDbObject.setnDay4Gain(nDay4Gain);
		return this;
	}
	
	public NDaysHistoryFlattenedDbObjectBuilder nDay5Gain(float nDay5Gain) {
		this.nDaysHistoryFlattenedDbObject.setnDay5Gain(nDay5Gain);
		return this;
	}

	public NDaysHistoryFlattenedDbObjectBuilder nDay6Gain(float nDay6Gain) {
		this.nDaysHistoryFlattenedDbObject.setnDay6Gain(nDay6Gain);
		return this;
	}

	/*	
	
	public NDaysHistoryFlattenedDbObjectBuilder returnTillDate(float returnTillDate) {
		this.nDaysHistoryFlattenedDbObject.setReturnTillDate(returnTillDate);
		return this;
	}
	
	public NDaysHistoryFlattenedDbObjectBuilder duration(String duration) {
		this.nDaysHistoryFlattenedDbObject.setDuration(duration);
		return this;
	}
	
	public NDaysHistoryFlattenedDbObjectBuilder buyPrice(float buyPrice) {
		this.nDaysHistoryFlattenedDbObject.setBuyPrice(buyPrice);
		return this;
	}
	
	public NDaysHistoryFlattenedDbObjectBuilder impactOnAverageReturn(float impactOnAverageReturn) {
		this.nDaysHistoryFlattenedDbObject.setImpactOnAverageReturn(impactOnAverageReturn);
		return this;
	}
*/
	
	public NDaysHistoryFlattenedDbObject build()
	{
	    return this.nDaysHistoryFlattenedDbObject;
	}
	
	

}
