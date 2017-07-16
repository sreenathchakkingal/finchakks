package com.finanalyzer.util;


public class HelloWorld
{
	private static final int TARGET=15;
	
	public static void main(String[] args)
	{
		 int set[] = {10, 2, 5, 15, 6};
         int sum = 15;
         int n = set.length;
         System.out.println(isSubsetSum("first", set, n, sum));
	}
	
	static boolean isSubsetSum(String iteration, int set[], int n, int sum)
	    {
		 System.out.println("iteration: "+iteration+" sum: "+sum+" n: "+n);
		 // Base Cases
	       if (sum == 0)
	       {
	    	   System.out.println("sum == 0");
	    	   return true;
	       }
	       if (n == 0 && sum != 0)
	       {
	    	   System.out.println("n == 0 && sum != 0");
	    	   return false;  
	       }
	         
	      
	       // If last element is greater than sum, then ignore it
	       if (set[n-1] > sum)
	       {
	    	   System.out.println("set[n-1] > sum");
	    	   return isSubsetSum("cuttingshort", set, n-1, sum);  
	       }
	         
	      
	       /* else, check if sum can be obtained by any of the following
	          (a) including the last element
	          (b) excluding the last element   */
	       return isSubsetSum("including", set, n-1, sum) || 
	                                   isSubsetSum("excluding", set, n-1, sum-set[n-1]);
	    }
	
	 private static String printFrom(int[] arr, int start) 
	 {
		 if(start==0)
		 {
			 return String.valueOf(arr[start]);
		 }
		 String result="";
		 for (int i=start;i<arr.length; i++)
		{
			 if(i>=0)
			 {
				 result=result+arr[i]+", ";	 
			 }
			 
		}
		return result;
	}
	
	

}
