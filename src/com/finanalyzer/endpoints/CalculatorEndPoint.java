package com.finanalyzer.endpoints;

import com.finanalyzer.domain.CompoundInterest;
import com.finanalyzer.util.CalculatorUtil;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;

@Api(name = "calculatorEndPoint", version = "v1")
public class CalculatorEndPoint {
	
	@ApiMethod(name = "calculateMissingCompoundInterestComponent", path="calculateMissingCompoundInterestComponent")
	public CompoundInterest calculateMissingCompoundInterestComponent(
			@Nullable @Named("initialAmount") float initialAmount, 
			@Nullable @Named("interestRate") float interestRate, 
			@Named("durationInDays") long durationInDays, 
			@Nullable @Named("finalAmount") float finalAmount)
	{
		CompoundInterest compoundInterest = new CompoundInterest(initialAmount, interestRate, durationInDays, finalAmount);
		
		if(initialAmount==0.0f)
		{
			final float calculatedInitialAmount = CalculatorUtil.calculateInitialAmount(finalAmount, interestRate, durationInDays);
			compoundInterest.setInitalAmount(calculatedInitialAmount);
		}
		else if(interestRate==0.0f)
		{
			final float calculatedInterestRate = CalculatorUtil.calculateQuaterlyCompoundedInterest(initialAmount, finalAmount, durationInDays);
			compoundInterest.setInterestRate(calculatedInterestRate);
		}
		else if(finalAmount==0.0f)
		{
			final float calculatedFinalAmount = CalculatorUtil.calculateFinalAmount(initialAmount, interestRate, durationInDays);
			compoundInterest.setFinalAmount(calculatedFinalAmount);
		}
		return compoundInterest;
	}

}
