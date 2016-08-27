package com.finanalyzer.endpoints;

import java.util.Collections;
import java.util.List;

import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.domain.builder.StopLossDbObjectBuilder;
import com.finanalyzer.domain.jdo.NDaysHistoryDbObject;
import com.finanalyzer.domain.jdo.NDaysHistoryFlattenedDbObject;
import com.finanalyzer.domain.jdo.ProfitAndLossDbObject;
import com.finanalyzer.domain.jdo.StockExceptionDbObject;
import com.finanalyzer.domain.jdo.StopLossDbObject;
import com.finanalyzer.domain.jdo.UnrealizedDetailDbObject;
import com.finanalyzer.domain.jdo.UnrealizedSummaryDbObject;
import com.finanalyzer.util.StringUtil;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.utility.Iterate;

@Api(name = "initalizeControllerEndPoint", version = "v1")
public class InitializeControllerEndPoint {

	@ApiMethod(name = "listExceptionStocks")
	public List<StockExceptionDbObject> listExceptionStocks()
	{
		JdoDbOperations<StockExceptionDbObject> stockExceptionDbOperations = new JdoDbOperations<>(StockExceptionDbObject.class);
		return stockExceptionDbOperations.getEntries("stockName");
	}
	
	@ApiMethod(name = "listStockTargets", path="listStockTargets")
	public List<StopLossDbObject> listStockTargets()
	{
		JdoDbOperations<StopLossDbObject> dbOperations = new JdoDbOperations<>(StopLossDbObject.class);
		final List<StopLossDbObject> dbObjects = dbOperations.getEntries("stockName");
		return dbObjects;
	}
	
	@ApiMethod(name = "listMissingWatchListStocks", path="listMissingWatchListStocks")
	public List<StopLossDbObject> listMissingWatchListStocks()
	{
		JdoDbOperations<NDaysHistoryFlattenedDbObject> dbOperations = new JdoDbOperations<>(NDaysHistoryFlattenedDbObject.class);
		final List<NDaysHistoryFlattenedDbObject> dbObjects = dbOperations.getEntries("stockName");
		
		JdoDbOperations<UnrealizedDetailDbObject> unrealizedDetailDbOperations = new JdoDbOperations<>(UnrealizedDetailDbObject.class);
		final List<UnrealizedDetailDbObject> unrealizedDetailDbObjects = unrealizedDetailDbOperations.getEntries("stockName");
		final List<StopLossDbObject> stopLossDbObjects = FastList.newList();
		for(UnrealizedDetailDbObject unrealizedDetailDbObject: unrealizedDetailDbObjects)
		{
			String unrealizedStockName = unrealizedDetailDbObject.getStockName();
			boolean isMatchFound=false;
			for (NDaysHistoryFlattenedDbObject nDaysHistoryFlattenedDbObject: dbObjects)
			{
				if(nDaysHistoryFlattenedDbObject.getStockName().equals(unrealizedStockName))
				{
					isMatchFound=true;
					break;
				}
			}
			if(!isMatchFound)
			{
				stopLossDbObjects.add(new StopLossDbObjectBuilder().stockName(unrealizedStockName).build());
			}
		}
		
		return stopLossDbObjects;
	}
	
	@ApiMethod(name = "listBlackListedStocks", path="listBlackListedStocks")
	public List<UnrealizedSummaryDbObject> listBlackListedStocks()
	{
		final List<UnrealizedSummaryDbObject> unrealizedSummaryDbObjects = listUnrealizedSummaryStocks();
		final List<UnrealizedSummaryDbObject> blackListedStocks = (List<UnrealizedSummaryDbObject>)Iterate.select(unrealizedSummaryDbObjects, UnrealizedSummaryDbObject.IS_BLACKLISTED);
		return blackListedStocks;
	}
	
	@ApiMethod(name = "listTargetReachedStocks", path="listTargetReachedStocks")
	public List<UnrealizedSummaryDbObject> listTargetReachedStocks()
	{
		final List<UnrealizedSummaryDbObject> unrealizedSummaryDbObjects = listUnrealizedSummaryStocks();
		final List<UnrealizedSummaryDbObject> stopLossStocks = (List<UnrealizedSummaryDbObject>)Iterate.select(unrealizedSummaryDbObjects, UnrealizedSummaryDbObject.IS_TARGET_REACHED);
		return stopLossStocks;
	}
	
