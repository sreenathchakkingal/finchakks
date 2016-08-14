package com.finanalyzer.domain;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.annotations.Persistent;

import com.finanalyzer.domain.jdo.StopLossDbObject;
import com.finanalyzer.util.CalculatorUtil;
import com.finanalyzer.util.DateUtil;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;

public class Stock
{
	private String stockName;
	
	private List<DateValueObject> dividends;

	private float investmentRatio;
	
	private float simpleMovingAverage;
	
	private List<DateValueObject> dateToClosePrice;
	
	private Map<String, Float> nDaysGains;
	
	private float sellPrice;
	private String duration;
	
	private int ups;
	private int downs;
	private float averageInterestReturn;
	private String buyDate;
	private float buyPrice;
	private String sellDate;
	
	private float quantity;
	private float sellableQuantity=-1;
	private float totalInvestment;
	
	private float totalReturn;
	private float totalReturnIfBank;
	private float diff=Float.MIN_VALUE;
	
	private boolean isException=false;
	private String graphToEquityOpinion;
	private String reportedNetProfitOpinion;
	private String debtToEquityOpinion;
	private List<InterestReturn> interestReturns;
	
	private float returnTillDate=Float.MAX_VALUE;
	
	
	private Adjustment splitAdjustment;
	private Adjustment bonusAdjustment;
	private StockExchange stockExchange;
	private int numOfDays;
	private StockRatingValue stockRatingValue;
	public Map<StockExchange, String> stockExchangeStocknameMap = UnifiedMap.newMap();
	private String industry;
	private Float industryInvestmentRatio;

	private String exceptionComment="";
	
	private float impactOnAverageReturn;
	
	private StopLossDbObject stopLossDbObject;
	private float targetReturnPercent;
	private float targetSellPrice;
	private String targetDate;
	private boolean isReachedStopLossTarget;
	private boolean isBlackListed;
	
	
	@SuppressWarnings("serial")
	public static final Function<Stock, String> STOCKNAME_SELECTOR = new Function<Stock, String>()
	{

		@Override
		public String valueOf(Stock stock)
		{
			return stock.getStockName();
		}
	};
	
	public static final Function<Stock, Date> BUY_DATE_SELECTOR = new Function<Stock, Date>() {

		@Override
		public Date valueOf(Stock stock) {
			try {
				return DateUtil.YYYY_MM_DD_FORMAT.parse(stock.getBuyDate());
			} catch (ParseException e) 
			{
				e.printStackTrace();
				return null;
			}
		}
	};
	
	public Stock ()
	{

	}
	public Stock(String stockName)
	{
		this(stockName, StockExchange.NSE);
	}

	public Stock(String stockName, StockExchange stockExchange)
	{
		this.stockName=stockName;
		this.stockExchange=stockExchange;
		this.stockExchangeStocknameMap.put(stockExchange, stockName);
	}
	
	public String getStockName()
	{
		return this.stockName;
	}

	public void setStockName(String stockName)
	{
		this.stockName=stockName;
		this.stockExchangeStocknameMap.put(StockExchange.NSE, stockName);
	}

	public String getStockName(StockExchange stockExchange)
	{
		return this.stockExchangeStocknameMap.get(stockExchange);
	}

	public List<DateValueObject> getDividends()
	{
		return this.dividends;
	}

	public void setDividends(List<DateValueObject> dividends)
	{
		this.dividends = dividends;
	}

	public int getDivdendFrequencyInMonths()
	{
		if (this.dividends!=null && this.dividends.size() > 1)
		{
			String dateFrom = this.dividends.get(this.dividends.size() - 1).getDate();
			String dateTo = this.dividends.get(0).getDate();
			int numberOfDays = DateUtil.differenceBetweenDates(dateTo, dateFrom);
			return numberOfDays / DateUtil.NUMBER_OF_DAYS_IN_MONTH / this.dividends.size();
		}
		return 0;
	}

	public String getBuyDate()
	{
		return this.buyDate;
	}

	public void setBuyDate(String buyDate)
	{
		this.buyDate = buyDate;
	}

	public String getSellDate()
	{
		if (this.sellDate==null)
		{
			return this.getDateToClosePrice().get(0).getDate();
		}
		return this.sellDate;
	}

