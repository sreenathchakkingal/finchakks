package com.finanalyzer.endpoints;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.jdo.NDaysHistoryDbObject;
import com.finanalyzer.domain.jdo.UnrealizedSummaryDbObject;
import com.finanalyzer.processors.Processor;
import com.finanalyzer.processors.QuandlNDaysPricesProcessor;
import com.finanalyzer.util.Adapter;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.gs.collections.impl.utility.Iterate;

@Api(name = "nDaysHistoryControllerEndPoint", version = "v1")
public class NDaysHistoryControllerEndPoint {
	
	@ApiMethod(name = "refreshNDaysHistoryStocks")
	public Collection<NDaysHistoryDbObject> refreshNDaysHistoryStocks(@Named("numOfDays") String numOfDays, @Named("simpleMovingAverage") String simpleMovingAverage)
	{
		List<Stock> stocks = new QuandlNDaysPricesProcessor(numOfDays, simpleMovingAverage).execute();
		return persistResult(stocks);
	}
	
	private Collection<NDaysHistoryDbObject> persistResult(List<Stock> stocks) 
	{
		List<NDaysHistoryDbObject> ndaysHistoryDbObjects = Adapter.stockToNdaysHistoryDbObject(stocks);
		JdoDbOperations<NDaysHistoryDbObject>  dbOperations = new JdoDbOperations<>(NDaysHistoryDbObject.class);
		dbOperations.deleteEntries();
		return dbOperations.insertEntries(ndaysHistoryDbObjects);
	}  
}