	@ApiMethod(name = "listUnrealizedSummaryStocks", path="listUnrealizedSummaryStocks")
	public List<UnrealizedSummaryDbObject> listUnrealizedSummaryStocks()
	{
		JdoDbOperations<UnrealizedSummaryDbObject> unrealizedSummaryDbOperations = new JdoDbOperations<>(UnrealizedSummaryDbObject.class);
		final List<UnrealizedSummaryDbObject> unrealizedSummaryDbObjects = unrealizedSummaryDbOperations.getEntries("stockName");
		return unrealizedSummaryDbObjects;
	}
	
	@ApiMethod(name = "listNDaysHistoryStocks")
	public List<NDaysHistoryDbObject> listNDaysHistoryStocks()
	{
		JdoDbOperations<NDaysHistoryDbObject>  nDaysHistoryDbOperations = new JdoDbOperations<>(NDaysHistoryDbObject.class);
		final List<NDaysHistoryDbObject> ndaysHistoryDbObjects =nDaysHistoryDbOperations.getEntries();
		Collections.sort(ndaysHistoryDbObjects, NDaysHistoryDbObject.SIMPLE_AVG_NET_GAINS_COMPARATOR);
		
		return ndaysHistoryDbObjects;
	}	
	
	@ApiMethod(name = "listNDaysHistoryFlattenedStocks")
	public List<NDaysHistoryFlattenedDbObject> listNDaysHistoryFlattenedStocks()
	{
		JdoDbOperations<NDaysHistoryFlattenedDbObject>  nDaysHistoryFlattenedDbOperations = new JdoDbOperations<>(NDaysHistoryFlattenedDbObject.class);
		final List<NDaysHistoryFlattenedDbObject> ndaysHistoryFlattenedDbObjects =nDaysHistoryFlattenedDbOperations.getEntries();
		Collections.sort(ndaysHistoryFlattenedDbObjects, NDaysHistoryFlattenedDbObject.SIMPLE_AVG_NET_GAINS_COMPARATOR);
		return ndaysHistoryFlattenedDbObjects;
	}	
	
	@ApiMethod(name = "listUnrealizedDetails")
	public List<UnrealizedDetailDbObject> listUnrealizedDetails()
	{
		return getUnrealizedDetails(null);
	}
	
	@ApiMethod(name = "listSelectedUnrealizedDetails")
	public List<UnrealizedDetailDbObject> listSelectedUnrealizedDetails(@Named("stockName") String stockName)
	{
		return getUnrealizedDetails(stockName);
	}
	
	@ApiMethod(name = "getProfitAndLoss")
	public ProfitAndLossDbObject getProfitAndLoss()
	{
		JdoDbOperations<ProfitAndLossDbObject> profitAndLossDbOperations = new JdoDbOperations<>(ProfitAndLossDbObject.class);
		final ProfitAndLossDbObject profitAndLossDbObject = profitAndLossDbOperations.getEntries().get(0);
		return profitAndLossDbObject;
	}
	

//helper methods
	
	private List<UnrealizedDetailDbObject> getUnrealizedDetails(String stockName)
	{
		final String sortBy = "stockName asc, buyDate desc";
		JdoDbOperations<UnrealizedDetailDbObject> unrealizedDetailDbOperations = new JdoDbOperations<>(UnrealizedDetailDbObject.class);
		List<UnrealizedDetailDbObject> unrealizedDetailObjects=null;
		if(StringUtil.isValidValue(stockName))
		{
			unrealizedDetailObjects = unrealizedDetailDbOperations.getEntries("stockName", FastList.newListWith(stockName), sortBy);
			return unrealizedDetailObjects;
		}
		
		unrealizedDetailObjects = unrealizedDetailDbOperations.getEntries(sortBy);
		return unrealizedDetailObjects;
	}
	
	
	
	
}
