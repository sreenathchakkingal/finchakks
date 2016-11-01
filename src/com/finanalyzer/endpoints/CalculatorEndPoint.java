package com.finanalyzer.endpoints;

import java.util.logging.Logger;

import com.finanalyzer.domain.CompoundInterest;
import com.finanalyzer.util.CalculatorUtil;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;

@Api(name = "calculatorEndPoint", version = "v1")
public class CalculatorEndPoint {
	
	private static final Logger LOG = Logger.getLogger(CalculatorEndPoint.class.getName());
	
	@ApiMethod(name = "calculateMissingCompoundInterestComponent", path="calculateMissingCompoundInterestComponent", httpMethod = ApiMethod.HttpMethod.GET)
	public CompoundInterest calculateMissingCompoundInterestComponent(
			@Nullable @Named("initialAmount") float initialAmount, 
			@Nullable @Named("interestRate") float interestRate, 
			@Named("durationInDays") int durationInDays, 
			@Nullable @Named("finalAmount") float finalAmount)
	{
		LOG.info("initialAmount: "+initialAmount+" interestRate: "+interestRate+" durationInDays: "+durationInDays+" finalAmount: "+finalAmount);
		
		CompoundInterest compoundInterest = new CompoundInterest(initialAmount, interestRate, durationInDays, finalAmount);
		
		if(initialAmount==0.0f)
		{
			final float calculatedInitialAmount = CalculatorUtil.calculateInitialAmount(finalAmount, interestRate, durationInDays);
			LOG.info("calculatedInitialAmount: "+calculatedInitialAmount);
			compoundInterest.setInitialAmount(calculatedInitialAmount);
		}
		else if(interestRate==0.0f)
		{
			final float calculatedInterestRate = CalculatorUtil.calculateQuaterlyCompoundedInterest(initialAmount, finalAmount, durationInDays);
			LOG.info("calculatedInterestRate: "+calculatedInterestRate);
			compoundInterest.setInterestRate(calculatedInterestRate);
		}
		else if(finalAmount==0.0f)
		{
			final float calculatedFinalAmount = CalculatorUtil.calculateFinalAmount(initialAmount, interestRate, durationInDays);
			LOG.info("calculatedFinalAmount: "+calculatedFinalAmount);
			compoundInterest.setFinalAmount(calculatedFinalAmount);
		}
		return compoundInterest;
	}

}
