package com.finanalyzer.util;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.domain.ModifiableStockAttributes;
import com.finanalyzer.domain.RatingObjectForUi;
import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.StockExchange;
import com.finanalyzer.domain.StockRatingValue;
import com.finanalyzer.domain.StockRatingValuesEnum;
import com.finanalyzer.domain.builder.NDaysHistoryFlattenedDbObjectBuilder;
import com.finanalyzer.domain.builder.ProfitAndLossBuilder;
import com.finanalyzer.domain.builder.UnrealizedDetailDbObjectBuilder;
import com.finanalyzer.domain.builder.UnrealizedSummaryDbObjectBuilder;
import com.finanalyzer.domain.builder.UnrealizedSummaryDiffDbObjectBuilder;
import com.finanalyzer.domain.jdo.AllScripsDbObject;
import com.finanalyzer.domain.jdo.DummyStockRatingValue;
import com.finanalyzer.domain.jdo.NDaysHistoryDbObject;
import com.finanalyzer.domain.jdo.NDaysHistoryFlattenedDbObject;
import com.finanalyzer.domain.jdo.ProfitAndLossDbObject;
import com.finanalyzer.domain.jdo.RatingDbObject;
import com.finanalyzer.domain.jdo.StopLossDbObject;
import com.finanalyzer.domain.jdo.UnrealizedDbObject;
import com.finanalyzer.domain.jdo.UnrealizedDetailDbObject;
import com.finanalyzer.domain.jdo.UnrealizedSummaryDbObject;
import com.finanalyzer.domain.jdo.UnrealizedSummaryDiffDbObject;
import com.gs.collections.impl.list.mutable.FastList;

public class ConverterUtil {
	
	private static final Logger LOG = Logger.getLogger(ConverterUtil.class.getName());
	private static final float ZERO  = 0.0f;
	public static final String DELIMITER_IN_UNREALIZED_UPLOAD = ",";
	
