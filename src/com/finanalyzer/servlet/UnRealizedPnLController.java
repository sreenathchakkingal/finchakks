package com.finanalyzer.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.jdo.UnrealizedSummaryDbObject;
import com.finanalyzer.processors.UnRealizedPnLProcessor;
import com.finanalyzer.util.Adapter;
import com.google.gson.JsonObject;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.utility.Iterate;

@Controller  
public class UnRealizedPnLController extends PnlController
{
	@RequestMapping("/unRealizedPnL") 
	public ModelAndView unRealizedPnL(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String triggerSource = request.getParameter("triggerFrom");
		final boolean sourceIsManual = "manual".equals(triggerSource);
		boolean isAuthorized = true; //for cron jobs we dont have to authorized as it just persists and does not display anything
		
		if(sourceIsManual)
		{
			isAuthorized=isAuthorizedUser(request);
		}
		
		if (isAuthorized)
		{

			String stockName = request.getParameter("stockName");

			ServletFileUpload upload = new ServletFileUpload();
			FileItemIterator fileItemIterator = null;
			try
			{
				fileItemIterator = upload.getItemIterator(request);
			} catch (FileUploadException e)
			{
				e.printStackTrace();
			}

			UnRealizedPnLProcessor processor = new UnRealizedPnLProcessor(fileItemIterator, stockName);

			Pair<List<Stock>,List<Stock>> nonExceptionAndExceptionStocks = processor.execute();

			List<Stock> stocks = nonExceptionAndExceptionStocks.getOne();
			List<Stock> exceptionStocks  = nonExceptionAndExceptionStocks.getTwo();
			
			List<Stock> requestedStockDetail = processor.getRequestedStockStatusInfo(stocks);

			final List<Stock> stocksWithMaturityMoreThanAQuarter = (List<Stock>)Iterate.select(stocks, UnRealizedPnLProcessor.MATURITY_MORE_THAN_A_QUARTER);
			List<Stock> stocksSummary = processor.fetchStockSummaryStatus(stocksWithMaturityMoreThanAQuarter);
			List<Stock> requestedStockSummary = processor.getRequestedStockStatusInfo(stocksSummary);

			JsonObject stockInvestmentChart = processor.getStockInvestmentChart(stocksSummary);
			JsonObject requestedStockInvestmentChart = processor.getRequestedStockInvestmentChart(stockInvestmentChart);
			
			Map<String, Object> result = UnifiedMap.newMap();
			result.put("stocksDetail", requestedStockDetail);
			result.put("stocksSummary", requestedStockSummary);
			result.put("stocksException", UnifiedSet.newSet(exceptionStocks));
			result.put("stockInvestmentChart", requestedStockInvestmentChart);
			
			processor.persistResults(stocks, stocksSummary);
			
			return new ModelAndView("unRealizedPnL", result);
		}

		else
		{
			return handleUnAuthUser(request);
		}

	}
}
