package test.com.finanalyzer.util;


import junit.framework.TestCase;

import com.finanalyzer.util.StringUtil;

public class StringUtilTest extends TestCase{
	
	public void testIsInvalidValuee()
	{
		assertTrue(StringUtil.isInvalidValue(" "));
		
	}

}
