package test.com.finanalyzer.api;

import com.finanalyzer.api.QuandlApi;
import junit.framework.TestCase;

public class QuandlApiTest  extends TestCase{
	
	public void testGetodifiedStockName()
	{
		String stockName1="bajaj-auto";
		assertEquals("BAJAJ_AUTO", QuandlApi.getModifiedStockName(stockName1));
		
		String stockName2="bajaj";
		assertEquals("BAJAJ", QuandlApi.getModifiedStockName(stockName2));
	}
}
