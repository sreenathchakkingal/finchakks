package com.finanalyzer.domain.builder;

import com.finanalyzer.domain.jdo.ProfitAndLossDbObject;

public class ProfitAndLossBuilder {
	
	private final ProfitAndLossDbObject profitAndLoss = new ProfitAndLossDbObject();
	
	public ProfitAndLossBuilder averageReturn(float averageReturn) 
	{
		this.profitAndLoss.setAverageReturn(averageReturn);
		return this;
	}
	
	public ProfitAndLossBuilder prevAverageReturn(float prevAverageReturn) 
	{
		this.profitAndLoss.setPrevAverageReturn(prevAverageReturn);
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
	
	public ProfitAndLossBuilder prevTotalReturn(float prevTotalReturn) 
	{
		this.profitAndLoss.setPrevTotalReturn(prevTotalReturn);
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
	
	public ProfitAndLossBuilder prevTotalReturnVsIfBank(float prevTotalReturnVsIfBank) 
	{
		this.profitAndLoss.setPrevTotalReturnVsIfBank(prevTotalReturnVsIfBank);
		return this;
	}
	
	public ProfitAndLossBuilder diffInCurrentAndPrevAverageReturn() {
		this.profitAndLoss.setDiffInCurrentAndPrevAverageReturn(
				this.profitAndLoss.getAverageReturn()-this.profitAndLoss.getPrevAverageReturn()
				);
		return this;
	}
	
	public ProfitAndLossBuilder diffInCurrentAndPrevTotalReturn() {
		this.profitAndLoss.setDiffInCurrentAndPrevTotalReturn(
				this.profitAndLoss.getTotalReturn()-this.profitAndLoss.getPrevTotalReturn()
				);
		return this;
	}
	
	public ProfitAndLossDbObject build() 
	{
		return this.profitAndLoss;
	}
}
