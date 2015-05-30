package com.finanalyzer.domain;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.gs.collections.impl.list.mutable.FastList;

public class StockRatingValue  
{
	private static final String SCORE = "Score : ";
	private Map<String, StockRatingValuesEnum> ratingToValue;
	
	
	public StockRatingValue(Map<String, StockRatingValuesEnum> ratingToValue) {
		this.ratingToValue = ratingToValue;
	}
	
	public Map<String, StockRatingValuesEnum> getRatingToValue() {
		return ratingToValue;
	}
	
	public List<String> getInferences()
	{
		int netRating = 0;
		int validRatingCount = 0;
		int notRatedCount = 0;
		int ratingsCount = 0;
		List<String> inferences = FastList.newList();
		for (Entry<String, StockRatingValuesEnum> entry : this.ratingToValue.entrySet()) {
			ratingsCount++;
			StockRatingValuesEnum value = entry.getValue();
			if (value == StockRatingValuesEnum.DISCARD) {
				return FastList.newListWith(StockRatingValuesEnum.DISCARD.getDescription());
			} else if (value == StockRatingValuesEnum.NOT_RATED) {
				notRatedCount++;
			} else {
				validRatingCount++;
				netRating = netRating + value.getRating();
			}
		}
		inferences.add(SCORE + netRating + "/" + validRatingCount);
		inferences.add("Total Ratings : " + ratingsCount);
		inferences.add("Not Rated Count : " + notRatedCount);
		return inferences;
	}
	
	public String getScore()
	{
		return getInferences().get(0).substring(SCORE.length());
	}
}
