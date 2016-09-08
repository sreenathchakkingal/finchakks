package test.com.finanalyzer.util;


import com.finanalyzer.util.StringUtil;

import junit.framework.TestCase;

public class StringUtilTest extends TestCase{
	
	public void testIsInvalidValuee()
	{
		assertTrue(StringUtil.isInvalidValue(" "));
		
	}

}
