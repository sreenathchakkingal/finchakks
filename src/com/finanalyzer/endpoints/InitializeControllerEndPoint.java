package com.finanalyzer.endpoints;

import java.util.Collections;
import java.util.List;

import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.domain.jdo.NDaysHistoryDbObject;
import com.finanalyzer.domain.jdo.NDaysHistoryFlattenedDbObject;
import com.finanalyzer.domain.jdo.ProfitAndLossDbObject;
import com.finanalyzer.domain.jdo.StockExceptionDbObject;
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
	
	@ApiMethod(name = "listBlackListedStocks")
	public List<UnrealizedSummaryDbObject> listBlackListedStocks()
	{
		JdoDbOperations<UnrealizedSummaryDbObject> unrealizedSummaryDbOperations = new JdoDbOperations<>(UnrealizedSummaryDbObject.class);
		final List<UnrealizedSummaryDbObject> unrealizedSummaryDbObjects = unrealizedSummaryDbOperations.getEntries("stockName");
		final List<UnrealizedSummaryDbObject> blackListedStocks = (List<UnrealizedSummaryDbObject>)Iterate.select(unrealizedSummaryDbObjects, UnrealizedSummaryDbObject.IS_BLACKLISTED);
		return blackListedStocks;
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
	
	@ApiMethod(name = "getProfitAndLoss")
	public ProfitAndLossDbObject getProfitAndLoss()
	{
		JdoDbOperations<ProfitAndLossDbObject> profitAndLossDbOperations = new JdoDbOperations<>(ProfitAndLossDbObject.class);
		final ProfitAndLossDbObject profitAndLossDbObject = profitAndLossDbOperations.getEntries().get(0);
		return profitAndLossDbObject;
	}
	
	
	
}
