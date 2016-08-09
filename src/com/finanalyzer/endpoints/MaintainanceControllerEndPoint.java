package com.finanalyzer.endpoints;

import java.util.Date;
import java.util.List;

import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.domain.jdo.StopLossDbObject;
import com.finanalyzer.domain.jdo.UnrealizedDetailDbObject;
import com.finanalyzer.util.Adapter;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.gs.collections.impl.list.mutable.FastList;

@Api(name = "maintainanceControllerEndPoint", version = "v1")
public class MaintainanceControllerEndPoint {
	
	@ApiMethod(name = "stopLoss")
	public List<String> stopLoss(
			@Named("stockName") String stockName,
			@Named("targetReturnPercent") @Nullable float targetReturnPercent,
			@Named("targetSellPrice") @Nullable float targetSellPrice,
			@Named("targetDate") @Nullable Date targetDate)
	{
		return FastList.newListWith("dummy");
	}

}
