package com.finanalyzer.db;

import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.impl.list.mutable.FastList;

public abstract class AbstractCoreDb
{
	public final DatastoreService DATASTORE = DatastoreServiceFactory.getDatastoreService();
	public String STOCK_NAME = "stockName";
	public final String NSE_ID = "nseId";
	private FastList<Entity> entities=null;

	@SuppressWarnings("serial")
	public final Function<Entity, Key> KEY_SELECTOR = new Function<Entity, Key>()
	{

		@Override
		public Key valueOf(Entity entity)
		{
			return entity.getKey();
		}
	};

	public abstract String getTableName();

	public void removeAllEntities()
	{
		List<Key> keysToBeDeleted = this.retrieveAllEntityKeys();
		this.DATASTORE.delete(keysToBeDeleted);
	}

	public void freshRemoveAllEntities()
	{
		List<Key> keys = this.freshRetrieveAllEntities().collect(this.KEY_SELECTOR);
		this.DATASTORE.delete(keys);
	}
	
	public List<Key> retrieveAllEntityKeys()
	{
		FastList<Entity> entities = this.retrieveAllEntities();
		FastList<Key> keys = entities.collect(this.KEY_SELECTOR);
		return keys;
	}

	public FastList<Entity> retrieveAllEntities()
	{
		if(this.entities==null)
		{
			this.entities=this.retrieveAllEntities(null);	
		}
		return this.entities;
	}

	public FastList<Entity> freshRetrieveAllEntities()
	{
		return this.retrieveAllEntities(null);
	}
	
	public FastList<Entity> retrieveAllEntities(String sortBy)
	{
		Query query = this.getQueryForTable();
		if(sortBy!=null)
		{
			query=query.addSort(sortBy);
		}
		return this.getEntitiesForQuery(query);
	}

	public FastList<Entity> getEntitiesForQuery(final Query query)
	{
		return FastList.newList(this.DATASTORE.prepare(query).asList(FetchOptions.Builder.withDefaults()));
	}

	public int getSize()
	{
		return this.DATASTORE.prepare(this.getQueryForTable()).countEntities(FetchOptions.Builder.withDefaults());
	}

	private Query getQueryForTable()
	{
		return new Query(this.getTableName());
	}
}
