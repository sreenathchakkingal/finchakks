package com.finanalyzer.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.jdo.ProfitAndLossDbObject;
import com.finanalyzer.domain.jdo.StockExceptionDbObject;
import com.finanalyzer.processors.UnRealizedPnLProcessor;
import com.google.gson.JsonObject;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.utility.Iterate;

@Controller  
public class UnRealizedPnLController extends PnlController
{
	private static final Logger LOG = Logger.getLogger(UnRealizedPnLController.class.getName());

	@RequestMapping("/unRealizedPnL") 
	public ModelAndView unRealizedPnL(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		LOG.info("in unRealizedPnL");
		String triggerSource = request==null ? "": request.getParameter("triggerFrom");
		final boolean sourceIsManual = "manual".equals(triggerSource);
		boolean isAuthorized = true; //for cron jobs we dont have to authorized as it just persists and does not display anything
		
		if(sourceIsManual)
		{
			isAuthorized=isAuthorizedUser(request);
		}
		
		if (isAuthorized)
		{
			ServletFileUpload upload = new ServletFileUpload();
			FileItemIterator fileItemIterator = null;
			try
			{
				fileItemIterator = upload.getItemIterator(request);
			} catch (Throwable e)
			{
				e.printStackTrace();
			}
			
			UnRealizedPnLProcessor processor = new UnRealizedPnLProcessor(fileItemIterator);

			LOG.info("before processor.execute()");
			Pair<List<Stock>,List<StockExceptionDbObject>> nonExceptionAndExceptionStocks = processor.execute();
			LOG.info("after processor.execute()");
			
			List<Stock> stocks = nonExceptionAndExceptionStocks.getOne();
			List<StockExceptionDbObject> exceptionStocks  = nonExceptionAndExceptionStocks.getTwo();
			
			final List<Stock> stocksWithMaturityMoreThan11Months = (List<Stock>)Iterate.select(stocks, UnRealizedPnLProcessor.MATURITY_MORE_THAN_11_MONTHS);
			List<Stock> stocksSummary = processor.fetchStockSummaryStatus(stocksWithMaturityMoreThan11Months);
			
			final List<StockExceptionDbObject> stocksMissingTargets = processor.enrichStocksWithTargets(stocksSummary);
			exceptionStocks.addAll(stocksMissingTargets);
			
			ProfitAndLossDbObject profitAndLoss = processor.getProfitAndLoss(stocksSummary);
			
			processor.calculateImpactOfEachStock(stocksSummary, profitAndLoss);
			
			JsonObject stockInvestmentChart = processor.getStockInvestmentChart(stocksSummary);
			JsonObject requestedStockInvestmentChart = processor.getRequestedStockInvestmentChart(stockInvestmentChart);
			
			Map<String, Object> result = UnifiedMap.newMap();
			result.put("stocksDetail", stocks);
			result.put("stocksSummary", stocksSummary);
			result.put("stocksException", UnifiedSet.newSet(exceptionStocks));
			result.put("stockInvestmentChart", requestedStockInvestmentChart);
			result.put("profitAndLoss", profitAndLoss);
			
			LOG.info("invoking persistResults");
			processor.persistResults(stocks, stocksSummary, profitAndLoss, exceptionStocks);
			
			LOG.info("out unRealizedPnL");
			
			return new ModelAndView("unRealizedPnL", result);
		}

		else
		{
			return handleUnAuthUser(request);
		}

	}
}
