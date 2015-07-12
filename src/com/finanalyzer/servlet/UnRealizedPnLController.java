package com.finanalyzer.servlet;

import java.io.IOException;
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

import com.finanalyzer.domain.Stock;
import com.finanalyzer.processors.UnRealizedPnLProcessor;
import com.google.gson.JsonObject;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.set.mutable.UnifiedSet;

@Controller  
public class UnRealizedPnLController extends PnlController
{
	@RequestMapping("/unRealizedPnL") 
	public ModelAndView unRealizedPnL(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (isAuthorizedUser(request))
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

			FastList<Stock> stocks = processor.execute();

			PartitionMutableList<Stock> stocksPartitioned = stocks.partition(UnRealizedPnLProcessor.MATURITY_MORE_THAN_A_QUARTER);

			List<Stock> requestedStockDetail = processor.getRequestedStockStatusInfo(stocksPartitioned.getSelected());

			FastList<Stock> stocksSummary = processor.fetchStockSummaryStatus((FastList<Stock>) stocksPartitioned.getSelected());
			List<Stock> requestedStockSummary = processor.getRequestedStockStatusInfo(stocksSummary);

			JsonObject stockInvestmentChart = processor.getStockInvestmentChart(stocksSummary);
			JsonObject requestedStockInvestmentChart = processor.getRequestedStockInvestmentChart(stockInvestmentChart);
			
			Map<String, Object> result = UnifiedMap.newMap();
			result.put("stocks", requestedStockDetail);
			result.put("stocksSummary", requestedStockSummary);
			result.put("stocksException", UnifiedSet.newSet(stocksPartitioned.getRejected()));
			result.put("stockInvestmentChart", requestedStockInvestmentChart);
			
			return new ModelAndView("unRealizedPnL", result);
		}

		else
		{
			return handleUnAuthUser(request);
		}

	}
}
