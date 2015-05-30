package com.finanalyzer.api;

import java.util.Set;

public class MultisetQuery extends SimpleQuery
{
	
	Set<String> qCodes;

    MultisetQuery(Set<String> qCodes)
	{
    	this.qCodes=qCodes;
	}

	public Set<String> getQCodes()
	{
		return qCodes;
	}
    
    
}
