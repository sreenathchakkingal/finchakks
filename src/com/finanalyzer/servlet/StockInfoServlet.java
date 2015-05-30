package com.finanalyzer.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finanalyzer.domain.Stock;
import com.finanalyzer.processors.StockInfoProcessor;

@SuppressWarnings("serial")
public class StockInfoServlet extends AbstractCoreServlet
{

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		String stockName = request.getParameter("stockName");
		final String numberOfYears = request.getParameter("numberOfYears");
		final String historicalClosePrices = request.getParameter("historicalClosePrices");
		StockInfoProcessor processor = new StockInfoProcessor(stockName, numberOfYears,historicalClosePrices);
		Stock stock = processor.execute();

		request.setAttribute("stock", stock);
		request.setAttribute("screenerUrl", processor.getScreenerUrl());
		this.despatchTo(request, response, "stockInfo.jsp");

	}

	public void doPost1(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		response.setContentType("application/json");
		String stockName = request.getParameter("stockName");
		final String numberOfYears = request.getParameter("numberOfYears");

		final String splitRatios = request.getParameter("splitRatios");
		final String latesetSplitYear = request.getParameter("latesetSplitYear");
		final String bonusRatios = request.getParameter("bonusRatios");
		final String latestBonusYear = request.getParameter("latestBonusYear");

		StockInfoProcessor processor = new StockInfoProcessor(stockName, numberOfYears, splitRatios, latesetSplitYear, 
				bonusRatios, latestBonusYear);
		Stock stock = processor.execute();

		request.setAttribute("stock", stock);
		this.despatchTo(request, response, "stockInfo.jsp");

	}
}
