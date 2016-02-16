package com.finanalyzer.endpoints;

import java.util.Collections;
import java.util.List;

import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.domain.jdo.NDaysHistoryDbObject;
import com.finanalyzer.domain.jdo.UnrealizedSummaryDbObject;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.gs.collections.impl.utility.Iterate;

@Api(name = "initalizeControllerEndPoint", version = "v1")
public class NdaysHistoryEndPoint {
	
	@ApiMethod(name = "listBlackListedStocks")
	public List<UnrealizedSummaryDbObject> listBlackListedStocks()
	{
		JdoDbOperations<UnrealizedSummaryDbObject> unrealizedSummaryDbOperations = new JdoDbOperations<>(UnrealizedSummaryDbObject.class);
		final List<UnrealizedSummaryDbObject> unrealizedSummaryDbObjects = unrealizedSummaryDbOperations.getEntries("stockName");
		final List<UnrealizedSummaryDbObject> blackListedStocks = (List<UnrealizedSummaryDbObject>)Iterate.select(unrealizedSummaryDbObjects, UnrealizedSummaryDbObject.IS_BLACKLISTED);
		return blackListedStocks;
	}
}
