package com.finanalyzer.domain;

public class Ratio
{
	private final int numerator;
	private final int denominator;

	public Ratio(String ratio)
	{
		final String[] ratiosSplit = ratio.split(":");
		this.numerator = Integer.parseInt(ratiosSplit[0]);
		this.denominator = Integer.parseInt(ratiosSplit[1]);
	}

	public int getNumerator()
	{
		return this.numerator;
	}

	public int getDenominator()
	{
		return this.denominator;
	}


}
