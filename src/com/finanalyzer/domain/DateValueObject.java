package com.finanalyzer.domain;

public class DateValueObject
{
	private final String date;
	private float value;

	public DateValueObject(String date, float value)
	{
		this.date = date;
		this.value = value;
	}

	public String getDate()
	{
		return this.date;
	}

	public float getValue()
	{
		return this.value;
	}

	public void setValue(float value)
	{
		this.value = value;
	}

	@Override
	public String toString()
	{
		return this.getDate() + ":" + this.getValue();
	}
}
