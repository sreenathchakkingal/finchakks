package com.finanalyzer.domain;

import java.util.Map;

import com.gs.collections.impl.map.mutable.UnifiedMap;

public enum StockRatingValuesEnum
{
	GOOD("Good", 1), AVERAGE("Average", 0), BAD("Bad", -1), DISCARD("Discard", -100), NOT_RATED("Not Rated",-99);

	public static final Map<String, StockRatingValuesEnum> DESC_TO_ENUM__MAPPING = UnifiedMap.newMap();
	public static final Map<Integer, StockRatingValuesEnum> RATING_VALUE_TO_ENUM_MAPPING = UnifiedMap.newMap();
	
	static
	{
		final StockRatingValuesEnum[] values = StockRatingValuesEnum.values();
		for (StockRatingValuesEnum eachValue: values)
		{
			DESC_TO_ENUM__MAPPING.put(eachValue.getDescription(), eachValue);
			RATING_VALUE_TO_ENUM_MAPPING.put(eachValue.getRating(), eachValue);
		}
	}
	
	private int rating;
	private String description;

	private StockRatingValuesEnum(String description, int rating)
	{
		this.description=description;
		this.rating=rating;
	}

	public int getRating()
	{
		return this.rating;
	}

	public String getDescription() {
		return description;
	}

	public static StockRatingValuesEnum getEnumForRatingDescription(String rating)
	{
		final StockRatingValuesEnum stockRatingValuesEnum = DESC_TO_ENUM__MAPPING.get(rating);
		return stockRatingValuesEnum==null ? NOT_RATED : stockRatingValuesEnum;
		
//		for (StockRatingValuesEnum eachEnum : StockRatingValuesEnum.values())
//		{
//			if(eachEnum.getDescription().equals(rating))
//			{
//				return eachEnum;
//			}
//		}
//		return NOT_RATED;
	}
	
	public static StockRatingValuesEnum getEnumForRatingValue(int rating)
	{
		final StockRatingValuesEnum stockRatingValuesEnum = RATING_VALUE_TO_ENUM_MAPPING.get(rating);
		return stockRatingValuesEnum==null ? NOT_RATED : stockRatingValuesEnum;
		
//		for (StockRatingValuesEnum eachEnum : StockRatingValuesEnum.values())
//		{
//			if(eachEnum.getRating()==rating)
//			{
//				return eachEnum;
//			}
//		}
//		return NOT_RATED;
	}
	
	@Override
	public String toString()
	{
		return this.description+" : "+ this.rating; 
	}
}
