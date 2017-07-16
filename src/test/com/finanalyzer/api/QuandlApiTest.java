package test.com.finanalyzer.api;

import junit.framework.TestCase;

import com.finanalyzer.api.QuandlApi;

public class QuandlApiTest  extends TestCase{
	
	public void testGetodifiedStockName()
	{
		String stockName1="bajaj-auto";
		assertEquals("BAJAJ_AUTO", QuandlApi.getModifiedStockName(stockName1));
		
		String stockName2="bajaj";
		assertEquals("BAJAJ", QuandlApi.getModifiedStockName(stockName2));
	}
}
