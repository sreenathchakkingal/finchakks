package com.finanalyzer.processors;

import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.db.jdo.PMF;
import com.finanalyzer.domain.ActionEnum;
import com.finanalyzer.domain.StockRatingValuesEnum;
import com.finanalyzer.domain.StockWrapperWithAllCalcResults;
import com.finanalyzer.domain.jdo.AllScripsDbObject;
import com.finanalyzer.domain.jdo.NDaysHistoryDbObject;
import com.finanalyzer.domain.jdo.RatingDbObject;
import com.finanalyzer.domain.jdo.UnrealizedDetailDbObject;
import com.finanalyzer.domain.jdo.UnrealizedSummaryDbObject;
import com.finanalyzer.util.StringUtil;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;

public class MaintainStockRatingsProcessor implements Processor<AllScripsDbObject>
{

	private final String stockId;
	private final ActionEnum action;
	Map<String, Integer> stockRatings = UnifiedMap.newMap();
	private Map<String, String[]> inputMap;

	private static final Predicate<AllScripsDbObject> IS_RATING_EXISTS = new Predicate<AllScripsDbObject>() {

		@Override
		public boolean accept(AllScripsDbObject allScripsDbObject) {
			return !allScripsDbObject.getRatingNameToValue().isEmpty();
		}
	};
	
	public MaintainStockRatingsProcessor(String stockId, ActionEnum action, Map map)
	{
		this.stockId=stockId;
		this.action=action;
		this.inputMap= map;
	}

	@Override
	public AllScripsDbObject execute()
	{
		if(StringUtil.isValidValue(this.stockId) )
		{
			PersistenceManager pm = null;
			AllScripsDbObject matchingScrip = null;
			try
			{
				pm = PMF.get().getPersistenceManager();	
				Query q = pm.newQuery(AllScripsDbObject.class, ":p.contains("+AllScripsDbObject.NSE_ID+")");
				List<AllScripsDbObject> matchingScrips = (List<AllScripsDbObject>)q.execute(FastList.newListWith(this.stockId));
				matchingScrip = matchingScrips.get(0);
				
				if(this.action==ActionEnum.UPDATE_STOCK_RATING)
				{
					Map<String, StockRatingValuesEnum> ratingNameToValue= getRatingToValuesFromClient();
					matchingScrip.setRatingNameToValue(ratingNameToValue);
				}
				else if (this.action==ActionEnum.ADD_TO_WATCHLIST)
				{
					new MaintainWatchListProcessor(FastList.newListWith(this.stockId), true, true).execute();
				}
				else if (this.action==ActionEnum.ADD_TO_BLACKLIST)
				{
					new MaintainBlackListProcessor(FastList.newListWith(this.stockId), true, true).execute();
				}
				
				else //retrieve
				{
					if(matchingScrip.getRatingNameToValue().isEmpty())
					{
						matchingScrip.setRatingNameToValue(createDummyMap());
					}
					
				}
			}
			finally
			{
				pm.close();
			}
			return matchingScrip;
		}
		AllScripsDbObject dummyObject = new AllScripsDbObject();
		dummyObject.setRatingNameToValue(createDummyMap());
		return dummyObject;
	}
	
	private Map<String, StockRatingValuesEnum> getRatingToValuesFromClient() 
	{
		Map<String, StockRatingValuesEnum> ratingsToValue = UnifiedMap.newMap();
		JdoDbOperations<RatingDbObject> dbOperations = new JdoDbOperations<RatingDbObject>(RatingDbObject.class);
		
		final List<RatingDbObject> ratingDbObjects = dbOperations.getEntries();
		
		for (RatingDbObject ratingDbObject : ratingDbObjects)
		{
			final String ratingName = ratingDbObject.getRatingName();
			String ratingValue = ((String[])this.inputMap.get(ratingName))[0];
			
			if(StringUtil.isValidValue(ratingValue))
			{
				ratingsToValue.put(ratingName, StockRatingValuesEnum.getEnumForRatingDescription(ratingValue));	
			}
		}
		
		return ratingsToValue;
	}

	
	private Map<String, StockRatingValuesEnum> createDummyMap()
	{
		JdoDbOperations<RatingDbObject> ratingDbOperations = new JdoDbOperations<RatingDbObject>(RatingDbObject.class);
		JdoDbOperations<AllScripsDbObject> allScripsDbObject = new JdoDbOperations<AllScripsDbObject>(AllScripsDbObject.class);
		
		final List<RatingDbObject> ratingDbObjects = ratingDbOperations.getEntries();

		Map<String, StockRatingValuesEnum> ratingsToValue = UnifiedMap.newMap();

		for (RatingDbObject ratingDbObject : ratingDbObjects)
		{
			ratingsToValue.put(ratingDbObject.getRatingName(),StockRatingValuesEnum.NOT_RATED);
		}
		
		return ratingsToValue;
	}

	public StockWrapperWithAllCalcResults getAllDetails(AllScripsDbObject allScripsDbObject) 
	{
		StockWrapperWithAllCalcResults stockWrapperWithAllCalcResults = new StockWrapperWithAllCalcResults();
		
		stockWrapperWithAllCalcResults.setAllScripsDbObject(allScripsDbObject);
		//Rating : 4/9
		
		JdoDbOperations<UnrealizedSummaryDbObject> unrealizedSummaryDbOperations = new JdoDbOperations<>(UnrealizedSummaryDbObject.class);
		final List<UnrealizedSummaryDbObject> unrealizedSummaryDbObjects = unrealizedSummaryDbOperations.getEntries("stockName", FastList.newListWith(allScripsDbObject.getMoneycontrolName()));
		if(unrealizedSummaryDbObjects!=null && unrealizedSummaryDbObjects.size()==1)
		{
			stockWrapperWithAllCalcResults.setUnrealizedSummaryDbObjects(unrealizedSummaryDbObjects);
		}
		
		JdoDbOperations<NDaysHistoryDbObject>  nDaysHistoryDbOperations = new JdoDbOperations<>(NDaysHistoryDbObject.class);
		final List<NDaysHistoryDbObject> ndaysHistoryDbObjects =nDaysHistoryDbOperations.getEntries("stockName", FastList.newListWith(allScripsDbObject.getNseId()));
		if(ndaysHistoryDbObjects!=null && ndaysHistoryDbObjects.size()==1)
		{
			stockWrapperWithAllCalcResults.setnDaysHistoryDbObjects(ndaysHistoryDbObjects);
		}		
		
		
		JdoDbOperations<UnrealizedDetailDbObject> unrealizedDetailDbOperations = new JdoDbOperations<>(UnrealizedDetailDbObject.class);
		final List<UnrealizedDetailDbObject> unrealizedDetailObjects = unrealizedDetailDbOperations.getEntries("stockName", FastList.newListWith(allScripsDbObject.getMoneycontrolName()));
		if(unrealizedDetailObjects!=null && unrealizedDetailObjects.size()!=0)
		{
			stockWrapperWithAllCalcResults.setUnrealizedDetailDbObjects(unrealizedDetailObjects);
		}	
		
		return stockWrapperWithAllCalcResults;
		
		//Buy Summary
		//Buy Details
	}


}
