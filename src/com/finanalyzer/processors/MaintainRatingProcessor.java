package com.finanalyzer.processors;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.finanalyzer.db.CoreDb;
import com.finanalyzer.db.RatingDb;
import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.db.jdo.PMF;
import com.finanalyzer.domain.StockRatingValuesEnum;
import com.finanalyzer.domain.jdo.AllScripsDbObject;
import com.finanalyzer.domain.jdo.RatingDbObject;
import com.finanalyzer.util.StringUtil;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.impl.list.mutable.FastList;

public class MaintainRatingProcessor implements Processor<List<String>>
{

	private final String rating;
	private final String[] ratingsToBeRemoved;
	
	private static final Function<RatingDbObject, String> RATING_NAME_COLLECTOR = new Function<RatingDbObject, String>() {

		@Override
		public String valueOf(RatingDbObject dbObject) {
			return dbObject.getRatingName();
		}
	};
	
	public MaintainRatingProcessor(String ratingToBeAdded, String[] ratingsToBeRemoved)
	{
		this.rating = ratingToBeAdded;
		this.ratingsToBeRemoved = ratingsToBeRemoved;
	}

	@Override
	public List<String> execute()
	{
		JdoDbOperations<RatingDbObject> dbOperations = new JdoDbOperations<RatingDbObject>(RatingDbObject.class);
		
		if(StringUtil.isValidValue(this.rating))
		{
			dbOperations.insertEntry(new RatingDbObject(this.rating));
			updateAllScripsWithAddedRating();
		}
		
		if (StringUtil.isValidValue(ratingsToBeRemoved)) 
		{
			dbOperations.deleteEntries("ratingName", Arrays.asList(ratingsToBeRemoved));
			removeRatingFromAllScrips();
		}
		
		final FastList<RatingDbObject> ratingDbObjects = FastList.newList(dbOperations.getEntries());

		return ratingDbObjects.collect(RATING_NAME_COLLECTOR);
	}

	private void removeRatingFromAllScrips() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try
		{
			Query q = pm.newQuery(AllScripsDbObject.class);
			final FastList<AllScripsDbObject> allScripsDbObjects = FastList.newList((List<AllScripsDbObject>)q.execute());
			final FastList<AllScripsDbObject> entriesWithRatings = allScripsDbObjects.select(new Predicate<AllScripsDbObject>() {

				@Override
				public boolean accept(AllScripsDbObject allScripsDbObject) {
					
					return allScripsDbObject.getRatingNameToValue()!=null && !allScripsDbObject.getRatingNameToValue().isEmpty();
				}
			});
			
			for (AllScripsDbObject allScripsDbObject : entriesWithRatings)
			{
				final Map<String, StockRatingValuesEnum> ratingNameToValue = allScripsDbObject.getRatingNameToValue();
				for (String ratingToBeRemoved : this.ratingsToBeRemoved)
				{
					ratingNameToValue.remove(ratingToBeRemoved)	;
				}
				allScripsDbObject.setRatingNameToValue(ratingNameToValue);
			}
		}
		finally
		{
			pm.close();				
		}
	}

	private void updateAllScripsWithAddedRating() 
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try
		{
			Query q = pm.newQuery(AllScripsDbObject.class);
			final FastList<AllScripsDbObject> allScripsDbObjects = FastList.newList((List<AllScripsDbObject>)q.execute());
			final FastList<AllScripsDbObject> entriesWithRatings = allScripsDbObjects.select(new Predicate<AllScripsDbObject>() {

				@Override
				public boolean accept(AllScripsDbObject allScripsDbObject) {
					
					return allScripsDbObject.getRatingNameToValue()!=null && !allScripsDbObject.getRatingNameToValue().isEmpty();
				}
			});
			
			for (AllScripsDbObject allScripsDbObject : entriesWithRatings)
			{
				final Map<String, StockRatingValuesEnum> ratingNameToValue = allScripsDbObject.getRatingNameToValue();
				ratingNameToValue.put(this.rating, StockRatingValuesEnum.NOT_RATED);
				allScripsDbObject.setRatingNameToValue(ratingNameToValue);
			}
		}
		finally
		{
			pm.close();				
		}
		
	}
	
//	@Override
//	public List<String> execute()
//	{
//		CoreDb<String, String[]> ratingDb = new RatingDb();
//		if (StringUtil.isValidValue(ratingsToBeRemoved)) {
//			ratingDb.removeEntries(this.ratingsToBeRemoved);
//			
//		}
//		if (StringUtil.isValidValue(this.rating)) {
//			ratingDb.addEntry(FastList.newListWith(this.rating));
//		}
//		return ratingDb.retrieveAllEntries();
//	}

}