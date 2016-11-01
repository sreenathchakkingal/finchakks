package com.finanalyzer.util;

import java.util.Date;

/*
Regular Compound Interest Formula

A= P (1 + r/n)^ (nt)
P = principal amount (the initial amount you borrow or deposit)
r  = annual rate of interest (as a decimal)
t  = number of years the amount is deposited or borrowed for.
A = amount of money accumulated after n years, including interest.
n  =  number of times the interest is compounded per y

*/

public class CalculatorUtil
{
  public static final int DAYS_IN_A_YEAR = 365;
  
  public static float getNetGain(float currentClosePrice, float prevClosePrice)
  {
    return (currentClosePrice - prevClosePrice) / prevClosePrice;
  }
  
  public static float calculateQuaterlyCompoundedInterest(float initialAmount, float finalAmount, long numberOfDays)
  {
    float finalByIntial = finalAmount / initialAmount;
    return (float) ((Math.pow(finalByIntial, 1.0D / (4.0D * (numberOfDays / 365.0D))) - 1.0D) * 4.0D * 100.0D);
  }
  
  public static float calculateFinalAmount(float initialAmount, float interestRate, long numberOfDays)
  {
    return (float) (initialAmount * Math.pow(1.0D + interestRate / 400.0D, 4L * numberOfDays / 365.0D));
  }
  
  public static float calculateInitialAmount(float finalAmount, float interestRate, long numberOfDays)
  {
      return (float) (finalAmount/(Math.pow(1.0f + interestRate / 400.0f, 4L * numberOfDays / 365.0f)));
  }
  
  public static boolean isValueMoreThanTarget(float value,float targetValue, float tolerance)
  {
	  return value>= targetValue*(1-tolerance);
  }
  
  public static boolean isValueLessThanTarget(float value,float targetValue, float tolerance)
  {
	  return value<= targetValue*(1+tolerance);
  }

}
