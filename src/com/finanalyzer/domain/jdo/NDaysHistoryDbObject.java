package com.finanalyzer.domain.jdo;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class NDaysHistoryDbObject {
	
	public static final Comparator<NDaysHistoryDbObject> SIMPLE_AVG_NET_GAINS_COMPARATOR = new Comparator<NDaysHistoryDbObject>()
	{
		@Override
		public int compare(NDaysHistoryDbObject stock1, NDaysHistoryDbObject stock2)
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
	
	@Persistent
	private String stockName; //nseName

	@Persistent
	private String moneyControlName; //moneyControlName
	
	@Persistent(serialized="true", defaultFetchGroup="true")
	private DummyStockRatingValue stockRatingValue;
	
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
	private List<String> dates;
	
	@Persistent(serialized="true", defaultFetchGroup="true")
	private Map<String, Float> nDaysGains;
	
	@Persistent
	private float returnTillDate;

	@Persistent
	private String duration;

	@Persistent
	private float buyPrice;

	public NDaysHistoryDbObject(String stockName, String moneyControlName,
			DummyStockRatingValue stockRatingValue, float investmentRatio,
			float industryInvestmentRatio, float sellPrice,
			float simpleMovingAverage, float simpleMovingAverageAndSellDeltaNormalized,
			float netNDaysGain, String industry,
			List<String> dates, Map<String, Float> nDaysGains) {
		this.stockName = stockName;
		this.moneyControlName = moneyControlName;
		this.stockRatingValue = stockRatingValue;
		this.investmentRatio = investmentRatio;
		this.industryInvestmentRatio = industryInvestmentRatio;
		this.sellPrice = sellPrice;
		this.simpleMovingAverage = simpleMovingAverage;
		this.simpleMovingAverageAndSellDeltaNormalized = simpleMovingAverageAndSellDeltaNormalized;
		this.netNDaysGain = netNDaysGain;
		this.industry = industry;
		this.dates = dates;
		this.nDaysGains = nDaysGains;
	}

	public static Comparator<NDaysHistoryDbObject> getSimpleAvgNetGainsComparator() {
		return SIMPLE_AVG_NET_GAINS_COMPARATOR;
	}

	public String getStockName() {
		return stockName;
	}

	public DummyStockRatingValue getStockRatingValue() {
		return stockRatingValue;
	}

	public float getInvestmentRatio() {
		return investmentRatio;
	}

	public float getIndustryInvestmentRatio() {
		return industryInvestmentRatio;
	}

	public float getSellPrice() {
		return sellPrice;
	}

	public float getSimpleMovingAverage() {
		return simpleMovingAverage;
	}

	public float getNetNDaysGain() {
		return netNDaysGain;
	}

	public String getIndustry() {
		return industry;
	}

	public List<String> getDates() {
		return dates;
	}

	public Map<String, Float> getnDaysGains() {
		return nDaysGains;
	}

	public float getSimpleMovingAverageAndSellDeltaNormalized() {
		return simpleMovingAverageAndSellDeltaNormalized;
	}

	public String getMoneyControlName() {
		return moneyControlName;
	}

	public float getReturnTillDate() {
		return returnTillDate;
	}

	public void setReturnTillDate(float returnTillDate) {
		this.returnTillDate = returnTillDate;
	}
	
	public void setDuration(String duration) {
		this.duration=duration;
		
	}
	
	public String getDuration() {
		return duration;
	}

	public void setBuyPrice(float buyPrice) {
		this.buyPrice=buyPrice;
	}
	
	public float getBuyPrice() {
		return buyPrice;
	}

	@Override
	public String toString()
	{
		return this.stockName;
	}


}

