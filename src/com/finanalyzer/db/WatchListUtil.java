package com.finanalyzer.db;

import java.util.Arrays;
import java.util.List;

import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.StockExchange;
import com.finanalyzer.util.StringUtil;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.gs.collections.impl.list.mutable.FastList;

public class WatchListUtil extends AbstractCoreDb
{

	private static final String WATCH_LIST = "WatchList";
	private static final String STOCK_ID = "stockId";

	public void addToWatchList(String stockId)
	{
		if(StringUtil.isValidValue(stockId))
		{
			Entity watchListEntry = new Entity(this.getTableName());
			watchListEntry.setProperty(STOCK_ID, stockId);
			this.DATASTORE.put(watchListEntry);
		}
	}

	public List<String> retrieveWatchList()
	{
		List<Entity> entities = this.retrieveAllEntities(STOCK_ID);

		List<String> stockIdentifiers = FastList.newList();
		for (Entity entity : entities)
		{
			String stockId = (String) entity.getProperty(STOCK_ID);
			if(stockId!=null && !"".equals(stockId))
			{
				stockIdentifiers.add(stockId);
			}
		}
		return stockIdentifiers;
	}

	public FastList<Stock> retrieveWatchListAsStocks()
	{
		return this.retrieveWatchListAsStocks(StockExchange.NSE);
	}

	public FastList<Stock> retrieveWatchListAsStocks(StockExchange stockExchange)
	{
		List<Entity> entities = this.retrieveAllEntities(STOCK_ID);
		FastList<Stock> stocks = FastList.newList();
		AllScripsUtil allScripsUtil = AllScripsUtil.getInstance();
		for (Entity entity : entities)
		{
			String nseId = (String) entity.getProperty(STOCK_ID);
			if(nseId!=null && !"".equals(nseId))
			{
				if(stockExchange==StockExchange.NSE)
				{
					stocks.add(new Stock(nseId));	
				}
				else
				{
					final String bseId = allScripsUtil.getBseIdFromNseId(nseId);
					if(bseId!=null && !"".equals(bseId))
					{
						stocks.add(new Stock(bseId, StockExchange.BSE));
					}	
				}


			}
		}
		return stocks;
	}


	//	public FastList<Stock> retrieveWatchListAsStocksBse(StockExchange stockExchange)
	//	{
	//		List<Entity> entities = this.retrieveAllEntities(STOCK_ID);
	//		FastList<Stock> stocks = FastList.newList();
	//		for (Entity entity : entities)
	//		{
	//			String nseId = (String) entity.getProperty(STOCK_ID);
	//
	//			if(nseId!=null && !"".equals(nseId))
	//			{
	//				AllScripsUtil allScripsUtil = new AllScripsUtil();
	//				final String bseId = allScripsUtil.getBseIdFromNseId(nseId);
	//				if(bseId!=null && !"".equals(bseId))
	//				{
	//					stocks.add(new Stock(bseId));
	//				}
	//			}
	//		}
	//		return stocks;
	//	}

	public void removeFromWatchList(String[] stocksToBeRemoved)
	{
		if (StringUtil.isValidValue(stocksToBeRemoved))
		{
			Filter selectedEntities = new FilterPredicate(STOCK_ID, FilterOperator.IN, Arrays.asList(stocksToBeRemoved));
			Query query = new Query(this.getTableName()).setFilter(selectedEntities);
			FastList<Entity> entitiesToBeDeleted = this.getEntitiesForQuery(query);
			FastList<Key> keys = entitiesToBeDeleted.collect(this.KEY_SELECTOR); 
			this.DATASTORE.delete(keys);			
		}
	}

	@Override
	public String getTableName()
	{
		return WATCH_LIST;
	}

	//	public static void addAllOneTime(List<String> watchListFromFile)
	//	{
	//		for (String eachEntry : watchListFromFile)
	//		{
	//			Entity wathcListEntry = new Entity(WATCH_LIST);
	//			wathcListEntry.setProperty(STOCK_ID, eachEntry);
	//			DATASTORE.put(wathcListEntry);
	//		}
	//	}

}
