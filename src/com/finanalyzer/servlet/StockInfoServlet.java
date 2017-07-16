package com.finanalyzer.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.finanalyzer.domain.Stock;
import com.finanalyzer.processors.StockInfoProcessor;
import com.gs.collections.impl.map.mutable.UnifiedMap;


@Controller
public class StockInfoServlet
{

	@RequestMapping("stockInfo")
	public ModelAndView stockInfo(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		String stockName = request.getParameter("stockName");
		final String numberOfYears = request.getParameter("numberOfYears");
		final String historicalClosePrices = request.getParameter("historicalClosePrices");
		
		StockInfoProcessor processor = new StockInfoProcessor(stockName, numberOfYears,historicalClosePrices);
		Stock stock = processor.execute();

		Map<String, Object> result = UnifiedMap.newMap();
		result.put("stock", stock);
		
		return new ModelAndView("stockInfo", result);
		
	}
}
