package com.finanalyzer.domain;

import java.util.List;
import java.util.Map;

import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.domain.jdo.AllScripsDbObject;
import com.finanalyzer.domain.jdo.RatingDbObject;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;

public class RatingObjectForUi {
	private String ratingName;
	private String ratingValue;
	
	public RatingObjectForUi(String ratingName, String ratingValue) 
	{
		this.ratingName = ratingName;
		this.ratingValue = ratingValue;
	}

	public String getRatingName() {
		return ratingName;
	}

	public void setRatingName(String ratingName) {
		this.ratingName = ratingName;
	}

	public String getRatingValue() {
		return ratingValue;
	}

	public void setRatingValue(String ratingValue) {
		this.ratingValue = ratingValue;
	}

	public static List<RatingObjectForUi> getDummyObjects() {
		List<RatingObjectForUi> dummyObjects = FastList.newList();
		JdoDbOperations<RatingDbObject> ratingDbOperations = new JdoDbOperations<RatingDbObject>(RatingDbObject.class);
		JdoDbOperations<AllScripsDbObject> allScripsDbObject = new JdoDbOperations<AllScripsDbObject>(AllScripsDbObject.class);
		
		final List<RatingDbObject> ratingDbObjects = ratingDbOperations.getEntries();

		Map<String, StockRatingValuesEnum> ratingsToValue = UnifiedMap.newMap();

		for (RatingDbObject ratingDbObject : ratingDbObjects)
		{
			final RatingObjectForUi dummyObject = new RatingObjectForUi(ratingDbObject.getRatingName(), StockRatingValuesEnum.NOT_RATED.getDescription());
			dummyObjects.add(dummyObject);
		}
		return dummyObjects;
	}
	
	
	
}
