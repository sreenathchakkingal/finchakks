package com.finanalyzer.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.jdo.StockExceptionDbObject;
import com.finanalyzer.processors.RealizedPnLProcessor;
import com.finanalyzer.processors.StockInfoProcessor;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;

@Controller
public class RealizedPnLController extends PnlController
{
	@Autowired
	ServletContext context;
	
	@RequestMapping("realizedPnL")
	public ModelAndView realizedPnL(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		if (isAuthorizedUser(request))
		{
			InputStream statusInputStream = context.getResourceAsStream("/WEB-INF/data/RealizedPnL.csv");
			RealizedPnLProcessor processor = new RealizedPnLProcessor(statusInputStream);

			Pair<List<Stock>,List<StockExceptionDbObject>> nonExceptionAndExceptionStocks = processor.execute();
			List<Stock> stocks = nonExceptionAndExceptionStocks.getOne();

			List<Stock> stocksSummary = processor.fetchStockSummaryStatus(stocks);
			Map<String, List<Stock>> result = UnifiedMap.newMap();	
			result.put("stocks", stocks);
			result.put("stocksSummary", stocksSummary);
			
			return new ModelAndView("realizedPnL", result);
		}
		
		else
		{
			return handleUnAuthUser(request);
		}
	}

}
