package com.finanalyzer.domain.builder;

import com.finanalyzer.domain.jdo.ProfitAndLossDbObject;

public class ProfitAndLossBuilder {
	
	private final ProfitAndLossDbObject profitAndLoss = new ProfitAndLossDbObject();
	
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
	  
	public ProfitAndLossDbObject build() 
	{
		return this.profitAndLoss;
	}
	  
}
