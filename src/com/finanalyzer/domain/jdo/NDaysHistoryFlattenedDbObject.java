package com.finanalyzer.domain.jdo;

import java.io.Serializable;
import java.util.Comparator;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.gs.collections.api.block.predicate.Predicate;

@PersistenceCapable
public class NDaysHistoryFlattenedDbObject implements Serializable{
	
	public static final Comparator<NDaysHistoryFlattenedDbObject> SIMPLE_AVG_NET_GAINS_COMPARATOR = new Comparator<NDaysHistoryFlattenedDbObject>()
	{
		@Override
		public int compare(NDaysHistoryFlattenedDbObject stock1,NDaysHistoryFlattenedDbObject stock2)
		{
			int totalForStock1 = Math.round(stock1.getSimpleMovingAverageAndSellDeltaNormalized())  + Math.round(stock1.getNetNDaysGain() * 100);
			int totalForStock2 =Math.round(stock2.getSimpleMovingAverageAndSellDeltaNormalized()) + Math.round(stock2.getNetNDaysGain() * 100);
			int totalDiff = totalForStock1-totalForStock2;
			if (totalDiff == 0)
			{
				return stock1.getStockName().compareTo(stock2.getStockName());
			}
			return totalDiff;
		}
	};
	
	public static final Predicate<NDaysHistoryFlattenedDbObject> IS_LATEST_CLOSE_PRICE_MIN_OR_MAX_FILTER = 
			new Predicate<NDaysHistoryFlattenedDbObject>() {
		@Override
		public boolean accept(NDaysHistoryFlattenedDbObject nDaysHistoryFlattenedDbObject) {
			
			//pick stocks that has hit max and is invested
			boolean isClosePriceCloseToMaxAndInvested = nDaysHistoryFlattenedDbObject.isLatestClosePriceMaximum() && 
					nDaysHistoryFlattenedDbObject.getInvestmentRatio()>0.0f;
					
			//pick stocks that has hit max - does not care if we have invested or not. This could be an investment opportunity
			boolean isClosePriceCloseToMin = nDaysHistoryFlattenedDbObject.isLatestClosePriceMinimum();

			return  isClosePriceCloseToMin || isClosePriceCloseToMaxAndInvested;
		}
	};
	
	@Persistent
	private String stockName; //nseId

	@Persistent
	private String stockRatingValue;
	
	@Persistent
	private float investmentRatio;
	
	@Persistent
	private float industryInvestmentRatio;
	
	@Persistent
	private float sellPrice;
	
	@Persistent
	private float simpleMovingAverage;
	
	@Persistent
	private float simpleMovingAverageAndSellDeltaNormalized;
	
	@Persistent
	private float netNDaysGain;
	
	@Persistent
	private String industry;
	
	@Persistent
	private float nDay1Gain;
	
	@Persistent
	private float nDay2Gain;

	@Persistent
	private float nDay3Gain;

	@Persistent
	private float nDay4Gain;

	@Persistent
	private float nDay5Gain;

	@Persistent
	private float nDay6Gain;	
	
	@Persistent
	private boolean isLatestClosePriceMinimum;	

	@Persistent
	private float minValue;	
	
	@Persistent
	private String minValueDate;	

	@Persistent
	private float maxValue;	
	
	@Persistent
	private String maxValueDate;	
	
	@Persistent
	private boolean isLatestClosePriceMaximum;	
	
	public String getStockName() {
		return stockName;
	}


	public void setStockName(String stockName) {
		this.stockName = stockName;
	}


	public String getStockRatingValue() {
		return stockRatingValue;
	}


	public void setStockRatingValue(String stockRatingValue) {
		this.stockRatingValue = stockRatingValue;
	}


	public float getInvestmentRatio() {
		return investmentRatio;
	}


	public void setInvestmentRatio(float investmentRatio) {
		this.investmentRatio = investmentRatio;
	}


	public float getIndustryInvestmentRatio() {
		return industryInvestmentRatio;
	}


	public void setIndustryInvestmentRatio(float industryInvestmentRatio) {
		this.industryInvestmentRatio = industryInvestmentRatio;
	}


	public float getSellPrice() {
		return sellPrice;
	}


	public void setSellPrice(float sellPrice) {
		this.sellPrice = sellPrice;
	}


	public float getSimpleMovingAverage() {
		return simpleMovingAverage;
	}


	public void setSimpleMovingAverage(float simpleMovingAverage) {
		this.simpleMovingAverage = simpleMovingAverage;
	}


	public float getSimpleMovingAverageAndSellDeltaNormalized() {
		return simpleMovingAverageAndSellDeltaNormalized;
	}


	public void setSimpleMovingAverageAndSellDeltaNormalized(
			float simpleMovingAverageAndSellDeltaNormalized) {
		this.simpleMovingAverageAndSellDeltaNormalized = simpleMovingAverageAndSellDeltaNormalized;
	}


	public float getNetNDaysGain() {
		return netNDaysGain;
	}


	public void setNetNDaysGain(float netNDaysGain) {
		this.netNDaysGain = netNDaysGain;
	}


	public String getIndustry() {
		return industry;
	}


	public void setIndustry(String industry) {
		this.industry = industry;
	}


	public float getnDay1Gain() {
		return nDay1Gain;
	}


	public void setnDay1Gain(float nDay1Gain) {
		this.nDay1Gain = nDay1Gain;
	}


	public float getnDay2Gain() {
		return nDay2Gain;
	}


	public void setnDay2Gain(float nDay2Gain) {
		this.nDay2Gain = nDay2Gain;
	}


	public float getnDay3Gain() {
		return nDay3Gain;
	}


	public void setnDay3Gain(float nDay3Gain) {
		this.nDay3Gain = nDay3Gain;
	}


	public float getnDay4Gain() {
		return nDay4Gain;
	}


	public void setnDay4Gain(float nDay4Gain) {
		this.nDay4Gain = nDay4Gain;
	}


	public float getnDay5Gain() {
		return nDay5Gain;
	}


	public void setnDay5Gain(float nDay5Gain) {
		this.nDay5Gain = nDay5Gain;
	}


	public float getnDay6Gain() {
		return nDay6Gain;
	}


	public void setnDay6Gain(float nDay6Gain) {
		this.nDay6Gain = nDay6Gain;
	}
	

	public boolean isLatestClosePriceMinimum() {
		return isLatestClosePriceMinimum;
	}

	public void setLatestClosePriceMinimum(boolean isLatestClosePriceMinimum) {
		this.isLatestClosePriceMinimum = isLatestClosePriceMinimum;
	}
	
	public float getMinValue() {
		return minValue;
	}


	public void setMinValue(float minValue) {
		this.minValue = minValue;
	}

	public String getMinValueDate() {
		return minValueDate;
	}

	public void setMinValueDate(String minValueDate) {
		this.minValueDate = minValueDate;
	}

	public float getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}

	public String getMaxValueDate() {
		return maxValueDate;
	}

	public void setMaxValueDate(String maxValueDate) {
		this.maxValueDate = maxValueDate;
	}
	
	public boolean isLatestClosePriceMaximum() {
		return isLatestClosePriceMaximum;
	}

	public void setLatestClosePriceMaximum(boolean isLatestClosePriceMaximum) {
		this.isLatestClosePriceMaximum = isLatestClosePriceMaximum;
	}

	@Override
	public String toString()
	{
		return this.stockName;
	}


}

