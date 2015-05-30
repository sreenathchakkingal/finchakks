package com.finanalyzer.db;

import java.util.Arrays;
import java.util.List;

import com.finanalyzer.domain.MappingStockId;
import com.finanalyzer.util.StringUtil;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.gs.collections.impl.list.mutable.FastList;

public class StockIdConverstionUtil extends AbstractCoreDb
{
	private static final String NSE_ID = "nseId";
	private static final String YAHOO_ID = "yahooId";
	private static final String MONEY_CONTROL_ID = "moneyControlId";
	private static final String CONVERSION = "Conversion";

	static DatastoreService DATASTORE = DatastoreServiceFactory.getDatastoreService();

	public void insertConversion(String moneyControlId, String yahooId, String nseId)
	{
		if(StringUtil.isValidValue(moneyControlId)&& StringUtil.isValidValue(nseId))
		{
			Entity conversion = new Entity(CONVERSION);
			conversion.setProperty(MONEY_CONTROL_ID, moneyControlId);
			conversion.setProperty(YAHOO_ID, yahooId);
			conversion.setProperty(NSE_ID, nseId);
			DATASTORE.put(conversion);
		}
	}

	public List<MappingStockId> retrieveEntries()
	{
		List<Entity> entities = this.retrieveAllEntities(MONEY_CONTROL_ID);
		List<MappingStockId> mappings = FastList.newList();
		for (Entity entity : entities)
		{
			String moneyControId = (String) entity.getProperty(MONEY_CONTROL_ID);
			String yahooId = (String) entity.getProperty(YAHOO_ID);
			String nseId = (String) entity.getProperty(NSE_ID);
			if(moneyControId!=null && nseId!=null)
			{
				MappingStockId mappingStockId = new MappingStockId(moneyControId, yahooId, nseId);
				mappings.add(mappingStockId);		
			}
		}
		return mappings;
	}

	public void deleteRecords(String[] selectedMappings)
	{
		Filter selectedEntities = new FilterPredicate(NSE_ID, FilterOperator.IN, Arrays.asList(selectedMappings));
		Query query = new Query(CONVERSION).setFilter(selectedEntities);
		FastList<Entity> entitiesToBeDeleted = this.getEntitiesForQuery(query);
		FastList<Key> keys = entitiesToBeDeleted.collect(this.KEY_SELECTOR); 
		DATASTORE.delete(keys);
	}

	public String getNseIdForMoneyControlId(String moneyControlId)
	{
		Query query = new Query(CONVERSION).setFilter(new FilterPredicate(MONEY_CONTROL_ID, FilterOperator.EQUAL, moneyControlId));
		Entity entity = DATASTORE.prepare(query).asSingleEntity();
		return (String) entity.getProperty(NSE_ID);
	}

	@Override
	public String getTableName()
	{
		return CONVERSION;
	}
}