	public void setSellDate(String sellDate)
	{
		this.sellDate = sellDate;
	}

	public float getBuyPrice()
	{
		return this.buyPrice;
	}

	public void setBuyPrice(float buyPrice)
	{
		this.buyPrice = buyPrice;
	}

	public float getQuantity()
	{
		return this.quantity;
	}

	public void setQuantity(float quantity)
	{
		this.quantity = quantity;
	}

	public float getSellPrice()
	{
		if(this.dateToClosePrice==null)
		{
			return this.sellPrice;
		}
		return this.dateToClosePrice.get(0).getValue();
	}

	public void setSellPrice(float sellPrice)
	{
		this.sellPrice = sellPrice;
	}

	public float getBankSellPrice()
	{
		return CalculatorUtil.caculateFinalPrice(this.getBuyPrice(), 10.0f, this.getDifferenceBetweeBuyDateAndSellDate());
	}

	public float getTotalInvestment()
	{
		if (this.totalInvestment <= 1.0D)
		{
			return this.quantity * this.buyPrice;
		}
		return this.totalInvestment;
	}

	public void setTotalInvestment(float totalInvestment)
	{
		this.totalInvestment = totalInvestment;
	}

	public float getAverageInterestReturn()
	{
		final List<InterestReturn> interestReturns = this.getInterestReturns();
		float netInterestRates=0.0f;
		for (InterestReturn interestReturn : interestReturns)
		{
			netInterestRates=netInterestRates+ interestReturn.getInterestRateForThePeriod();
		}
		return netInterestRates / interestReturns.size();
	}

	public void setAverageInterestReturn(float averageInterestReturn)
	{
		this.averageInterestReturn = averageInterestReturn;
	}

	public List<InterestReturn> getInterestReturns()
	{
		List<InterestReturn> returns = FastList.newList();
		for (int i=0; i<this.dateToClosePrice.size()-1; i++)
		{
			final DateValueObject currentDateValueObject = this.dateToClosePrice.get(i);
			final DateValueObject prevDateValueObject = this.dateToClosePrice.get(i+1);

			float currentYearClosePrice = currentDateValueObject.getValue();
			String currentYear= currentDateValueObject.getDate();

			float prevYearClosePrice = prevDateValueObject.getValue();
			String prevYear= prevDateValueObject.getDate();

			float returnForYear = CalculatorUtil.calculateQuaterlyCompoundedInterest(prevYearClosePrice, currentYearClosePrice, DateUtil.NUMBER_OF_DAYS_IN_YEAR);
			returns.add(new InterestReturn(currentYear, prevYear, returnForYear));
		}
		return returns;
	}

	//replace with UpsAndDowns domain object - does the same iteration as in getDowns()
	public int getUps()
	{
		int ups = 0;
		for (int i=0; i<this.dateToClosePrice.size()-1; i++)
		{
			final DateValueObject currentDateValueObject = this.dateToClosePrice.get(i);
			final DateValueObject prevDateValueObject = this.dateToClosePrice.get(i+1);

			float currentYearClosePrice = currentDateValueObject.getValue();
			float prevYearClosePrice = prevDateValueObject.getValue();

			if(currentYearClosePrice>prevYearClosePrice)
			{
				ups++;
			}
		}
		return ups;
	}

	public void setUps(int ups)
	{
		this.ups = ups;
	}

	public int getDowns()
	{
		int downs = 0;
		for (int i=0; i<this.dateToClosePrice.size()-1; i++)
		{
			final DateValueObject currentDateValueObject = this.dateToClosePrice.get(i);
			final DateValueObject prevDateValueObject = this.dateToClosePrice.get(i+1);

			float currentYearClosePrice = currentDateValueObject.getValue();
			float prevYearClosePrice = prevDateValueObject.getValue();

			if(currentYearClosePrice<prevYearClosePrice)
			{
				downs++;
			}
		}
		return downs;
	}

	public void setDowns(int downs)
	{
		this.downs = downs;
	}


	public void setInterestReturns(List<InterestReturn> interestReturns)
	{
		this.interestReturns = interestReturns;
	}

	public float getTotalReturn()
	{
		if (this.totalReturn <= 1.0D)
		{
			return this.quantity * this.getSellPrice();
		}
		return this.totalReturn;
	}

