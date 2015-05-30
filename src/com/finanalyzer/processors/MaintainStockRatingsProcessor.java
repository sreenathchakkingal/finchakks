package com.finanalyzer.processors;

import java.util.List;
import java.util.Map;
 






import com.finanalyzer.db.CoreDb;
import com.finanalyzer.db.RatingDb;
import com.finanalyzer.db.StockRatingsDb;
import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.StockRatingValue;
import com.finanalyzer.domain.StockRatingValuesEnum;
import com.gs.collections.impl.map.mutable.UnifiedMap;

public class MaintainStockRatingsProcessor implements Processor<Stock>
{

	private final String stockId;
	private final boolean isAddOrUpdateAction;
	private static final StockRatingsDb DB = new StockRatingsDb();
	private static final RatingDb RATINGS_DB = new RatingDb();
	Map<String, Integer> stockRatings = UnifiedMap.newMap();
	private Map<String, String[]> inputMap;

	public MaintainStockRatingsProcessor(String stockId, boolean isAddOrUpdateAction, Map map)
	{
		this.stockId=stockId;
		this.isAddOrUpdateAction=isAddOrUpdateAction;
		this.inputMap= map;
	}

	@Override
	public Stock execute()
	{
		if(this.stockId!=null )
		{
			Stock stock = new Stock(this.stockId);
			
			if(this.isAddOrUpdateAction)
			{
//				Map<String, Integer> map= getRatingToValueMap();
				Map<String, StockRatingValuesEnum> map= getRatingToValueMap();
				StockRatingValue stockRatingValue = new StockRatingValue(map);
				stock.setStockRatingValue(stockRatingValue);
				DB.updateEntry(stock);
				return stock;
			}
			else //retrieve
			{
				StockRatingValue ratingToValue = DB.getStockRatingValue(this.stockId);
				stock.setStockRatingValue(ratingToValue);
				return ratingToValue.getRatingToValue().isEmpty() ? createDummyMap() : stock;
			}
		}
		return createDummyMap();
	}

	private Map<String, StockRatingValuesEnum> getRatingToValueMap() {
		List<String> allRatings = RATINGS_DB.retrieveAllEntries();

		Map<String, StockRatingValuesEnum> ratingsToValue = UnifiedMap.newMap();

		for (String eachRating : allRatings)
		{
			String ratingValue = ((String[])this.inputMap.get(eachRating))[0];
			if(ratingValue!=null)
			{
				ratingsToValue.put(eachRating, StockRatingValuesEnum.getEnumForRatingDescription(ratingValue));	
			}
		}
		return ratingsToValue;
	}

	private Stock createDummyMap()
	{
		
		List<String> allRatings = RATINGS_DB.retrieveAllEntries();
		Map<String, StockRatingValuesEnum> ratingsToValue = UnifiedMap.newMap();

		for (String eachRating : allRatings)
		{
			ratingsToValue.put(eachRating, StockRatingValuesEnum.NOT_RATED);
		}
		
		Stock stock = new Stock(this.stockId);
		stock.setStockRatingValue(new StockRatingValue(ratingsToValue));
		return stock;
	}

}
