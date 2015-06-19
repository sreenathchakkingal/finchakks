package com.finanalyzer.processors;

import java.util.List;
import java.util.Map;
 















import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.finanalyzer.db.CoreDb;
import com.finanalyzer.db.RatingDb;
import com.finanalyzer.db.StockRatingsDb;
import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.db.jdo.PMF;
import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.StockRatingValue;
import com.finanalyzer.domain.StockRatingValuesEnum;
import com.finanalyzer.domain.jdo.AllScripsDbObject;
import com.finanalyzer.domain.jdo.RatingDbObject;
import com.finanalyzer.util.StringUtil;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;

public class MaintainStockRatingsProcessor implements Processor<AllScripsDbObject>
{

	private final String stockId;
	private final boolean isAddOrUpdateAction;
	private static final StockRatingsDb DB = new StockRatingsDb();
	private static final RatingDb RATINGS_DB = new RatingDb();
	Map<String, Integer> stockRatings = UnifiedMap.newMap();
	private Map<String, String[]> inputMap;

	private static final Predicate<AllScripsDbObject> IS_RATING_EXISTS = new Predicate<AllScripsDbObject>() {

		@Override
		public boolean accept(AllScripsDbObject allScripsDbObject) {
			return !allScripsDbObject.getRatingNameToValue().isEmpty();
		}
	};
	
	public MaintainStockRatingsProcessor(String stockId, boolean isAddOrUpdateAction, Map map)
	{
		this.stockId=stockId;
		this.isAddOrUpdateAction=isAddOrUpdateAction;
		this.inputMap= map;
	}

	@Override
	public AllScripsDbObject execute()
	{
		if(StringUtil.isValidValue(this.stockId) )
		{
			if(this.isAddOrUpdateAction)
			{
				PersistenceManager pm = PMF.get().getPersistenceManager();
				AllScripsDbObject matchingScrip=null;
				try
				{
					Map<String, StockRatingValuesEnum> ratingNameToValue= getRatingToValuesFromClient();
					Query q = pm.newQuery(AllScripsDbObject.class, ":p.contains("+"nseId"+")");
					List<AllScripsDbObject> matchingScrips = (List<AllScripsDbObject>)q.execute(FastList.newListWith(this.stockId));
					matchingScrips.get(0).setRatingNameToValue(ratingNameToValue);
				}
				finally
				{
					pm.close();
				}
				return matchingScrip;
			}
			else //retrieve
			{
				JdoDbOperations<AllScripsDbObject> dbOperations = new JdoDbOperations<AllScripsDbObject>(AllScripsDbObject.class);
				final List<AllScripsDbObject> matchingScrips = FastList.newList(dbOperations.getEntries("nseId", this.stockId));
				final AllScripsDbObject matchingScrip = matchingScrips.get(0);
				return matchingScrip.getRatingNameToValue().isEmpty() ? createDummyMap() : matchingScrip;
			}
		}
		return createDummyMap();
	}
	
//	@Override
//	public Stock execute()
//	{
//		if(this.stockId!=null )
//		{
//			Stock stock = new Stock(this.stockId);
//			
//			if(this.isAddOrUpdateAction)
//			{
//				Map<String, StockRatingValuesEnum> map= getRatingToValueMap();
//				StockRatingValue stockRatingValue = new StockRatingValue(map);
//				stock.setStockRatingValue(stockRatingValue);
//				DB.updateEntry(stock);
//				return stock;
//			}
//			else //retrieve
//			{
//				StockRatingValue ratingToValue = DB.getStockRatingValue(this.stockId);
//				stock.setStockRatingValue(ratingToValue);
//				return ratingToValue.getRatingToValue().isEmpty() ? createDummyMap() : stock;
//			}
//		}
//		return createDummyMap();
//	}

//	private Map<String, StockRatingValuesEnum> getRatingToValueMap() {
//		List<String> allRatings = RATINGS_DB.retrieveAllEntries();
//
//		Map<String, StockRatingValuesEnum> ratingsToValue = UnifiedMap.newMap();
//
//		for (String eachRating : allRatings)
//		{
//			String ratingValue = ((String[])this.inputMap.get(eachRating))[0];
//			if(ratingValue!=null)
//			{
//				ratingsToValue.put(eachRating, StockRatingValuesEnum.getEnumForRatingDescription(ratingValue));	
//			}
//		}
//		return ratingsToValue;
//	}

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

	
	private AllScripsDbObject createDummyMap()
	{
		JdoDbOperations<RatingDbObject> dbOperations = new JdoDbOperations<RatingDbObject>(RatingDbObject.class);
		final List<RatingDbObject> ratingDbObjects = dbOperations.getEntries();

		Map<String, StockRatingValuesEnum> ratingsToValue = UnifiedMap.newMap();

		for (RatingDbObject ratingDbObject : ratingDbObjects)
		{
			ratingsToValue.put(ratingDbObject.getRatingName(),StockRatingValuesEnum.NOT_RATED);
		}
		return new AllScripsDbObject(this.stockId, ratingsToValue);
	}

	
//	private Stock createDummyMap()
//	{
//		
//		List<String> allRatings = RATINGS_DB.retrieveAllEntries();
//		Map<String, StockRatingValuesEnum> ratingsToValue = UnifiedMap.newMap();
//
//		for (String eachRating : allRatings)
//		{
//			ratingsToValue.put(eachRating, StockRatingValuesEnum.NOT_RATED);
//		}
//		
//		Stock stock = new Stock(this.stockId);
//		stock.setStockRatingValue(new StockRatingValue(ratingsToValue));
//		return stock;
//	}

}
