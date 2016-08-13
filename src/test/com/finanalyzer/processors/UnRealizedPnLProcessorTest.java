package test.com.finanalyzer.processors;

import java.util.List;

import junit.framework.TestCase;

import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.builder.StockBuilder;
import com.finanalyzer.domain.builder.StopLossDbObjectBuilder;
import com.finanalyzer.domain.jdo.StopLossDbObject;
import com.finanalyzer.processors.UnRealizedPnLProcessor;
import com.gs.collections.impl.list.mutable.FastList;


public class UnRealizedPnLProcessorTest extends TestCase{
	
	public void testStampStopLossFlag()
	{
		final Stock stock = new StockBuilder().name("DUMMY").returnTillDate(10.0f).sellPrice(100.0f).build();
		final UnRealizedPnLProcessorTest unRealizedPnLProcessorTest = new UnRealizedPnLProcessorTest();
		DummyUnRealizedPnLProcessor dummyUnRealizedPnLProcessor=null;
		
		StopLossDbObject onlyTargetPercentStopLossDbObject1 = new StopLossDbObjectBuilder().stockName("DUMMY")
																	.lowerReturnPercentTarget(15.0f)
																	.build();

		dummyUnRealizedPnLProcessor = unRealizedPnLProcessorTest.
				new DummyUnRealizedPnLProcessor(onlyTargetPercentStopLossDbObject1);
		dummyUnRealizedPnLProcessor.enrichWithStopLossDetails(FastList.newListWith(stock)); 
		assertTrue(stock.isReachedStopLossTarget());

		
		StopLossDbObject onlyTargetPercentStopLossDbObject2 = new StopLossDbObjectBuilder()
																	.stockName("DUMMY")
																	.lowerReturnPercentTarget(5.0f)
																	.build();
		dummyUnRealizedPnLProcessor = unRealizedPnLProcessorTest.
				new DummyUnRealizedPnLProcessor(onlyTargetPercentStopLossDbObject2);
		dummyUnRealizedPnLProcessor.enrichWithStopLossDetails(FastList.newListWith(stock));
		assertFalse(stock.isReachedStopLossTarget());
		
		StopLossDbObject onlyTargetPercentStopLossDbObject3 =new StopLossDbObjectBuilder() 
				.stockName("DUMMY")
				.upperReturnPercentTarget(5.0f) 
				.build();
		dummyUnRealizedPnLProcessor = unRealizedPnLProcessorTest.
				new DummyUnRealizedPnLProcessor(onlyTargetPercentStopLossDbObject3);
		dummyUnRealizedPnLProcessor.enrichWithStopLossDetails(FastList.newListWith(stock));
		assertTrue(stock.isReachedStopLossTarget());
		
		StopLossDbObject onlyTargetPercentStopLossDbObject4 =new StopLossDbObjectBuilder() 
		.stockName("DUMMY")
		.upperReturnPercentTarget(50.0f)
		.build();
		dummyUnRealizedPnLProcessor = unRealizedPnLProcessorTest.
				new DummyUnRealizedPnLProcessor(onlyTargetPercentStopLossDbObject4);
		dummyUnRealizedPnLProcessor.enrichWithStopLossDetails(FastList.newListWith(stock));
		assertFalse(stock.isReachedStopLossTarget());
		
		StopLossDbObject onlyTargetPriceStopLossDbObject1 =new StopLossDbObjectBuilder() 
		.stockName("DUMMY")
		.lowerSellPriceTarget(110)
		.build();
		dummyUnRealizedPnLProcessor = unRealizedPnLProcessorTest.
				new DummyUnRealizedPnLProcessor(onlyTargetPriceStopLossDbObject1);
		dummyUnRealizedPnLProcessor.enrichWithStopLossDetails(FastList.newListWith(stock));
		assertTrue(stock.isReachedStopLossTarget());
		
		StopLossDbObject onlyTargetPriceStopLossDbObject2 =
				new StopLossDbObjectBuilder() 
		.stockName("DUMMY")
		.upperSellPriceTarget(90)
		.build();
		dummyUnRealizedPnLProcessor = unRealizedPnLProcessorTest.
				new DummyUnRealizedPnLProcessor(onlyTargetPriceStopLossDbObject2);
		dummyUnRealizedPnLProcessor.enrichWithStopLossDetails(FastList.newListWith(stock));
		assertTrue(stock.isReachedStopLossTarget());
		
		StopLossDbObject onlyTargetPriceStopLossDbObject3 = new StopLossDbObjectBuilder() 
		.stockName("DUMMY")
		.upperSellPriceTarget(120)
		.build();
		dummyUnRealizedPnLProcessor = unRealizedPnLProcessorTest.
				new DummyUnRealizedPnLProcessor(onlyTargetPriceStopLossDbObject3);
		dummyUnRealizedPnLProcessor.enrichWithStopLossDetails(FastList.newListWith(stock));
		assertFalse(stock.isReachedStopLossTarget());
				
		StopLossDbObject targetPriceAndDateStopLossDbObject1 = new StopLossDbObjectBuilder() 
		.stockName("DUMMY")
		.lowerSellPriceTarget(110)
		.achieveAfterDate("2099-12-31")
		.build();
		dummyUnRealizedPnLProcessor = unRealizedPnLProcessorTest.
				new DummyUnRealizedPnLProcessor(targetPriceAndDateStopLossDbObject1);
		dummyUnRealizedPnLProcessor.enrichWithStopLossDetails(FastList.newListWith(stock));
		assertFalse(stock.isReachedStopLossTarget());
		
		
		StopLossDbObject targetPriceAndDateStopLossDbObject2 =  new StopLossDbObjectBuilder() 
		.stockName("DUMMY")
		.lowerSellPriceTarget(110)
		.achieveByDate("2099-12-31")
		.build();
		dummyUnRealizedPnLProcessor = unRealizedPnLProcessorTest.
				new DummyUnRealizedPnLProcessor(targetPriceAndDateStopLossDbObject2);
		dummyUnRealizedPnLProcessor.enrichWithStopLossDetails(FastList.newListWith(stock));
		assertTrue(stock.isReachedStopLossTarget());
		
		
		StopLossDbObject targetReturnAndDateStopLossDbObject1 =new StopLossDbObjectBuilder() 
		.stockName("DUMMY")
		.lowerReturnPercentTarget(15.0f)
		.achieveAfterDate("2005-12-31")//date conditions is met
		.achieveByDate("2005-12-31")//date conditions is not met
		.build();
				
		dummyUnRealizedPnLProcessor = unRealizedPnLProcessorTest.
				new DummyUnRealizedPnLProcessor(targetReturnAndDateStopLossDbObject1);
		dummyUnRealizedPnLProcessor.enrichWithStopLossDetails(FastList.newListWith(stock));
		assertTrue(stock.isReachedStopLossTarget());
		
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
