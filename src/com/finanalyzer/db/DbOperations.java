package com.finanalyzer.db;

import java.util.List;

import com.finanalyzer.domain.DbObject;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.gs.collections.impl.list.mutable.FastList;

public class DbOperations {
	
	
	public final DatastoreService DATASTORE = DatastoreServiceFactory.getDatastoreService();
	private static DbOperations instance;
	private FastList<Entity> entities=null;
	private DbOperations(){}

	public static DbOperations getInstance()
	{
		if(instance==null)
		{
			instance=new DbOperations();
		}
		return instance;
	}

	
	public void bulkInsert(List<DbObject> dbObjects)
	{
		List<Entity> entities = FastList.newList();
		
		for (DbObject dbObject: dbObjects)
		{
			Key key = KeyFactory.createKey(dbObject.getTableName(), dbObject.getPrimaryKey());
			entities.add(new Entity(key));
		}
		
		DATASTORE.put(entities);
	}

	public List<Entity> retrieveEntities(Query query, boolean refreshRetrieve) {
		if(this.entities==null || refreshRetrieve)
		{
			this.entities=FastList.newList(this.DATASTORE.prepare(query).asList(FetchOptions.Builder.withDefaults()));	
		}
		return this.entities;
	}

	public Entity retrieveEntity(Query query) {
		return this.DATASTORE.prepare(query).asSingleEntity();
	}

}