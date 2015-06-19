package com.finanalyzer.domain;

import java.util.List;

import com.google.appengine.api.datastore.Entity;
import com.gs.collections.api.block.function.Function;

public abstract class DbObject {
	
//	protected List<String> properties;
	public String primaryKey;

//	public DbObject(List<String> properties)
//	{
//		this.properties=properties;
//	}
	
//	public String getProperty(int propertyIndex)
//	{
//		return this.properties.get(propertyIndex);
//	}
//	
//	public List<String> getProperties() {
//		return properties;
//	}

	public abstract String getTableName();

	public String getPrimaryKey() { //default implementation - first element is the primary key
		return primaryKey;
	}
	
	public abstract Function getDbObjectSelector();
}
