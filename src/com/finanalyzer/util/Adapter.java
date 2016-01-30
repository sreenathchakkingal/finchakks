package com.finanalyzer.util;

import java.util.List;

import com.finanalyzer.db.StockIdConverstionUtil;
import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.StockBuilder;
import com.finanalyzer.domain.StockExchange;
import com.finanalyzer.domain.StockRatingValue;
import com.finanalyzer.domain.jdo.DummyStockRatingValue;
import com.finanalyzer.domain.jdo.NDaysHistoryDbObject;
import com.finanalyzer.domain.jdo.UnrealizedDetailDbObject;
import com.finanalyzer.domain.jdo.UnrealizedSummaryDbObject;
import com.finanalyzer.domain.jdo.builder.UnrealizedDetailDbObjectBuilder;
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
			
			final UnrealizedSummaryDbObject unrealizedSummaryDbObject = 
					new UnrealizedSummaryDbObject(stockName, stock.getReturnTillDate(), stock.getQuantity(),
							stock.getTotalInvestment(), stock.getTotalReturn(), stock.getTotalReturnIfBank(), 
							stock.getIndustry(), stock.isBlackListed());
			
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
