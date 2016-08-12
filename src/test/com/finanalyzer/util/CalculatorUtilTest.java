package test.com.finanalyzer.util;

import org.junit.Test;

import com.finanalyzer.util.CalculatorUtil;

import junit.framework.TestCase;

public class CalculatorUtilTest extends TestCase{

	public void testDifferenceBetweenValuesLessThanTolerance()
	{
		assertFalse(CalculatorUtil.isValueMoreThanTarget(10.0f,110.0f,0.1f));
		assertTrue(CalculatorUtil.isValueMoreThanTarget(100.0f,110.0f,0.1f));
	}
	
	public void testCalculateQuaterlyCompoundedInterest()
	{
		assertEquals(7.5, CalculatorUtil.calculateQuaterlyCompoundedInterest(100.0f, 107.71f, 365), 0.1f);
	}
}
