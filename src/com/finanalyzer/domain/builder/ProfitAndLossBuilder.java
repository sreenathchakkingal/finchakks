package com.finanalyzer.domain.builder;

import com.finanalyzer.domain.ProfitAndLoss;

public class ProfitAndLossBuilder {
	
	private final ProfitAndLoss profitAndLoss = new ProfitAndLoss();
	
	public ProfitAndLossBuilder averageReturn(float averageReturn) 
	{
		this.profitAndLoss.setAverageReturn(averageReturn);
		return this;
	}
	
	public ProfitAndLossBuilder totalInvestment(float totalInvestment) 
	{
		this.profitAndLoss.setTotalInvestment(totalInvestment);
		return this;
	}
	
	public ProfitAndLossBuilder totalReturn(float totalReturn) 
	{
		this.profitAndLoss.setTotalReturn(totalReturn);
		return this;
	}
	
	public ProfitAndLossBuilder totalReturnIfBank(float totalReturnIfBank) 
	{
		this.profitAndLoss.setTotalReturnIfBank(totalReturnIfBank);
		return this;
	}
	
	public ProfitAndLossBuilder totalReturnVsIfBank(float totalReturnVsIfBank) 
	{
		this.profitAndLoss.setTotalReturnVsIfBank(totalReturnVsIfBank);
		return this;
	}
	  
	public ProfitAndLoss build() 
	{
		return this.profitAndLoss;
	}
	  
}
