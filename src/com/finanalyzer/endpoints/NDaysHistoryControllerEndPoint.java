package com.finanalyzer.endpoints;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.jdo.NDaysHistoryDbObject;
import com.finanalyzer.domain.jdo.ProfitAndLossDbObject;
import com.finanalyzer.domain.jdo.StockExceptionDbObject;
import com.finanalyzer.domain.jdo.UnrealizedDetailDbObject;
import com.finanalyzer.domain.jdo.UnrealizedSummaryDbObject;
import com.finanalyzer.processors.Processor;
import com.finanalyzer.processors.QuandlNDaysPricesProcessor;
import com.finanalyzer.processors.UnRealizedPnLProcessor;
import com.finanalyzer.util.Adapter;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.utility.Iterate;

@Api(name = "nDaysHistoryControllerEndPoint", version = "v1")
public class NDaysHistoryControllerEndPoint {
	
	@ApiMethod(name = "refreshNDaysHistoryStocks")
	public Collection<NDaysHistoryDbObject> refreshNDaysHistoryStocks(@Named("numOfDays") String numOfDays, @Named("simpleMovingAverage") String simpleMovingAverage)
	{
		List<Stock> stocks = new QuandlNDaysPricesProcessor(numOfDays, simpleMovingAverage).execute();
		
		List<NDaysHistoryDbObject> ndaysHistoryDbObjects = stampDetailsFromUnrealized(stocks);
		
		return persistResult(ndaysHistoryDbObjects);
	}

	protected List<NDaysHistoryDbObject> stampDetailsFromUnrealized(List<Stock> stocks) 
	{
		List<NDaysHistoryDbObject> ndaysHistoryDbObjects = Adapter.stockToNdaysHistoryDbObject(stocks);

		JdoDbOperations<UnrealizedSummaryDbObject> unrealizedSummaryDbOperations = new JdoDbOperations<>(UnrealizedSummaryDbObject.class);
		JdoDbOperations<UnrealizedDetailDbObject> unrealizedDetailDbOperations = new JdoDbOperations<>(UnrealizedDetailDbObject.class);

		final List<UnrealizedSummaryDbObject> unrealizedSummaryDbObjects = unrealizedSummaryDbOperations.getEntries("stockName");
		final List<UnrealizedDetailDbObject> unrealizedDetailDbObjects = unrealizedDetailDbOperations.getEntries("buyDate asc");

		for (NDaysHistoryDbObject ndaysHistoryDbObject :  ndaysHistoryDbObjects)
		{
			for (UnrealizedSummaryDbObject unrealizedSummaryDbObject :  unrealizedSummaryDbObjects)
			{
				if(unrealizedSummaryDbObject.getStockName().equals(ndaysHistoryDbObject.getMoneyControlName()))
				{
					ndaysHistoryDbObject.setReturnTillDate(unrealizedSummaryDbObject.getReturnTillDate());
					ndaysHistoryDbObject.setImpactOnAverageReturn(unrealizedSummaryDbObject.getImpactOnAverageReturn());
				}
			}
			
			for (UnrealizedDetailDbObject unrealizedDetailDbObject :  unrealizedDetailDbObjects)
			{
				if(unrealizedDetailDbObject.getStockName().equals(ndaysHistoryDbObject.getMoneyControlName()))
				{
					ndaysHistoryDbObject.setDuration(unrealizedDetailDbObject.getDuration());
					ndaysHistoryDbObject.setBuyPrice(unrealizedDetailDbObject.getBuyPrice());
				}
			}
		}
		return ndaysHistoryDbObjects;
	}
	
	private Collection<NDaysHistoryDbObject> persistResult(List<NDaysHistoryDbObject> ndaysHistoryDbObjects) 
	{
		JdoDbOperations<NDaysHistoryDbObject>  dbOperations = new JdoDbOperations<>(NDaysHistoryDbObject.class);
		dbOperations.deleteEntries();
		return dbOperations.insertEntries(ndaysHistoryDbObjects);
	}  
}
