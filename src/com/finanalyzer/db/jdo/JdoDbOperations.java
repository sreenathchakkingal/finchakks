package com.finanalyzer.db.jdo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.finanalyzer.db.RatingDb;
import com.finanalyzer.db.StockIdConverstionUtil;
import com.finanalyzer.domain.MappingStockId;
import com.finanalyzer.domain.jdo.AllScripsDbObject;
import com.finanalyzer.domain.jdo.RatingDbObject;
import com.finanalyzer.domain.jdo.UnrealizedDbObject;
import com.finanalyzer.util.ConverterUtil;
import com.finanalyzer.util.DateUtil;
import com.finanalyzer.util.ReaderUtil;
import com.finanalyzer.util.StringUtil;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.gs.collections.impl.list.mutable.FastList;

public class JdoDbOperations<T> {
	
	private static final Logger LOG =  Logger.getLogger(JdoDbOperations.class.getName());
	private static final MemcacheService SYNC_CACHE = MemcacheServiceFactory.getMemcacheService();
	
	private final boolean isCachingEnabled = true;
	private final String cacheName;
	
	private Class<T> dbObjectClass;
	
	public JdoDbOperations(Class<T> dbObjectClass)
	{
		this.dbObjectClass=dbObjectClass;
		this.cacheName = this.dbObjectClass.getName()+DateUtil.todaysDate();
		SYNC_CACHE.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
	}
	
	public T getNullOrOneEntry()
	{
		final List<T> entries = this.getEntries();
		if (entries.size()==0)
		{
			return null;
		}
		if(entries.size()==1)
		{
			return entries.get(0);
		}
		
		throw new RuntimeException("fetched more than one record "+entries.size());
	}
	
	public List<T> getEntries()
	{
		return this.getEntries(null, null, null);
	}

	public List<T> getEntries(String sortBy)
	{
		return getEntries(null, null, sortBy);
	}
	
	public List<T> getEntries(String field, List<String> values)
	{
		return this.getEntries(field, values, null);
	}
	
	public T getOneEntry(String field, List<String> values)
	{
		final List<T> entries = getEntries(field, values);
		if(entries.size()==1)
		{
			return entries.get(0);
		}
		throw new RuntimeException("fetched zero or more than one record "+entries.size());
	}
	
	public T getNullOrOneEntry(String field, List<String> values)
	{
		final List<T> entries = getEntries(field, values);
		if (entries.size()==0)
		{
			return null;
		}
		if(entries.size()==1)
		{
			return entries.get(0);
		}
		
		throw new RuntimeException("fetched more than one record "+entries.size());
	}
	
	public List<T> getEntries(String field, List<String> values, String sortBy)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = null;
		List<T> entries;
		
