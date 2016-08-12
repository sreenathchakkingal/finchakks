package test.com.finanalyzer.util;

import com.finanalyzer.domain.jdo.StopLossDbObject;
import com.finanalyzer.util.DateUtil;

import junit.framework.TestCase;

public class DateUtilTest extends TestCase {
	
	public void testDifferenceBetweenDatesLessThanTolerance()
	{
		assertTrue(DateUtil.dateCloserToCurrentDate(DateUtil.todaysDate(), StopLossDbObject.DATE_DIFF_TOLERANCE));
		assertFalse(DateUtil.dateCloserToCurrentDate("2016-01-10",StopLossDbObject.DATE_DIFF_TOLERANCE));
	}
}
