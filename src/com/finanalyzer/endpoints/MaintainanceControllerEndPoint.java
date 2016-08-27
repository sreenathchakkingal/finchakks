package com.finanalyzer.endpoints;

import java.util.List;

import com.finanalyzer.domain.EndPointResponse;
import com.finanalyzer.domain.ModifiableStockAttributes;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.gs.collections.impl.list.mutable.FastList;

@Api(name = "maintainanceControllerEndPoint", version = "v1")
public class MaintainanceControllerEndPoint {
	
	@ApiMethod(name = "getModifiableStockAttributes", path="getModifiableStockAttributes")
	public List<ModifiableStockAttributes> getModifiableStockAttributes()
	{
		final ModifiableStockAttributes modifiableStockAttributes = new ModifiableStockAttributes("dummy","true", "good", "bad");
		return FastList.newListWith(modifiableStockAttributes);

	}
	
	@ApiMethod(name = "updateStockAttributes", path="updateStockAttributes", httpMethod = ApiMethod.HttpMethod.POST)
	public EndPointResponse updateStockAttributes(@Named("stockName") String stockName, 
			@Named("isWatchListed") String isWatchListed)
	{
		return new EndPointResponse(true, "all is well");

	}

}
