package com.finanalyzer.domain;

import java.util.Arrays;
import java.util.List;

import com.finanalyzer.db.AllScripsUtil;
import com.finanalyzer.db.DbOperations;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.gs.collections.impl.list.mutable.FastList;

public class AllScriptsDbObject extends DbObject{

	private static final String NSE_ID = "NseId";
	private static final String STOCK_NAME = "StockName";
	private static final String ISIN = "Isin";
	private static final String BSE_ID = "BseId";
	private static final String FAIR_VALUE = "FairValue";
	private static final String INDUSTRY = "Industry";
	
	private String nseId;
	private String stockName;
	private String isin;
	private String bseId;
	private String fairValue;
	private String industry;

	public AllScriptsDbObject(String nseId, String stockName, String isin, String bseId, String fairValue, String industry) {
		this.primaryKey=nseId;
		this.nseId= nseId;
		this.stockName= stockName;
		this.isin= isin;
		this.bseId= bseId;
		this.fairValue= fairValue;
		this.industry=industry;
	}

	@Override
	public String getTableName() {
		return "AllScrips";
	}

	public String retrieveBseIdForNse(String nseId)
	{
		Filter filter = new FilterPredicate(NSE_ID, FilterOperator.EQUAL, nseId);
		Query query = new Query(this.getTableName()).setFilter(filter);
		
		DbOperations dbOperations = DbOperations.getInstance();
		Entity entity = dbOperations.retrieveEntity(query);
		return this.convertToDbObject(entity).getBseId();
	}

	public String retrieveNseIdForBse(String bseId)
	{
//		Filter filter = new FilterPredicate(BSE_ID, FilterOperator.EQUAL, bseId);
		Query query = new Query(this.getTableName());
		
		DbOperations dbOperations = DbOperations.getInstance();
		List<Entity> entities = FastList.newList(dbOperations.retrieveEntities(query, false));
		return null;
	}
	
	
	@Override
	public AllScriptsDbObject convertToDbObject(Entity entity) {
		return new AllScriptsDbObject(
				(String) entity.getProperty(NSE_ID),
				(String) entity.getProperty(STOCK_NAME),
				(String) entity.getProperty(ISIN),
				(String) entity.getProperty(BSE_ID),
				(String) entity.getProperty(FAIR_VALUE),
				(String) entity.getProperty(INDUSTRY)
				);
	}

	@Override
	public List<DbObject> remodelData() {
		AllScripsUtil allScripsUtil = AllScripsUtil.getInstance();
		FastList<Entity> entitiesFromLegacyTable = allScripsUtil.retrieveAllEntities();
		List<Entity> entities= FastList.newList(); 

		for(Entity legacyEntity : entitiesFromLegacyTable)
		{
			String isin =(String) legacyEntity.getProperty("isin");
			String stockName =(String)  legacyEntity.getProperty("stockName");
			String bseId =(String)  legacyEntity.getProperty("bseId");
			String nseId =(String)  legacyEntity.getProperty("nseId");
			String fairValue =(String)  legacyEntity.getProperty("fairValue");
			String industry =(String)  legacyEntity.getProperty("industry");
			
//			entitiesFromLegacyTable.
		}
		return null;
	}

	public String getNseId() {
		return nseId;
	}

	public String getStockName() {
		return stockName;
	}

	public String getIsin() {
		return isin;
	}

	public String getBseId() {
		return bseId;
	}

	public String getFairValue() {
		return fairValue;
	}

	public String getIndustry() {
		return industry;
	}
	
	

}
