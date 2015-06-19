package com.finanalyzer.db.jdo;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.finanalyzer.db.AllScripsUtil;
import com.finanalyzer.db.RatingDb;
import com.finanalyzer.db.StockIdConverstionUtil;
import com.finanalyzer.db.StockRatingsDb;
import com.finanalyzer.db.WatchListUtil;
import com.finanalyzer.domain.MappingStockId;
import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.StockExchange;
import com.finanalyzer.domain.StockRatingValue;
import com.finanalyzer.domain.StockRatingValuesEnum;
import com.finanalyzer.domain.jdo.AllScripsDbObject;
import com.finanalyzer.domain.jdo.RatingDbObject;
import com.finanalyzer.domain.jdo.UnrealizedDbObject;
import com.finanalyzer.util.DateUtil;
import com.finanalyzer.util.ReaderUtil;
import com.finanalyzer.util.StringUtil;
import com.google.appengine.api.datastore.Entity;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.set.mutable.UnifiedSet;

public class JdoDbOperations<T> {
	private final String ISIN = "isin";
	private final String BSE_ID = "bseId";
	private final String FAIR_VALUE = "fairValue";
	private final String INDUSTRY = "industry";
	public String STOCK_NAME = "stockName";
	public final String NSE_ID = "nseId";
	
	private Class<T> dbObjectClass;
	
	private static final Function<Entity, String> NSE_ID_COLLECTOR = new Function<Entity, String>() {

		@Override
		public String valueOf(Entity entity) {
			
			return (String) entity.getProperty("stockId");
		}
	};

	
	public JdoDbOperations(Class<T> dbObjectClass)
	{
		this.dbObjectClass=dbObjectClass;
	}
	
	public JdoDbOperations() {
		
	}

	public List<T> getEntries()
	{
		return this.getEntries(null);
	}

	public List<T> getEntries(String sortBy)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try
		{
			Query q = pm.newQuery(this.dbObjectClass);
			if(sortBy!=null)
			{
				q.setOrdering(sortBy);	
			}
			return (List<T>)q.execute();
		}
		finally
		{
			pm.close();
		}
	}
	
	public List<T> getEntries(String field, String value)
	{
		return this.getEntries(field, FastList.newListWith(value));
	}
	public List<T> getEntries(String field, List<String> values)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try
		{
			Query q = pm.newQuery(this.dbObjectClass, ":p.contains("+field+")");
			return (List<T>)q.execute(values);
		}
		finally
		{
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
	
	public List<UnrealizedDbObject> insertUnrealizedDataFromMoneycontrol(List<String> rawDataDFromMoneyControl)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try
		{
			List<UnrealizedDbObject> unrealizedDbObjects = FastList.newList(); 

			for (String row : rawDataDFromMoneyControl)
			{
				String[] stockAttributes = ReaderUtil.removeCommanBetweenQuotes(row).split(",");

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
			}
			return (List<UnrealizedDbObject>) pm.makePersistentAll(unrealizedDbObjects);	
		}
		finally
		{
			pm.close();
		}
	}
	
	public void populateAllScrips()
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try
		{
			AllScripsUtil allScripsUtil = AllScripsUtil.getInstance();
			
			List<Entity> entities = allScripsUtil.freshRetrieveAllEntities();
			
			List<AllScripsDbObject> allscrips = FastList.newList(); 
			for(Entity entity : entities)
			{
				String bse = (String) entity.getProperty(BSE_ID);
				String fv = String.valueOf(entity.getProperty(FAIR_VALUE));
				String ind = (String) entity.getProperty(INDUSTRY);
				String isin = (String) entity.getProperty(ISIN);
				String nse = (String) entity.getProperty(NSE_ID);
				String stockName = (String) entity.getProperty(STOCK_NAME);
				allscrips.add(new AllScripsDbObject(nse, stockName, isin, bse, fv, ind));
				
			}
			pm.makePersistentAll(allscrips);			
		}
		finally
		{
			pm.close();
		}

	}
	
	public void updateStockIdConversion() {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try
		{
			List<MappingStockId> moneycontrolMappings = new StockIdConverstionUtil().retrieveEntries();
			
			Query q = pm.newQuery(AllScripsDbObject.class);
			q.declareParameters("String nseIdParam");
			
			for (MappingStockId eachMapping : moneycontrolMappings)
			{
				q.setFilter("nseId == nseIdParam");
				
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
		}
		finally
		{
			pm.close();
		}
	}
	
	public void populateWatchList()
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try
		{
			WatchListUtil listUtil = new WatchListUtil();
			
			FastList<Entity> entities = FastList.newList(listUtil.freshRetrieveAllEntities());
			final List<String> existingWatchListEntries = entities.collect(NSE_ID_COLLECTOR);
			
			Query q = pm.newQuery(this.dbObjectClass, ":p.contains("+"nseId"+")");
			List<AllScripsDbObject> matchingEntries = (List<AllScripsDbObject>)q.execute(existingWatchListEntries);
			
			for (AllScripsDbObject allScripsDbObject : matchingEntries)
			{
				allScripsDbObject.setWatchListed(true);
			}
		}
		finally
		{
			pm.close();
		}
	}
	
	public void populateRatings()
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
		}
		
		finally
		{
			pm.close();
		}
	}
	
	public void populateScripsWithRatings()
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try
		{
			StockRatingsDb stockRatingsDb = new StockRatingsDb();
			FastList<Entity> entities = FastList.newList(stockRatingsDb.freshRetrieveAllEntities());
			Query q = null; 
			Set<String> distinctStockNames = UnifiedSet.newSet();
			
			for (Entity entity : entities)
			{
				distinctStockNames.add((String)entity.getProperty("STOCK_ID"));
			}
			
			for (String stockName : distinctStockNames)
			{
				final Map<String, StockRatingValuesEnum> ratingToValue = stockRatingsDb.getStockRatingValue(stockName).getRatingToValue();
				q = pm.newQuery(AllScripsDbObject.class, ":p.contains("+"nseId"+")");
				AllScripsDbObject matchingScrip = ((List<AllScripsDbObject>)q.execute(stockName)).get(0);
				matchingScrip.setRatingNameToValue(ratingToValue);
			}
		}
		
		finally
		{
			pm.close();
		}
	}
	
	
}
