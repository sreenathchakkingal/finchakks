package com.finanalyzer.db;

import java.util.Arrays;
import java.util.List;

import com.finanalyzer.domain.StockRatingValuesEnum;
import com.finanalyzer.util.StringUtil;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PropertyProjection;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.gs.collections.impl.list.mutable.FastList;
 
public class RatingDb extends CoreDb<String, String[]> {

	private static final String RATING = "rating";
	private static final String RATING_NAME = "rating_name";

	public RatingDb() 
	{
		super(RATING, FastList.newListWith(RATING_NAME));
	}

	@Override
	public String convertRowToDomainObject(Entity row) {
		return (String) row.getProperty(RATING_NAME);
	}

	@Override
	public Filter getFilterCriteriaForRemovingRecords(String[] ratingsToBeRemoved) {
		return new FilterPredicate(RATING_NAME, FilterOperator.IN, Arrays.asList(ratingsToBeRemoved));
	}

	@Override
	public Filter getFilterCriteriaForFetchingRecords(String[] entriesToBeFetched) {
		throw new RuntimeException("method not implemented!!");
	}
	
	@Override
	public Query getCascadeQueryForDeletion(String[]  ratingsToBeRemoved) { //default implementation 
		StockRatingsDb db = new StockRatingsDb();
		Filter selectedEntities = new FilterPredicate(db.RATING_NAME, FilterOperator.IN, Arrays.asList(ratingsToBeRemoved));
		Query query = new Query(db.getTableName()).setFilter(selectedEntities);
		return query;
	}
	
	@Override
	public void performCascadeAddition(List<String> ratingsToBeAdded)
	{
		if(ratingsToBeAdded.size()>1)
		{
			throw new RuntimeException("does not support more than one ratings to be added!!");
		}
		StockRatingsDb stockRatingsDb = new StockRatingsDb();
		Query query = new Query(stockRatingsDb.getTableName());
		query.addProjection(new PropertyProjection(stockRatingsDb.STOCK_ID, String.class));
		query.setDistinct(true);
		
		FastList<Entity> entities = this.getEntitiesForQuery(query);
		for(Entity entity : entities)
		{
			String stockName = (String) entity.getProperty(stockRatingsDb.STOCK_ID);
			stockRatingsDb.addEntry(FastList.newListWith(stockName,
					ratingsToBeAdded.get(0), 
					String.valueOf(StockRatingValuesEnum.NOT_RATED.getRating())));
		}
		
	}
}
