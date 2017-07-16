package com.finanalyzer.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finanalyzer.domain.NDaysPrice;
import com.finanalyzer.processors.Processor;
import com.finanalyzer.processors.YahooNDaysPricesProcessor;

@SuppressWarnings("serial")
//not used NdaysHistoryController is used. Can delete after few months of usage

public class NdaysHistoryServlet extends HttpServlet
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

	    RequestDispatcher requestDispatcher = request.getRequestDispatcher("nDaysHistory.jsp");
	    requestDispatcher.forward(request, response);

	}
	
	public InputStream getResourceAsStream(String fileName)
	{
		return getServletContext().getResourceAsStream("/WEB-INF/data/"+fileName);
	}

}
