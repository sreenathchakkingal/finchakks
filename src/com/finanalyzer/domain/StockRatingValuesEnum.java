package com.finanalyzer.domain;

public enum StockRatingValuesEnum
{
	GOOD("Good", 1), AVERAGE("Average", 0), BAD("Bad", -1), DISCARD("Discard", -100), NOT_RATED("Not Rated",-99);

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
		for (StockRatingValuesEnum eachEnum : StockRatingValuesEnum.values())
		{
			if(eachEnum.getDescription().equals(rating))
			{
				return eachEnum;
			}
		}
		return NOT_RATED;
	}
	
	public static StockRatingValuesEnum getEnumForRatingValue(int rating)
	{
		for (StockRatingValuesEnum eachEnum : StockRatingValuesEnum.values())
		{
			if(eachEnum.getRating()==rating)
			{
				return eachEnum;
			}
		}
		return NOT_RATED;
	}
}
