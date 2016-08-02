package com.finanalyzer.util;

import java.util.Collection;
import java.util.List;

import com.finanalyzer.db.StockIdConverstionUtil;
import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.StockExchange;
import com.finanalyzer.domain.StockRatingValue;
import com.finanalyzer.domain.builder.NDaysHistoryFlattenedDbObjectBuilder;
import com.finanalyzer.domain.builder.StockBuilder;
import com.finanalyzer.domain.builder.UnrealizedDetailDbObjectBuilder;
import com.finanalyzer.domain.builder.UnrealizedSummaryDbObjectBuilder;
import com.finanalyzer.domain.jdo.DummyStockRatingValue;
import com.finanalyzer.domain.jdo.NDaysHistoryDbObject;
import com.finanalyzer.domain.jdo.NDaysHistoryFlattenedDbObject;
import com.finanalyzer.domain.jdo.UnrealizedDetailDbObject;
import com.finanalyzer.domain.jdo.UnrealizedSummaryDbObject;
import com.gs.collections.impl.list.mutable.FastList;

public class Adapter {
	
	public static List<NDaysHistoryDbObject> stockToNdaysHistoryDbObject(List<Stock> stocks)
	{
		List<NDaysHistoryDbObject> nDaysHistoryDbObjects = FastList.newList();
		for (Stock stock : stocks)
		{
			
			NDaysHistoryDbObject daysHistoryDbObject = new NDaysHistoryDbObject(
					stock.getStockName(), stock.getStockExchangeStocknameMap().get(StockExchange.MONEY_CONTROL),
					Adapter.stockRatingValueToDummyStockRatingValue(stock.getStockRatingValue()),
					stock.getInvestmentRatio(), stock.getIndustryInvestmentRatio(), 
					stock.getSellPrice(), stock.getSimpleMovingAverage(), stock.getSimpleMovingAverageAndSellDeltaNormalized(),
					stock.getNetNDaysGain(), stock.getIndustry(), 
					stock.getDates(), stock.getnDaysGains());	
			
			nDaysHistoryDbObjects.add(daysHistoryDbObject);
		}
		
		return nDaysHistoryDbObjects;
	}
	
	public static List<NDaysHistoryFlattenedDbObject> stockToNDaysHistoryFlattenedDbObject(List<Stock> stocks)
	{
		List<NDaysHistoryFlattenedDbObject> nDaysHistoryFlattenedDbObjects = FastList.newList();
		for (Stock stock : stocks)
		{
			List<Float> values = FastList.newList(stock.getnDaysGains().values());
			float nDay1Gain = values.get(0);
			float nDay2Gain = values.get(1);
			float nDay3Gain = values.get(3);
			float nDay4Gain = values.get(4);
			float nDay5Gain = values.get(5);
			float nDay6Gain = values.get(6);

			NDaysHistoryFlattenedDbObjectBuilder builder = new NDaysHistoryFlattenedDbObjectBuilder()
			.stockName(stock.getStockName())
			.stockRatingValue(stock.getStockRatingValue().getScore())
			.investmentRatio(stock.getInvestmentRatio())
			.industryInvestmentRatio(stock.getIndustryInvestmentRatio())
			.sellPrice(stock.getSellPrice())
			.simpleMovingAverage(stock.getSimpleMovingAverage())
			.simpleMovingAverageAndSellDeltaNormalized(stock.getSimpleMovingAverageAndSellDeltaNormalized())
			.netNDaysGain(stock.getNetNDaysGain())
			.industry(stock.getIndustry())
			.nDay1Gain(nDay1Gain)
			.nDay2Gain(nDay2Gain)
			.nDay3Gain(nDay3Gain)
			.nDay4Gain(nDay4Gain)
			.nDay5Gain(nDay5Gain)
			.nDay6Gain(nDay6Gain)
			;
			
			NDaysHistoryFlattenedDbObject nDaysHistoryFlattenedDbObject = builder.build();
			nDaysHistoryFlattenedDbObjects.add(nDaysHistoryFlattenedDbObject);
		}
		
		return nDaysHistoryFlattenedDbObjects;
	}
	
	
	
	private static DummyStockRatingValue stockRatingValueToDummyStockRatingValue(StockRatingValue stockRatingValue)
	{
		DummyStockRatingValue  dummyStockRatingValue = new DummyStockRatingValue(stockRatingValue.getScore());
		return dummyStockRatingValue;
	}

	public static List<UnrealizedSummaryDbObject> stockToUnrealizedSummaryDbObject(List<Stock> stocksSummary) {
		
		List<UnrealizedSummaryDbObject> unrealizedSummaryDbObjects = FastList.newList();
		
		for (Stock stock : stocksSummary)
		{
			
			final String stockName = stock.getStockName();
			
			final UnrealizedSummaryDbObjectBuilder unrealizedSummaryBuilder = new UnrealizedSummaryDbObjectBuilder(); 
			
			final UnrealizedSummaryDbObject unrealizedSummaryDbObject = 
			unrealizedSummaryBuilder
			.stockName(stockName)
			.returnTillDate(stock.getReturnTillDate())
			.quantity(stock.getQuantity())
			.sellableQuantity(stock.getSellableQuantity())
			.totalInvestment(stock.getTotalInvestment())
			.totalReturn(stock.getTotalReturn())
			.totalReturnIfBank(stock.getTotalReturnIfBank())
			.impactOnAverageReturn(stock.getImpactOnAverageReturn())
			.industry(stock.getIndustry())
			.isBlackListed(stock.isBlackListed()).build();
			
			unrealizedSummaryDbObjects.add(unrealizedSummaryDbObject);
		}
		
		return unrealizedSummaryDbObjects;
	}
	

	public static List<UnrealizedDetailDbObject> stockToUnrealizedDetailDbObject(List<Stock> stocks) {
		
		List<UnrealizedDetailDbObject> unrealizedSummaryDbObjects = FastList.newList();
		
		for (Stock stock : stocks)
		{
			
			final UnrealizedDetailDbObjectBuilder builder = new UnrealizedDetailDbObjectBuilder();
			final UnrealizedDetailDbObject unrealizedDetailDbObject = builder
			.sellableQuantity(stock.getSellableQuantity())
			.isMoreThanAnYear(stock.getIsMoreThanAnYear())
			.isMaturityIsCloseToAnYear(stock.getIsMaturityIsCloseToAnYear())
			.stockName(stock.getStockName())
			.returnTillDate(stock.getReturnTillDate())
			.buyDate(stock.getBuyDate())
			.buyPrice(stock.getBuyPrice())
			.sellPrice(stock.getSellPrice())
			.bankSellPrice(stock.getBankSellPrice())
			.quantity(stock.getQuantity())
			.duration(DateUtil.approxDurationInMonthsAndYears(stock.getBuyDate()))
			.build();
			
			unrealizedSummaryDbObjects.add(unrealizedDetailDbObject);
		}
		
		return unrealizedSummaryDbObjects;
	}


}
