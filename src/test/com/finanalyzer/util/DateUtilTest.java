package test.com.finanalyzer.util;

import junit.framework.TestCase;

import com.finanalyzer.domain.jdo.StopLossDbObject;
import com.finanalyzer.util.DateUtil;

public class DateUtilTest extends TestCase {
	
	public void testDifferenceBetweenDatesLessThanTolerance()
	{
		assertTrue(DateUtil.isDateAfterTargetDate("2005-12-31", StopLossDbObject.DATE_DIFF_TOLERANCE));
		assertTrue(DateUtil.isDateBeforeTargetDate("2099-12-31", StopLossDbObject.DATE_DIFF_TOLERANCE));
	}
}
