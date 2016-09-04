package com.finanalyzer.endpoints;

import java.util.List;
import java.util.Map;

import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.domain.EndPointResponse;
import com.finanalyzer.domain.ModifiableStockAttributes;
import com.finanalyzer.domain.StockRatingValuesEnum;
import com.finanalyzer.domain.jdo.AllScripsDbObject;
import com.finanalyzer.domain.jdo.StopLossDbObject;
import com.finanalyzer.domain.jdo.UnrealizedSummaryDbObject;
import com.finanalyzer.util.Adapter;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.utility.Iterate;

@Api(name = "maintainanceControllerEndPoint", version = "v1")
public class MaintainanceControllerEndPoint {
	
	@ApiMethod(name = "getModifiableStockAttributes", path="getModifiableStockAttributes")
	public List<ModifiableStockAttributes> getModifiableStockAttributes(@Named("stockName") String stockName)
	{
		JdoDbOperations<AllScripsDbObject> allScripsDbOperations = new JdoDbOperations<>(AllScripsDbObject.class);
		final AllScripsDbObject allScripsDbObject = allScripsDbOperations.getOneEntry("nseId", FastList.newListWith(stockName));
		
		JdoDbOperations<StopLossDbObject> stopLossDbOperations = new JdoDbOperations<>(StopLossDbObject.class);
		final StopLossDbObject stopLossDbObject = stopLossDbOperations.getNullOrOneEntry("stockName", FastList.newListWith(stockName));
		ModifiableStockAttributes modifiableStockAttributes = Adapter.getModifiableStockAttributes(allScripsDbObject, stopLossDbObject);

		return FastList.newListWith(modifiableStockAttributes);
	}
	
	@ApiMethod(name = "updateStockAttributes", path="updateStockAttributes", httpMethod = ApiMethod.HttpMethod.POST)
	public EndPointResponse updateStockAttributes(
			@Named("stockName") String stockName,
			@Named("moneycontrolName") String moneycontrolName, 
			@Nullable @Named("isWatchListed") String isWatchListed,
			@Nullable @Named("lowerReturnPercentTarget") float lowerReturnPercentTarget,
			@Nullable @Named("upperReturnPercentTarget") float upperReturnPercentTarget,
			@Nullable @Named("stockRatings") List<String> stockRatings
			)
	
	{
		return new EndPointResponse(true, "all is well");

	}

}
