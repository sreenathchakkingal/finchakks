package com.finanalyzer.endpoints;

import java.util.Collections;
import java.util.List;

import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.domain.UnrealizedWrapper;
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
	
	@ApiMethod(name = "listSelectedUnrealized", path="listSelectedUnrealized")
	public UnrealizedWrapper listSelectedUnrealized(@Named("stockName") String stockName)
	{
		final List<UnrealizedDetailDbObject> unrealizedDetail = listSelectedUnrealizedDetails(stockName);
		final UnrealizedSummaryDbObject unrealizedSummary = listSelectedUnrealizedSummary(stockName);
		return new UnrealizedWrapper(unrealizedSummary, unrealizedDetail);
	}
	
	@ApiMethod(name = "listUnrealizedDetails")
	public List<UnrealizedDetailDbObject> listUnrealizedDetails()
	{
		return getUnrealizedDetails(null);
	}
	
	//move this to helper method - this api will be needed once listSelectedUnrealized api is productionized
	@ApiMethod(name = "listSelectedUnrealizedDetails")
	public List<UnrealizedDetailDbObject> listSelectedUnrealizedDetails(@Named("stockName") String stockName)
	{
		return getUnrealizedDetails(stockName);
	}
	
	@ApiMethod(name = "listUnrealizedSummaryStocks", path="listUnrealizedSummaryStocks")
	public List<UnrealizedSummaryDbObject> listUnrealizedSummaryStocks()
	{
		return getUnrealizedSummary(null);
	}
	
	@ApiMethod(name = "getProfitAndLoss")
	public ProfitAndLossDbObject getProfitAndLoss()
	{
		JdoDbOperations<ProfitAndLossDbObject> profitAndLossDbOperations = new JdoDbOperations<>(ProfitAndLossDbObject.class);
		final ProfitAndLossDbObject profitAndLossDbObject = profitAndLossDbOperations.getEntries().get(0);
		return profitAndLossDbObject;
	}
	
	@ApiMethod(name = "listTargetReachedStocks", path="listTargetReachedStocks")
	public List<UnrealizedSummaryDbObject> listTargetReachedStocks()
	{
		final List<UnrealizedSummaryDbObject> unrealizedSummaryDbObjects = listUnrealizedSummaryStocks();
		final List<UnrealizedSummaryDbObject> stopLossStocks = (List<UnrealizedSummaryDbObject>)Iterate.select(unrealizedSummaryDbObjects, UnrealizedSummaryDbObject.IS_TARGET_REACHED);
		return stopLossStocks;
	}
	
	@ApiMethod(name = "listSelectedTargetHistory", path="listSelectedTargetHistory")
	public List<StopLossDbObject> listSelectedTargetHistory(@Named("stockName") String stockName)
	{
		JdoDbOperations<StopLossDbObject> targetHistoryDbOperations = new JdoDbOperations<>(StopLossDbObject.class);
		final List<StopLossDbObject> stopLossDbObjects = targetHistoryDbOperations.getEntries("stockName", FastList.newListWith(stockName.toUpperCase()), "businessDate desc");
		return stopLossDbObjects;
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
	
	private List<UnrealizedSummaryDbObject> getUnrealizedSummary(String stockName)
	{
		final String sortBy = "stockName";
		JdoDbOperations<UnrealizedSummaryDbObject> unrealizedSummaryDbOperations = new JdoDbOperations<>(UnrealizedSummaryDbObject.class);
		List<UnrealizedSummaryDbObject> unrealizedSummaryDbObjects=null;
		if(StringUtil.isValidValue(stockName))
		{
			unrealizedSummaryDbObjects = unrealizedSummaryDbOperations.getEntries("stockName", FastList.newListWith(stockName), sortBy);
			return unrealizedSummaryDbObjects;
		}
		
		unrealizedSummaryDbObjects = unrealizedSummaryDbOperations.getEntries(sortBy);
		return unrealizedSummaryDbObjects;
	}
	
	//dont know why - but the @Named notation is required. if remove the api that calls this method stops working
	public UnrealizedSummaryDbObject listSelectedUnrealizedSummary(@Named("stockName") String stockName)
	{
		return getUnrealizedSummary(stockName).get(0);
	}
}
