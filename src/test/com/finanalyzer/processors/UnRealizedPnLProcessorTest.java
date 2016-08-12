package test.com.finanalyzer.processors;

import java.util.List;

import junit.framework.TestCase;

import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.builder.StockBuilder;
import com.finanalyzer.domain.jdo.StopLossDbObject;
import com.finanalyzer.processors.UnRealizedPnLProcessor;
import com.gs.collections.impl.list.mutable.FastList;


public class UnRealizedPnLProcessorTest extends TestCase{
	
	private static final float ZERO = 0.0f;

	
	public void testStampStopLossFlag()
	{
		final Stock stock = new StockBuilder().name("DUMMY").sellPrice(100.0f).returnTillDate(7.5f).build();
		final UnRealizedPnLProcessorTest unRealizedPnLProcessorTest = new UnRealizedPnLProcessorTest();
		DummyUnRealizedPnLProcessor dummyUnRealizedPnLProcessor=null;
		
		StopLossDbObject onlyTargetPercentStopLossDbObject1 = new StopLossDbObject("DUMMY", 
		7.1f, ZERO, null);
		dummyUnRealizedPnLProcessor = unRealizedPnLProcessorTest.
				new DummyUnRealizedPnLProcessor(onlyTargetPercentStopLossDbObject1);
		dummyUnRealizedPnLProcessor.enrichWithStopLossDetails(FastList.newListWith(stock));
		assertTrue(stock.isReachedStopLossTarget());

		StopLossDbObject onlyTargetPercentStopLossDbObject2 = new StopLossDbObject("DUMMY", 
		8.5f, ZERO, null);
		dummyUnRealizedPnLProcessor = unRealizedPnLProcessorTest.
				new DummyUnRealizedPnLProcessor(onlyTargetPercentStopLossDbObject2);
		dummyUnRealizedPnLProcessor.enrichWithStopLossDetails(FastList.newListWith(stock));
		assertFalse(stock.isReachedStopLossTarget());
		
		StopLossDbObject onlyTargetPriceStopLossDbObject1 = new StopLossDbObject("DUMMY", 
				ZERO, 80, null);
		dummyUnRealizedPnLProcessor = unRealizedPnLProcessorTest.
				new DummyUnRealizedPnLProcessor(onlyTargetPriceStopLossDbObject1);
		dummyUnRealizedPnLProcessor.enrichWithStopLossDetails(FastList.newListWith(stock));
		assertTrue(stock.isReachedStopLossTarget());
		
		StopLossDbObject onlyTargetPriceStopLossDbObject2 = new StopLossDbObject("DUMMY", 
				ZERO, 110, null);
		dummyUnRealizedPnLProcessor = unRealizedPnLProcessorTest.
				new DummyUnRealizedPnLProcessor(onlyTargetPriceStopLossDbObject2);
		dummyUnRealizedPnLProcessor.enrichWithStopLossDetails(FastList.newListWith(stock));
		assertTrue(stock.isReachedStopLossTarget());
		
		
		StopLossDbObject onlyTargetPriceStopLossDbObject3 = new StopLossDbObject("DUMMY", 
				ZERO, 150, null);
		dummyUnRealizedPnLProcessor = unRealizedPnLProcessorTest.
				new DummyUnRealizedPnLProcessor(onlyTargetPriceStopLossDbObject3);
		dummyUnRealizedPnLProcessor.enrichWithStopLossDetails(FastList.newListWith(stock));
		assertFalse(stock.isReachedStopLossTarget());
		
		StopLossDbObject targetPriceAndDateStopLossDbObject1 = new StopLossDbObject("DUMMY", 
				ZERO, 80, "2016-12-31");
		dummyUnRealizedPnLProcessor = unRealizedPnLProcessorTest.
				new DummyUnRealizedPnLProcessor(targetPriceAndDateStopLossDbObject1);
		dummyUnRealizedPnLProcessor.enrichWithStopLossDetails(FastList.newListWith(stock));
		assertTrue(stock.isReachedStopLossTarget());
		
		StopLossDbObject targetPriceAndDateStopLossDbObject2 = new StopLossDbObject("DUMMY", 
				ZERO, 80, "2016-07-29");
		dummyUnRealizedPnLProcessor = unRealizedPnLProcessorTest.
				new DummyUnRealizedPnLProcessor(targetPriceAndDateStopLossDbObject2);
		dummyUnRealizedPnLProcessor.enrichWithStopLossDetails(FastList.newListWith(stock));
		assertFalse(stock.isReachedStopLossTarget());
		
		StopLossDbObject targetReturnndDateStopLossDbObject1 = new StopLossDbObject("DUMMY", 
				6, ZERO, "2016-12-31");
		dummyUnRealizedPnLProcessor = unRealizedPnLProcessorTest.
				new DummyUnRealizedPnLProcessor(targetReturnndDateStopLossDbObject1);
		dummyUnRealizedPnLProcessor.enrichWithStopLossDetails(FastList.newListWith(stock));
		assertTrue(stock.isReachedStopLossTarget());
		
		
		StopLossDbObject targetReturnndDateStopLossDbObject2 = new StopLossDbObject("DUMMY", 
				6, ZERO, "2016-07-29");
		dummyUnRealizedPnLProcessor = unRealizedPnLProcessorTest.
				new DummyUnRealizedPnLProcessor(targetReturnndDateStopLossDbObject2);
		dummyUnRealizedPnLProcessor.enrichWithStopLossDetails(FastList.newListWith(stock));
		assertFalse(stock.isReachedStopLossTarget());
	}
	
	public class DummyUnRealizedPnLProcessor extends UnRealizedPnLProcessor
	{
		
		StopLossDbObject stopLossDbObject;
		
		public DummyUnRealizedPnLProcessor(StopLossDbObject stopLossDbObject)
		{
			this.stopLossDbObject=stopLossDbObject;
		}
		
		@Override
		 public List<StopLossDbObject> getMatchingStopLossDbObject(Stock eachStock)
		 {
				return FastList.newListWith(this.stopLossDbObject);
		 }	
	}
	
}
