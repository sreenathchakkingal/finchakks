package com.finanalyzer.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finanalyzer.domain.NDaysPrice;
import com.finanalyzer.domain.Stock;
import com.finanalyzer.processors.Processor;
import com.finanalyzer.processors.QuandlNDaysPricesProcessor;
import com.finanalyzer.processors.YahooNDaysPricesProcessor;

@SuppressWarnings("serial")
public class NdaysHistoryServlet extends AbstractCoreServlet
{
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String numOfDays = request.getParameter("numOfDays");
		String source = request.getParameter("source");
		String simpleMovingAverage = request.getParameter("simpleMovingAverage");

		boolean isYahoo = "yahoo".equals(source);

		InputStream stocksAsInputStream = isYahoo ? this.getResourceAsStream("DataYahoo.txt") : this.getResourceAsStream("DataNse.txt");
		//for the ease of deletion 
		if (isYahoo)
		{
			Processor<List<NDaysPrice>> processor = new YahooNDaysPricesProcessor(stocksAsInputStream, numOfDays, source);
			List<NDaysPrice> nDaysPrices = processor.execute();
			Set<String> dates = ((YahooNDaysPricesProcessor) processor).getDistinctDates(nDaysPrices);
			request.setAttribute("nDaysPrices", nDaysPrices);
			request.setAttribute("dates", dates);
		} 
		else
		{
			Processor<List<Stock>> processor = new QuandlNDaysPricesProcessor(numOfDays, simpleMovingAverage);
			List<Stock> stocks = processor.execute();
			request.setAttribute("stocks", stocks);
			request.setAttribute("numOfDays", numOfDays);
		}

		this.despatchTo(request, response, "nDaysHistory.jsp");

	}

}
