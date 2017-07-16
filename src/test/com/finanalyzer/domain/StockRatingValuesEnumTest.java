package test.com.finanalyzer.domain;

import junit.framework.TestCase;

import com.finanalyzer.domain.StockRatingValuesEnum;

public class StockRatingValuesEnumTest extends TestCase{
	
	public void testGetEnumForRatingDescription()
	{
		assertEquals(StockRatingValuesEnum.GOOD, StockRatingValuesEnum.getEnumForRatingDescription("Good"));
		assertEquals(StockRatingValuesEnum.AVERAGE, StockRatingValuesEnum.getEnumForRatingDescription("Average"));
		assertEquals(StockRatingValuesEnum.BAD, StockRatingValuesEnum.getEnumForRatingDescription("Bad"));
		assertEquals(StockRatingValuesEnum.DISCARD, StockRatingValuesEnum.getEnumForRatingDescription("Discard"));
		assertEquals(StockRatingValuesEnum.NOT_RATED, StockRatingValuesEnum.getEnumForRatingDescription("Not Rated"));
		assertEquals(StockRatingValuesEnum.NOT_RATED, StockRatingValuesEnum.getEnumForRatingDescription("Dummy String"));
	}

	public void testGetEnumForRatingValue()
	{
		assertEquals(StockRatingValuesEnum.GOOD, StockRatingValuesEnum.getEnumForRatingValue(1));
		assertEquals(StockRatingValuesEnum.AVERAGE, StockRatingValuesEnum.getEnumForRatingValue(0));
		assertEquals(StockRatingValuesEnum.BAD, StockRatingValuesEnum.getEnumForRatingValue(-1));
		assertEquals(StockRatingValuesEnum.DISCARD, StockRatingValuesEnum.getEnumForRatingValue(-100));
		assertEquals(StockRatingValuesEnum.NOT_RATED, StockRatingValuesEnum.getEnumForRatingValue(-99));
		assertEquals(StockRatingValuesEnum.NOT_RATED, StockRatingValuesEnum.getEnumForRatingValue(1222));
	}
	
}