	public static List<NDaysHistoryDbObject> stockToNdaysHistoryDbObject(List<Stock> stocks)
	{
		List<NDaysHistoryDbObject> nDaysHistoryDbObjects = FastList.newList();
		for (Stock stock : stocks)
		{
			
			NDaysHistoryDbObject daysHistoryDbObject = new NDaysHistoryDbObject(
					stock.getStockName(), stock.getStockExchangeStocknameMap().get(StockExchange.MONEY_CONTROL),
					ConverterUtil.stockRatingValueToDummyStockRatingValue(stock.getStockRatingValue()),
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
			
			LOG.info("processing stock: "+stock.getStockName());
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
			.minValue(stock.getMinValue())
			.minValueDate(stock.getMinValueDate())
			.isLatestClosePriceMinimum(stock.isLatestClosePriceMinimum())
			.maxValue(stock.getMaxValue())
			.maxValueDate(stock.getMaxValueDate())
			.isLatestClosePriceMaximum(stock.isLatestClosePriceMaximum());
			
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
			final StopLossDbObject stopLossDbObject = stock.getStopLossDbObject();
			LOG.info("converting stock: "+stock.getStockName()+"to  UnrealizedDetailDbObject. stopLossDbObject: "+stopLossDbObject);
			
			final UnrealizedSummaryDbObjectBuilder unrealizedSummaryBuilder = new UnrealizedSummaryDbObjectBuilder(); 
			
			final UnrealizedSummaryDbObject unrealizedSummaryDbObject = 
			unrealizedSummaryBuilder
			.stockName(stock.getStockName())
			.returnTillDate(stock.getReturnTillDate())
			.quantity(stock.getQuantity())
			.sellableQuantity(stock.getSellableQuantity())
			.sellPrice(stock.getSellPrice())
			.totalInvestment(stock.getTotalInvestment())
			.totalReturn(stock.getTotalReturn())
			.totalReturnIfBank(stock.getTotalReturnIfBank())
			.impactOnAverageReturn(stock.getImpactOnAverageReturn())
			.industry(stock.getIndustry())
			.isBlackListed(stock.isBlackListed())
			.diff(stock.getDiff())
			.lowerReturnPercentTarget(stopLossDbObject==null ? ZERO : stopLossDbObject.getLowerReturnPercentTarget())
			.upperReturnPercentTarget(stopLossDbObject==null ? ZERO : stopLossDbObject.getUpperReturnPercentTarget())
			.lowerSellPriceTarget(stopLossDbObject==null ? ZERO : stopLossDbObject.getLowerSellPriceTarget())
			.upperSellPriceTarget(stopLossDbObject==null ? ZERO : stopLossDbObject.getUpperSellPriceTarget())
			.achieveAfterDate(stopLossDbObject==null ? null : stopLossDbObject.getAchieveAfterDate())
			.achieveByDate(stopLossDbObject==null ? null : stopLossDbObject.getAchieveByDate())
			.isTargetReached(stock.isReachedStopLossTarget())
			.applicableDate(DateUtil.todaysDate())
			.build();
			
			LOG.info("conversion complete for stock: "+stock.getStockName()+"to  UnrealizedSummaryDbObject");
			unrealizedSummaryDbObjects.add(unrealizedSummaryDbObject);
		}
		
		return unrealizedSummaryDbObjects;
	}
	

	public static List<UnrealizedDetailDbObject> stockToUnrealizedDetailDbObject(List<Stock> stocks) {
		
		List<UnrealizedDetailDbObject> unrealizedDetailDbObjects = FastList.newList();
		
		for (Stock stock : stocks)
		{
			
			final UnrealizedDetailDbObjectBuilder builder = new UnrealizedDetailDbObjectBuilder();
			final UnrealizedDetailDbObject unrealizedDetailDbObject = builder
			.sellableQuantity(stock.getSellableQuantity())
			.isMoreThanAnYear(stock.getIsMoreThanAnYear())
			.isMaturityIsCloseToAnYear(stock.getIsMaturityIsCloseToAnYear())
			.stockName(stock.getStockName(StockExchange.NSE))
			.returnTillDate(stock.getReturnTillDate())
			.buyDate(stock.getBuyDate())
			.buyPrice(stock.getBuyPrice())
			.sellPrice(stock.getSellPrice())
			.bankSellPrice(stock.getBankSellPrice())
			.quantity(stock.getQuantity())
			.duration(DateUtil.approxDurationInMonthsAndYears(stock.getBuyDate()))
			.totalInvestment(stock.getTotalInvestment())
			.totalReturn(stock.getTotalReturn())
			.totalReturnIfBank(stock.getTotalReturnIfBank())
			.diff(stock.getDiff())
			.build();
			
			unrealizedDetailDbObjects.add(unrealizedDetailDbObject);
		}
		
		return unrealizedDetailDbObjects;
	}

	public static ModifiableStockAttributes getModifiableStockAttributes(
			AllScripsDbObject allScripsDbObject,
			StopLossDbObject stopLossDbObject) 
	{
		final Map<String, StockRatingValuesEnum> ratingNameToValue = allScripsDbObject.getRatingNameToValue();
		
		List<RatingObjectForUi> ratingObjects = ConverterUtil.ratingNameToValueToRatingObject(ratingNameToValue);
		
		final float lowerReturnPercentTarget = stopLossDbObject==null ? 0.0f : stopLossDbObject.getLowerReturnPercentTarget();
		
		final float upperReturnPercentTarget = stopLossDbObject==null ? 0.0f : stopLossDbObject.getUpperReturnPercentTarget();
		
		return new ModifiableStockAttributes(allScripsDbObject.getNseId(),allScripsDbObject.getMoneycontrolName(),
				allScripsDbObject.isWatchListed(), lowerReturnPercentTarget, 
				upperReturnPercentTarget, ratingObjects);
	}

	public static List<RatingObjectForUi> ratingNameToValueToRatingObject(
													Map<String, StockRatingValuesEnum>	ratingNameToValue)
	{
		LOG.info("ratingNameToValue: "+ratingNameToValue);
		List<RatingObjectForUi> ratingObjects = FastList.newList();
		RatingObjectForUi ratingObjectForUi;
		
		JdoDbOperations<RatingDbObject> operations = new JdoDbOperations<RatingDbObject>(RatingDbObject.class);
		final List<RatingDbObject> ratings = operations.getEntries();
		for (RatingDbObject rating : ratings)
		{
			LOG.info("rating: "+rating.getRatingName());
			final StockRatingValuesEnum persitedRating = ratingNameToValue.get(rating.getRatingName());
			LOG.info("persitedRating: "+persitedRating);
			if(persitedRating!=null)
			{
				LOG.info("persitedRating!=null");
				ratingObjectForUi = new RatingObjectForUi(rating.getRatingName(), persitedRating.getDescription());
			}
			else
			{
				LOG.info("persitedRating==null");
				ratingObjectForUi = new RatingObjectForUi(rating.getRatingName(), StockRatingValuesEnum.NOT_RATED.getDescription());
			}
			
			ratingObjects.add(ratingObjectForUi);
		}
		return ratingObjects;
	}

	public static List<UnrealizedDbObject> convertMoneyControlDownloadToUnrealizedDbObjects(
			List<String> rawDataDFromMoneyControl) {
		
		List<UnrealizedDbObject> unrealizedDbObjects = FastList.newList(); 

		for (String row : rawDataDFromMoneyControl)
		{
			String[] stockAttributes = ReaderUtil.removeCommanBetweenQuotes(row).split(DELIMITER_IN_UNREALIZED_UPLOAD);

			String name = ReaderUtil.parseStockName(stockAttributes[0]);
			
			int quantityColumnPosition = 5;
			int buyQuantity =  Integer.valueOf(stockAttributes[quantityColumnPosition]).intValue();

			int invoicePriceColumnPosition =  6;
			float buyPrice = Float.valueOf(stockAttributes[invoicePriceColumnPosition]);

			int invoiceDateColumnPosition = 7;
			String invoiceDate=stockAttributes[invoiceDateColumnPosition];

			String buyDate;
			if(invoiceDate.contains("/"))
			{
				buyDate = DateUtil.convertToStandardFormat("d/M/yyyy",invoiceDate);
			}
			else
			{
				buyDate = DateUtil.convertToStandardFormat("dd-MM-yyyy",invoiceDate);
			}
			unrealizedDbObjects.add(new UnrealizedDbObject(name, buyDate, buyPrice, buyQuantity));
		}
		return unrealizedDbObjects;
	}

	
	public static List<UnrealizedSummaryDiffDbObject> convertToSummaryDbObjectDiff(
			List<UnrealizedSummaryDbObject> currentEntries,List<UnrealizedSummaryDbObject> prevDayEntries) 
	{
		final List<UnrealizedSummaryDiffDbObject> diffEntries = FastList.newList();
		//abs diff both
		boolean isFoundInBoth=false;
		UnrealizedSummaryDiffDbObject diffEntry=null;
		
		for(UnrealizedSummaryDbObject current : currentEntries)
		{
			isFoundInBoth=false;
			for (UnrealizedSummaryDbObject prev : prevDayEntries)
			{
				if(current.getStockName().equals(prev.getStockName()))
				{
					isFoundInBoth=true;
					final float diff = current.getReturnTillDate()-prev.getReturnTillDate();
					
					diffEntry = new UnrealizedSummaryDiffDbObjectBuilder().
							stockName(current.getStockName()).
							currReturnTillDate(current.getReturnTillDate()).
							prevReturnTillDate(prev.getReturnTillDate()).
							absoluteDiffReturnTillDate(Math.abs(diff)).
							diffReturnTillDate(diff).
							build();
					break;
				}
			}
			
			if(!isFoundInBoth)
			{
				diffEntry = new UnrealizedSummaryDiffDbObjectBuilder().
						stockName(current.getStockName()).
						currReturnTillDate(current.getReturnTillDate()).
						diffReturnTillDate(Math.abs(current.getReturnTillDate())).
						build();
			}
			
			diffEntries.add(diffEntry);
		}
		return diffEntries;
	}


	public static List<UnrealizedSummaryDiffDbObject> convertToSummaryDbObjectDiffRetainPrev(
			List<UnrealizedSummaryDbObject> currentEntries,
			List<UnrealizedSummaryDiffDbObject> prevDiffEntries) 
	{
		final List<UnrealizedSummaryDiffDbObject> diffEntries = FastList.newList();

		boolean isFoundInBoth=false;
		UnrealizedSummaryDiffDbObject diffEntry=null;
		
		for(UnrealizedSummaryDbObject current : currentEntries)
		{
			isFoundInBoth=false;
			for (UnrealizedSummaryDiffDbObject prev : prevDiffEntries)
			{
				if(current.getStockName().equals(prev.getStockName()))
				{
					isFoundInBoth=true;
					final float diff = current.getReturnTillDate()-prev.getPrevReturnTillDate();
					
					diffEntry = new UnrealizedSummaryDiffDbObjectBuilder().
							stockName(current.getStockName()).
							currReturnTillDate(current.getReturnTillDate()).
							prevReturnTillDate(prev.getPrevReturnTillDate()).
							absoluteDiffReturnTillDate(Math.abs(diff)).
							diffReturnTillDate(diff).
							build();
					break;
				}
			}
			
			if(!isFoundInBoth)
			{
				diffEntry = new UnrealizedSummaryDiffDbObjectBuilder().
						stockName(current.getStockName()).
						currReturnTillDate(current.getReturnTillDate()).
						diffReturnTillDate(Math.abs(current.getReturnTillDate())).
						build();
			}
			
			diffEntries.add(diffEntry);
		}
		
		return diffEntries;
	}
	
	
	public static ProfitAndLossDbObject convertToProfitAndLossObject(ProfitAndLossDbObject prevProfitAndLossDbObject,
			ProfitAndLossDbObject profitAndLoss) 
	{
		final ProfitAndLossBuilder profitAndLossBuilder = new ProfitAndLossBuilder()
		.averageReturn(profitAndLoss.getAverageReturn())
		.totalInvestment(profitAndLoss.getTotalInvestment())
		.totalReturn(profitAndLoss.getTotalReturn())
		.totalReturnIfBank(profitAndLoss.getTotalReturnIfBank())
		.totalReturnVsIfBank(profitAndLoss.getTotalReturnVsIfBank());
		
		if(prevProfitAndLossDbObject!=null)
		{
			profitAndLossBuilder.prevAverageReturn(prevProfitAndLossDbObject.getAverageReturn())
			.prevTotalReturn(prevProfitAndLossDbObject.getTotalReturn())
			.prevTotalReturnVsIfBank(prevProfitAndLossDbObject.getTotalReturnVsIfBank());
		}
		return profitAndLossBuilder
		.diffInCurrentAndPrevAverageReturn()
		.diffInCurrentAndPrevTotalReturn().build();
		
	}


}