		try {
			if (field == null) 
			{
				q = pm.newQuery(this.dbObjectClass);
			} 
			else 
			{
				q = pm.newQuery(this.dbObjectClass, ":p.contains(" + field
						+ ")");
			}
			if (sortBy != null) 
			{
				q.setOrdering(sortBy);
			}
			
			if (isCachingEnabled) 
			{
				String cacheNameAppendedWithValues = values == null ? this.cacheName : this.cacheName+values.toString() ;
				entries = (List<T>) SYNC_CACHE.get(cacheNameAppendedWithValues);
				
				if (entries == null || entries.size()==0) 
				{
					LOG.info("no entries found in cache: "+cacheNameAppendedWithValues);
					entries = values == null ? (List<T>) q.execute() : (List<T>) q.execute(values);
					List<T> tempList = new ArrayList();
					tempList.addAll(entries);
					LOG.info("putting "+entries.size()+" entries in cache: "+cacheNameAppendedWithValues);
					SYNC_CACHE.put(cacheNameAppendedWithValues, tempList);
					LOG.info(entries.size()+" entries put into cache: "+cacheNameAppendedWithValues);
				}
				else
				{
					LOG.info("fetched: "+entries.size()+" from cache: "+this.cacheName);	
				}
			} 
			else 
			{
				LOG.info("caching disabled");
				entries = values == null ? (List<T>) q.execute() : (List<T>) q.execute(values);
			}
			
			return entries;

		} finally {
			pm.close();
		}
	}
	
	public void deleteEntries(String field, List<String> values)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = null;
		try
		{
			if(field!=null && StringUtil.isValidValue(values))
			{
				q =pm.newQuery(this.dbObjectClass, ":p.contains("+field+")");
			}
			else
			{
				q =pm.newQuery(this.dbObjectClass);
			}
			
			final List<T> entries =  (List<T>)q.execute(values);
			pm.deletePersistentAll(entries);
		}
		finally
		{
			pm.close();
		}
	}
	
	public void deleteEntries()
	{
		this.deleteEntries(null, null);
	}
	
	public Collection<T> deleteAndInsertEntries(List<T> dbObjects)
	{
		this.deleteEntries();
		return this.insertEntries(dbObjects);
	}
	
	public T insertEntry(T dbObject) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try
		{
			return pm.makePersistent(dbObject);
		}
		finally
		{
			pm.close();
		}
	}
	
	public Collection<T> insertEntries (List<T> dbObjects) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try
		{
			if(isCachingEnabled)
			{
				SYNC_CACHE.put(this.cacheName, dbObjects);
				LOG.info("inserted "+dbObjects.size()+" into to cache: "+this.cacheName);
			}
			else
			{
				LOG.info("caching disabled");
			}
			return pm.makePersistentAll(dbObjects);
		}
		finally
		{
			pm.close();
		}
	}
	
	public List<UnrealizedDbObject> insertUnrealizedDataFromMoneycontrol(List<String> rawDataDFromMoneyControl)
	{
		return this.insertUnrealizedDataFromMoneycontrol(rawDataDFromMoneyControl, false);
	}
			
	public List<UnrealizedDbObject> insertUnrealizedDataFromMoneycontrol(List<String> rawDataDFromMoneyControl, boolean isTabSeperated)
	{
		String splitBy = isTabSeperated ? "\t" : ",";
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try
		{
			List<UnrealizedDbObject> unrealizedDbObjects = ConverterUtil.convertMoneyControlDownloadToUnrealizedDbObjects(rawDataDFromMoneyControl);
			
			return (List<UnrealizedDbObject>) pm.makePersistentAll(unrealizedDbObjects);	
		}
		finally
		{
			pm.close();
		}
	}

	protected List<UnrealizedDbObject> convertMoneyControlDownloadToUnrealizedDbObjects(
			List<String> rawDataDFromMoneyControl, String splitBy) {
		List<UnrealizedDbObject> unrealizedDbObjects = FastList.newList(); 

		for (String row : rawDataDFromMoneyControl)
		{
			String[] stockAttributes = ReaderUtil.removeCommanBetweenQuotes(row);

			String name = ReaderUtil.parseStockName(stockAttributes[0]);
			
			int quantityColumnPosition = 5;
			int buyQuantity =  Integer.valueOf(stockAttributes[quantityColumnPosition]).intValue();

			int invoicePriceColumnPosition =  6;
			float buyPrice = Float.valueOf(stockAttributes[invoicePriceColumnPosition]);

			int invoiceDateColumnPosition = 7;
			String invoiceDate=stockAttributes[invoiceDateColumnPosition];

			String buyDate;
			if(invoiceDate.contains("/"))
			{
				buyDate = DateUtil.convertToStandardFormat("d/M/yyyy",invoiceDate);
			}
			else
			{
				buyDate = DateUtil.convertToStandardFormat("dd-MM-yyyy",invoiceDate);
			}
			unrealizedDbObjects.add(new UnrealizedDbObject(name, buyDate, buyPrice, buyQuantity));
			LOG.info("collecting stock: "+name+" for insertion");
		}
		return unrealizedDbObjects;
	}
	
	
	public boolean updateStockIdConversion() {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try
		{
			List<MappingStockId> moneycontrolMappings = new StockIdConverstionUtil().retrieveEntries();
			
			Query q = pm.newQuery(AllScripsDbObject.class);
			q.declareParameters("String nseIdParam");
			
			for (MappingStockId eachMapping : moneycontrolMappings)
			{
				q.setFilter(AllScripsDbObject.NSE_ID+" == nseIdParam");
				
				List<AllScripsDbObject> result = (List<AllScripsDbObject>)q.execute(eachMapping.getNseId());
				if(!result.isEmpty()){
					AllScripsDbObject scrip =result.get(0);
					scrip.setMoneycontrolName(eachMapping.getMoneyControlId());
					scrip.setYahooName(eachMapping.getYahooId());
				}
				else
				{
					System.out.println("not available for: "+eachMapping.getNseId());
				}
			}
			return true;
		}
		finally
		{
			pm.close();
		}
	}
	
	public boolean populateRatings()
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try
		{
			RatingDb ratingDb = new RatingDb();
			List<Entity> entities = FastList.newList(ratingDb.freshRetrieveAllEntities());
			List<RatingDbObject> dbObjects=  FastList.newList();
			
			for (Entity entity : entities)
			{
				String ratingName =(String)entity.getProperty("rating_name");
				dbObjects.add(new RatingDbObject(ratingName));
			}
			pm.makePersistentAll(dbObjects);
			return true;
		}
		
		finally
		{
			pm.close();
		}
	}
	
	
	public void deleteAllScrips() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = null;
		try
		{
			q =pm.newQuery(AllScripsDbObject.class);
			final List<T> entries =  (List<T>)q.execute();
			pm.deletePersistentAll(entries);
		}
		finally
		{
			pm.close();
		}
		
	}
	
}
