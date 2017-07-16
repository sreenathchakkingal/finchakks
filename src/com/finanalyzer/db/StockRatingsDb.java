package com.finanalyzer.db;

import java.util.List;
import java.util.Map;

import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.StockRatingValue;
import com.finanalyzer.domain.StockRatingValuesEnum;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;

public class StockRatingsDb extends CoreDb<Stock, String>
{
	private static final String RATING_VALUE = "RATING_VALUE";
	static final String RATING_NAME = "RATING_NAME";
	public static final String STOCK_ID = "STOCK_ID";

	public StockRatingsDb() {
		super("STOCKRATING", FastList.newListWith(STOCK_ID, RATING_NAME, RATING_VALUE));
	}

	@Override
	public Stock convertRowToDomainObject(Entity row) {
		throw new RuntimeException("method not implemented!!");
	}
	
	public StockRatingValue getStockRatingValue(String stockName)
	{
		List<Entity> rows = this.retrieveSelectedEntities(stockName);
		Map<String, StockRatingValuesEnum> ratingToValue = UnifiedMap.newMap();
		for (Entity eachRow : rows)
		{
			String ratingName = (String) eachRow.getProperty(RATING_NAME);
			Integer ratingValue = Integer.parseInt((String) eachRow.getProperty(RATING_VALUE));
			ratingToValue.put(ratingName, StockRatingValuesEnum.getEnumForRatingValue(ratingValue));
		}
		return new StockRatingValue(ratingToValue);
	}
	
	public List<Stock> stampStockRatingValue(List<Stock> stocks) 
	{
		
		List<Entity> rows = this.freshRetrieveAllEntities();
		
		for (Stock stock : stocks)
		{
			Map<String, StockRatingValuesEnum> ratingToValue = UnifiedMap.newMap();
			String stockName = stock.getStockName();
			for (Entity eachRow : rows)
			{
				String stockId = (String) eachRow.getProperty(STOCK_ID);
				if(stockName.equalsIgnoreCase(stockId))
				{
					String ratingName = (String) eachRow.getProperty(RATING_NAME);
					Integer ratingValue = Integer.parseInt((String) eachRow.getProperty(RATING_VALUE));
					ratingToValue.put(ratingName, StockRatingValuesEnum.getEnumForRatingValue(ratingValue));	
				}
			}
			stock.setStockRatingValue(new StockRatingValue(ratingToValue));
		}
		return stocks;
	}

	@Override
	public Filter getFilterCriteriaForFetchingRecords(String stockName) {
		return new FilterPredicate(STOCK_ID, FilterOperator.EQUAL, stockName);
	}
	
	@Override
	public Filter getFilterCriteriaForRemovingRecords(String stockName) {
		return new FilterPredicate(STOCK_ID, FilterOperator.EQUAL, stockName);
	}

	public void updateEntry(Stock stock) {
		removeEntries(stock.getStockName());
		
		Map<String, StockRatingValuesEnum> ratingToValue = stock.getStockRatingValue().getRatingToValue();
		for (Map.Entry<String, StockRatingValuesEnum> eachEntry : ratingToValue.entrySet())
		
		{
			List<String> columnValues = FastList.newListWith(
					stock.getStockName(), 
					eachEntry.getKey(),
					String.valueOf(eachEntry.getValue().getRating()));
			addEntry(columnValues);
		}
		
		stock.setStockRatingValue(this.getStockRatingValue(stock.getStockName()));
	}

}

