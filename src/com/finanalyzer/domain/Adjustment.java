package com.finanalyzer.domain;

import java.util.List;

import com.gs.collections.impl.list.mutable.FastList;

public class Adjustment
{
	private final AdjustmentType adjustmentType;
	private final List<Ratio> ratios;
	private final int year;

	public Adjustment(AdjustmentType adjustmentType, String ratios, int year)
	{
		this.year=year;
		this.adjustmentType=adjustmentType;
		this.ratios=this.convertToRatios(ratios);
	}

	//10:2,12:5
	private List<Ratio> convertToRatios(String ratios)
	{
		List<Ratio> ratioObjects = FastList.newList();
		final String[] ratio = ratios.split(",");
		for (String eachRatio : ratio)
		{
			ratioObjects.add(new Ratio(eachRatio));
		}

		return ratioObjects;
	}

	public AdjustmentType getAdjustmentType()
	{
		return this.adjustmentType;
	}

	public List<Ratio> getRatios()
	{
		return this.ratios;
	}

	public int getYear()
	{
		return this.year;
	}

	public float getFactor()
	{
		float normalizedDenominator;
		float result=1.0f;
		if(this.adjustmentType==AdjustmentType.SPLIT)
		{
			for (Ratio eachRatio : this.ratios)
			{
				normalizedDenominator=eachRatio.getDenominator()/eachRatio.getNumerator();
				result = result * normalizedDenominator;
			}
			return result;
		}

		if(this.adjustmentType==AdjustmentType.BONUS)
		{
			for (Ratio eachRatio : this.ratios)
			{
				normalizedDenominator=(float)eachRatio.getNumerator()/(eachRatio.getDenominator()+eachRatio.getNumerator());
				result=result * (1+normalizedDenominator);
			}
			return result;
		}

		return 0.0f;
	}

	public boolean isApplyFromCurrentYear()
	{
		if (this.adjustmentType==AdjustmentType.SPLIT)
		{
			return false;
		}
		return true;
	}

}
