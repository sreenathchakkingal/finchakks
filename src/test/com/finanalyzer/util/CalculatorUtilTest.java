package test.com.finanalyzer.util;

import junit.framework.TestCase;

import com.finanalyzer.util.CalculatorUtil;

public class CalculatorUtilTest extends TestCase{

	private static final float TOLERANCE = 0.01F;
	public void testDifferenceBetweenValuesLessThanTolerance()
	{
		assertFalse(CalculatorUtil.isValueMoreThanTarget(10.0f,110.0f,0.1f));
		assertTrue(CalculatorUtil.isValueMoreThanTarget(100.0f,110.0f,0.1f));
	}
	
	public void testCalculateQuaterlyCompoundedInterest()
	{
		assertEquals(7209.57, CalculatorUtil.calculateFinalAmount(1000, 10, 20*365), TOLERANCE);
		assertEquals(10, CalculatorUtil.calculateQuaterlyCompoundedInterest(1000, 7209.57f, 20*365), TOLERANCE);
		assertEquals(1000, CalculatorUtil.calculateInitialAmount(7209.57f, 10.0f, 20*365), TOLERANCE);
	}
}
