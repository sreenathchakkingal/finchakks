package com.finanalyzer.endpoints;

import java.util.Collections;
import java.util.List;

import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.domain.jdo.NDaysHistoryDbObject;
import com.finanalyzer.domain.jdo.ProfitAndLossDbObject;
import com.finanalyzer.domain.jdo.UnrealizedDetailDbObject;
import com.finanalyzer.domain.jdo.UnrealizedSummaryDbObject;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.gs.collections.impl.utility.Iterate;

@Api(name = "initalizeControllerEndPoint", version = "v1")
public class InitializeControllerEndPoint {
	
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
	
	@ApiMethod(name = "listUnrealizedDetails")
	public List<UnrealizedDetailDbObject> listUnrealizedDetails()
	{
		JdoDbOperations<UnrealizedDetailDbObject> unrealizedDetailDbOperations = new JdoDbOperations<>(UnrealizedDetailDbObject.class);
		final List<UnrealizedDetailDbObject> unrealizedDetailObjects = unrealizedDetailDbOperations.getEntries("stockName asc, buyDate desc");

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
