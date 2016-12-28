package com.finanalyzer.endpoints;

import java.util.List;

import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.jdo.ProfitAndLossDbObject;
import com.finanalyzer.domain.jdo.StockExceptionDbObject;
import com.finanalyzer.processors.UnRealizedPnLProcessor;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.gs.collections.api.tuple.Pair;
//used only in angular
@Api(name = "unrealizedDetailsControllerEndPoint", version = "v1")
public class UnrealizedDetailsControllerEndPoint {
	
	@ApiMethod(name = "refreshUnrealizedDetails")
	public void refreshUnrealizedDetails(@Named("unrealizedDetailsContent") String unrealizedDetailsContent)
	{
		final UnRealizedPnLProcessor processor = new UnRealizedPnLProcessor(unrealizedDetailsContent);
		final Pair<List<Stock>,List<StockExceptionDbObject>> stockDetailsAndExceptions = processor.execute();
		
		final List<Stock> stocksDetail = stockDetailsAndExceptions.getOne();
		final List<StockExceptionDbObject> exceptionStocks = stockDetailsAndExceptions.getTwo();
		
		List<Stock> stocksSummary = processor.fetchStockSummaryStatus(stocksDetail);
		ProfitAndLossDbObject profitAndLoss = processor.getProfitAndLoss(stocksSummary);
		processor.calculateImpactOfEachStock(stocksSummary, profitAndLoss);
		
		processor.persistResults(stocksDetail, stocksSummary, profitAndLoss, exceptionStocks);
	}
}