	public void setTotalReturn(float totalReturn)
	{
		this.totalReturn = totalReturn;
	}

	public float getTotalReturnIfBank()
	{
		if (this.totalReturnIfBank <= 1.0D)
		{
			return this.quantity * this.getBankSellPrice();
		}
		return this.totalReturnIfBank;
	}
	
	public float getDiff()
	{
		if(this.diff==Float.MIN_VALUE)
		{
			this.diff = getTotalReturn()-getTotalReturnIfBank();	
		}
		return this.diff;
	}
	
	public void setDiff(float diff)
	{
		this.diff = diff;
	}

	public void setTotalReturnIfBank(float totalReturnIfBank)
	{
		this.totalReturnIfBank = totalReturnIfBank;
	}

	public boolean getIsException()
	{
		return this.isException;
	}

	public void setIsException(String exceptionComment)
	{
		this.isException = true;
		this.exceptionComment=" "+exceptionComment;
	}

	public String getExceptionComment() {
		return exceptionComment;
	}
	
	public String getGraphToEquityOpinion()
	{
		return this.graphToEquityOpinion;
	}

	public void setGraphToEquityOpinion(String graphToEquityOpinion)
	{
		this.graphToEquityOpinion = graphToEquityOpinion;
	}

	public String getReportedNetProfitOpinion()
	{
		return this.reportedNetProfitOpinion;
	}

	public void setReportedNetProfitOpinion(String reportedNetProfitOpinion)
	{
		this.reportedNetProfitOpinion = reportedNetProfitOpinion;
	}

	public String getDebtToEquityOpinion()
	{
		return this.debtToEquityOpinion;
	}

	public void setDebtToEquityOpinion(String debtToEquityOpinion)
	{
		this.debtToEquityOpinion = debtToEquityOpinion;
	}

