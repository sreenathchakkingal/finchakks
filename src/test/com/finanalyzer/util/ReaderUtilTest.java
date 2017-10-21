package test.com.finanalyzer.util;

import java.util.regex.Pattern;

import com.finanalyzer.util.ReaderUtil;

import junit.framework.TestCase;

public class ReaderUtilTest extends TestCase{
	
	public void testRemoveCommanBetweenQuotes()
	{
		
		String input="-17.35,\"1,106.01\",\"1,106.01\",1.13m";
//		assertEquals("-17.35,1106.01,1.13m",ReaderUtil.removeCommanBetweenQuotes(input));
		String test = "Eicher Motors - NSE - 10/08 (15:59),30111.7,\"-1,377.40\",\"31,489.10\",76974,1,5850,17-04-2014,5850,31799.90 / 29830.00,32449.35 / 19530.00,-1377,-4.37,24262,414.74,30112,,";
//		System.out.println(ReaderUtil.removeCommanBetweenQuotes(input));
		String output="";
	      Pattern p = Pattern.compile(",(?=([^\"]*\"[^\"]*\")*(?![^\"]*\"))");
	          // Split input with the pattern
	          String[] fields = p.split(test);
	          for (int i = 0; i < fields.length; i++) {
	              // Get rid of residual double quotes
	        	  fields[i]= fields[i].replace("\"", "").replace(",", "");
	        	  System.out.println(fields[i]);
	          }
	          
	          
	          
	}

}
