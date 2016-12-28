package test.com.finanalyzer.util;

import com.finanalyzer.domain.builder.ProfitAndLossBuilder;
import com.finanalyzer.domain.jdo.ProfitAndLossDbObject;
import com.finanalyzer.util.ConverterUtil;
import junit.framework.TestCase;

public class ConverterUtilTest extends TestCase 
{
	private static final float TOLERANCE = 0.01F;
	
	public void testConvertToProfitAndLossObject()
	{
		final ProfitAndLossDbObject profitAndLossPrev = new ProfitAndLossBuilder()
		.averageReturn(10.0f)
		.totalReturn(30.0f)
		.totalReturnVsIfBank(50.0f).build();
		
		final ProfitAndLossDbObject profitAndLoss = new ProfitAndLossBuilder()
		.averageReturn(100.0f)
		.totalInvestment(200.0f)
		.totalReturn(300.0f)
		.totalReturnIfBank(400.0f)
		.totalReturnVsIfBank(500.0f).build();
		
		final ProfitAndLossDbObject result = ConverterUtil.convertToProfitAndLossObject(profitAndLossPrev,profitAndLoss);
		
		assertEquals(10.0f,result.getPrevAverageReturn(), TOLERANCE);
		assertEquals(30.0f,result.getPrevTotalReturn(), TOLERANCE);
		assertEquals(50.0f,result.getPrevTotalReturnVsIfBank(), TOLERANCE);
		
		assertEquals(100.0f,result.getAverageReturn(), TOLERANCE);
		assertEquals(200.0f,result.getTotalInvestment(), TOLERANCE);
		assertEquals(300.0f,result.getTotalReturn(), TOLERANCE);
		assertEquals(400.0f,result.getTotalReturnIfBank(), TOLERANCE);
		assertEquals(500.0f,result.getTotalReturnVsIfBank(), TOLERANCE);

		assertEquals(90.0f,result.getDiffInCurrentAndPrevAverageReturn(), TOLERANCE);
		assertEquals(270.0f,result.getDiffInCurrentAndPrevTotalReturn(), TOLERANCE);
		
		final ProfitAndLossDbObject result1 = ConverterUtil.convertToProfitAndLossObject(null,profitAndLoss);
		
		assertEquals(0.0f,result1.getPrevAverageReturn(), TOLERANCE);
		assertEquals(0.0f,result1.getPrevTotalReturn(), TOLERANCE);
		assertEquals(0.0f,result1.getPrevTotalReturnVsIfBank(), TOLERANCE);
		
		assertEquals(100.0f,result1.getAverageReturn(), TOLERANCE);
		assertEquals(200.0f,result1.getTotalInvestment(), TOLERANCE);
		assertEquals(300.0f,result1.getTotalReturn(), TOLERANCE);
		assertEquals(400.0f,result1.getTotalReturnIfBank(), TOLERANCE);
		assertEquals(500.0f,result1.getTotalReturnVsIfBank(), TOLERANCE);
		
		assertEquals(100.0f,result1.getDiffInCurrentAndPrevAverageReturn(), TOLERANCE);
		assertEquals(300.0f,result1.getDiffInCurrentAndPrevTotalReturn(), TOLERANCE);

		
	}	

}
