package com.finanalyzer.domain;

public class Movement
{
	private final int ups;
	private final int downs;

	public Movement(int ups, int downs)
	{
		this.ups = ups;
		this.downs = downs;
	}

	public int getUps()
	{
		return this.ups;
	}
	public int getDowns()
	{
		return this.downs;
	}

}
