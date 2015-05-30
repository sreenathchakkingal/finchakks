package com.finanalyzer.db;

import java.util.List;

import com.finanalyzer.util.StringUtil;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.impl.list.mutable.FastList;

public abstract class CoreDb<T,R> 
{
	public final DatastoreService DATASTORE = DatastoreServiceFactory.getDatastoreService();
	private FastList<Entity> entities;
	public final String tableName;
	private List<String> columnNames;
	
	@SuppressWarnings("serial")
	public final Function<Entity, Key> KEY_SELECTOR = new Function<Entity, Key>()
	{

		@Override
		public Key valueOf(Entity entity)
		{
			return entity.getKey();
		}
	};
	
	
	public CoreDb(String tableName, List<String> columnNames)
	{
		this.tableName=tableName;
		this.columnNames=columnNames;
	}
	
	public void addEntry(List<String> columnValues)
	{
		if(columnValues.size()!=columnNames.size())
		{
			throw new RuntimeException("column names and values size does not match!!");
		}
		StringUtil.filterOutInvalidValues(columnValues);
		Entity entity = new Entity(this.getTableName());
		int count = 0;
		for (String columnName : this.columnNames) {
			entity.setProperty(columnName, columnValues.get(count++));
		}

		this.DATASTORE.put(entity);
		
		performCascadeAddition(columnValues);
	}
	
	
	public List<T> retrieveAllEntries()
	{
		return retrieveAllEntries(null);
	}
	
	public List<T> retrieveAllEntries(String sortBy)
	{
		List<Entity> rows = this.retrieveAllEntities(this.columnNames.get(0));
		List<T> domainObjects = FastList.newList();
		for (Entity eachRow : rows) {
				domainObjects.add(convertRowToDomainObject(eachRow));
		}
		return domainObjects;
	}
	
	public void removeEntries(R entriesToBeRemoved) {
		Filter selectedEntities = getFilterCriteriaForRemovingRecords(entriesToBeRemoved);
		Query query = new Query(this.getTableName())
				.setFilter(selectedEntities);
		removeEntries(query);
		Query casacadeQuery = getCascadeQueryForDeletion(entriesToBeRemoved);
		if(casacadeQuery!=null)
		{
			removeEntries(casacadeQuery);	
		}
	}

	public Query getCascadeQueryForDeletion(R entriesToBeRemoved) { //default implementation 
		return null;
	}
	
	public void performCascadeAddition(List<String> columnValues) { //default implementation 
		
	}

	private void removeEntries(Query query) {
		FastList<Entity> entitiesToBeDeleted = this.getEntitiesForQuery(query);
		FastList<Key> keys = entitiesToBeDeleted.collect(this.KEY_SELECTOR);
		this.DATASTORE.delete(keys);
	}

	public void removeAllEntities()
	{
		List<Key> keysToBeDeleted = this.retrieveAllEntityKeys();
		this.DATASTORE.delete(keysToBeDeleted);
	}

	public void freshRemoveAllEntities()
	{
		FastList<Key> keys = this.freshRetrieveAllEntities().collect(this.KEY_SELECTOR);
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
		Query query = new Query(this.getTableName());
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

	public String getTableName()
	{
		return this.tableName;
	}

	public List<T> retrieveSelectedEntries(R entriesToBeFetched)
	{
		List<Entity> rows = this.retrieveSelectedEntities(entriesToBeFetched);
		List<T> domainObjects = FastList.newList();
		for (Entity eachRow : rows) {
				domainObjects.add(convertRowToDomainObject(eachRow));
		}
		return domainObjects;
	}

	protected List<Entity> retrieveSelectedEntities(R entriesToBeFetched) {
		Filter filter = getFilterCriteriaForFetchingRecords(entriesToBeFetched);
		Query query = new Query(this.getTableName()).setFilter(filter);
		return this.getEntitiesForQuery(query);
	}

	public abstract T convertRowToDomainObject(Entity row);

	public abstract Filter getFilterCriteriaForFetchingRecords(R entriesToBeFetched); 

	public abstract Filter getFilterCriteriaForRemovingRecords(R entriesToBeRemoved); 
}