	@Override
	public boolean equals(Object o)
	{
		if ((o instanceof Stock))
		{
			return this.stockName.equals(((Stock) o).getStockName());
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return this.stockName==null ? 13 : this.stockName.hashCode();
	}

	public float getSellableQuantity()
	{
		if(this.sellableQuantity==-1)
		{
			return this.getIsMoreThanAnYear() && this.isReturnTillDateMoreThanExpected(15.0f) ? this.getQuantity() : 0;
		}
		return this.sellableQuantity;
	}

	public boolean getIsMoreThanAnYear()
	{
		return this.getDifferenceBetweeBuyDateAndSellDate() > DateUtil.NUMBER_OF_DAYS_IN_YEAR;
	}

	public void setSellableQuantity(float sellableQuantity)
	{
		this.sellableQuantity = sellableQuantity;
	}

	public int getDifferenceBetweeBuyDateAndSellDate()
	{
		return this.getSellDate() == null ? 0 : DateUtil.differenceBetweenDates(this.getSellDate(), this.getBuyDate());
	}

	public boolean isReturnTillDateMoreThanExpected(float expected)
	{
		float returnTillDate = this.getReturnTillDate();
		return returnTillDate >= expected;
	}

	public float getReturnTillDate()
	{
		if(this.returnTillDate==Float.MAX_VALUE)
		{
			return CalculatorUtil.calculateQuaterlyCompoundedInterest(this.getBuyPrice(), this.getSellPrice(), this .getDifferenceBetweeBuyDateAndSellDate());			
		}
		return this.returnTillDate;
	}

	public void setReturnTillDate(float returnTillDate)
	{
		this.returnTillDate = returnTillDate;
	}

	public boolean getIsMaturityIsCloseToAnYear()
	{
		return this.getDifferenceBetweeBuyDateAndSellDate() > 330 && !this.getIsMoreThanAnYear();//between 11 and 12 months
	}

	public Map<String, Float> getnDaysGains()
	{
		if (this.nDaysGains==null)
		{
			this.nDaysGains = new LinkedHashMap<>();
			for (int i=0;i<=Integer.valueOf(this.getNumOfDays());i++)
			{
				DateValueObject currentDateToClosePrice = this.dateToClosePrice.get(i);
				DateValueObject prevDateToClosePrice = this.dateToClosePrice.get(i+1);
				String currentDate = currentDateToClosePrice.getDate();
				float currentClosePrice = currentDateToClosePrice.getValue();
				float prevClosePrice = prevDateToClosePrice.getValue();
				float gain = (currentClosePrice-prevClosePrice)/prevClosePrice*100;
				this.nDaysGains.put(currentDate, gain);
			}
		}
		return this.nDaysGains;
	}

	public void setnDaysGains(Map<String, Float> nDaysGains)
	{
		this.nDaysGains = nDaysGains;
	}

	public List<String> getDates()
	{
		List<String> dates = FastList.newList();
		for (Map.Entry<String, Float> eachEntry : this.getnDaysGains().entrySet())
		{
			dates.add(eachEntry.getKey());
		}
		return dates;
	}

	public float getNetNDaysGain()
	{
		Collection<Float> values = this.getnDaysGains().values();
		float netGain=0.0f;
		for(Float eachDayGain : values)
		{
			netGain=netGain+eachDayGain;	
		}
		return netGain/100.0f;

	}

	public float getSimpleMovingAverage()
	{
		if (this.simpleMovingAverage==0)
		{
			float sumOfClosePrices=0;
			for (DateValueObject dateValueObject : this.dateToClosePrice)
			{
				sumOfClosePrices=sumOfClosePrices+dateValueObject.getValue();	
			}
			this.simpleMovingAverage=sumOfClosePrices/this.dateToClosePrice.size();
		}
		return this.simpleMovingAverage;
	}

	public void setSimpleMovingAverage(float simpleMovingAverage)
	{
		this.simpleMovingAverage = simpleMovingAverage;
	}
	public float getSimpleMovingAverageAndSellDeltaNormalized()
	{
		float simpleMovingAverage=this.getSimpleMovingAverage();
		return (this.getSellPrice()-simpleMovingAverage)/simpleMovingAverage*100;
	}

	public List<DateValueObject> getDateToClosePrice()
	{
		return this.dateToClosePrice;
	}

	public void setDateToClosePrice(List<DateValueObject> dateToClosePrice)
	{
		this.dateToClosePrice = dateToClosePrice;
	}

	public void setAdjustments(Adjustment splitAdjustment, Adjustment bonusAdjustment)
	{
		this.splitAdjustment=splitAdjustment;
		this.bonusAdjustment=bonusAdjustment;
	}

	public List<DateValueObject> getYearToAdjustedClosePrices()
	{
		if (this.splitAdjustment!=null)
		{
			this.applyAdjustment(this.splitAdjustment.getFactor(), this.splitAdjustment.getYear(), this.splitAdjustment.isApplyFromCurrentYear());
		}
		if (this.bonusAdjustment!=null)
		{
			this.applyAdjustment(this.bonusAdjustment.getFactor(), this.bonusAdjustment.getYear(), this.bonusAdjustment.isApplyFromCurrentYear());
		}
		return this.dateToClosePrice;
	}

	private void applyAdjustment(float adjustmentFactor, int adjustmentYear, boolean isApplyAdjustmentFromCurrentYear)
	{
		final List<DateValueObject> dataSetToBeAdjusted =this.getDataSetToBeAdjusted(adjustmentYear, isApplyAdjustmentFromCurrentYear);
		this.applyAdjustmentToDataSet(dataSetToBeAdjusted, adjustmentFactor);
	}

	private List<DateValueObject> getDataSetToBeAdjusted(int adjustmentYear, boolean isApplyAdjustmentFromCurrentYear)
	{
		for (int i=0; i<this.dateToClosePrice.size(); i++)
		{
			DateValueObject eachYearToClosePrice = this.dateToClosePrice.get(i);
			if(eachYearToClosePrice.getDate().contains(String.valueOf(adjustmentYear)))
			{
				int beginIndex = isApplyAdjustmentFromCurrentYear ? i : i+1;
				return this.dateToClosePrice.subList(beginIndex, this.dateToClosePrice.size());
			}	
		}
		return null;
	}

	private void applyAdjustmentToDataSet(List<DateValueObject> dataSetToBeAdjusted, float adjustmentFactor)
	{
		for (DateValueObject eachYearToClosePrice : dataSetToBeAdjusted)
		{
			final float unadjsutedValue = eachYearToClosePrice.getValue();
			eachYearToClosePrice.setValue(unadjsutedValue/adjustmentFactor);
		}
	}

	public Movement getMovement()
	{
		int ups = 0;
		int downs = 0;
		for (int i=0; i<this.dateToClosePrice.size()-1; i++)
		{
			final DateValueObject currentDateValueObject = this.dateToClosePrice.get(i);
			final DateValueObject prevDateValueObject = this.dateToClosePrice.get(i+1);

			float currentYearClosePrice = currentDateValueObject.getValue();
			float prevYearClosePrice = prevDateValueObject.getValue();

			if(currentYearClosePrice>prevYearClosePrice)
			{
				ups++;
			}
			else
			{
				downs++;
			}
		}

		return new Movement(ups, downs);
	}

	public StockExchange getStockExchange()
	{
		return this.stockExchange;
	}

	public void setStockExchange(StockExchange stockExchange)
	{
		this.stockExchange = stockExchange;
	}

	//tactical : remove when you learn to pass it as argument from the ndayshistory jsp. Not a stock attribute
	public int getNumOfDays()
	{
		return numOfDays;
	}
	
	public void setNumOfDays(int numOfDays)
	{
		this.numOfDays = numOfDays;
	}
	
	public StockRatingValue getStockRatingValue() {
		return stockRatingValue;
	}
	public void setStockRatingValue(StockRatingValue stockRatingValue) {
		this.stockRatingValue = stockRatingValue;
	}
	
	public void addNames(StockExchange exchange, String stockName)
	{
		this.stockExchangeStocknameMap.put(exchange, stockName);
	}
	
	public Map<StockExchange, String> getStockExchangeStocknameMap() {
		return stockExchangeStocknameMap;
	}
	
	public float getInvestmentRatio() {
		return investmentRatio;
	}
	
	public void setInvestmentRatio(float investmentRatio) {
		this.investmentRatio = investmentRatio;
	}
	
	public void setIndustry(String industry) {
		this.industry=industry;
	}
	
	public String getIndustry() {
		return industry;
	}
	
	public void setIndustryInvestmentRatio(Float industryInvestmentRatio) {
		this.industryInvestmentRatio=industryInvestmentRatio;
	}
	
	public Float getIndustryInvestmentRatio() {
		return industryInvestmentRatio==null ? 0f : industryInvestmentRatio ;
	}
	
	public boolean isBlackListed() {
		return isBlackListed;
	}
	public void setBlackListed(boolean isBlackListed) {
		this.isBlackListed = isBlackListed;
	}
	
//	dummy method - because ndayhistorydbobject has this.
	public String getDuration() {
		return DateUtil.approxDurationInMonthsAndYears(this.getBuyDate());
	}
	
	public boolean isException()
	{
		return this.isException;
	}
	
	public float getImpactOnAverageReturn() {
		return impactOnAverageReturn;
	}
	
	public void setImpactOnAverageReturn(float impactOnAverageReturn) {
		this.impactOnAverageReturn = impactOnAverageReturn;
	}
	
	public StopLossDbObject getStopLossDbObject() {
		return stopLossDbObject;
	}
	
	public void setStopLossDbObject(StopLossDbObject stopLossDbObject) {
		this.stopLossDbObject = stopLossDbObject;
	}
	
	public boolean isReachedStopLossTarget() {
		return isReachedStopLossTarget;
	}
	
	public void setReachedStopLossTarget(boolean isReachedStopLossTarget) {
		this.isReachedStopLossTarget = isReachedStopLossTarget;
	}
	
	/*
	public float getTargetReturnPercent() {
		return targetReturnPercent;
	}
	
	public void setTargetReturnPercent(float targetReturnPercent) {
		this.targetReturnPercent = targetReturnPercent;
	}
	
	public float getTargetSellPrice() {
		return targetSellPrice;
	}
	
	public void setTargetSellPrice(float targetSellPrice) {
		this.targetSellPrice = targetSellPrice;
	}
	
	public String getTargetDate() {
		return targetDate;
	}
	
	public void setTargetDate(String targetDate) {
		this.targetDate = targetDate;
	}
	*/
	
	@Override
	public String toString()
	{
		return this.stockName+" Qty: "+this.getQuantity()+" Buy Date: "+this.getBuyDate()+" Buy Price: "+this.getBuyPrice();
	}

	
}
