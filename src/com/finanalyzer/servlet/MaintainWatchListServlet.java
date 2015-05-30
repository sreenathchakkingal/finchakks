package com.finanalyzer.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finanalyzer.processors.MaintainWatchListProcessor;

public class MaintainWatchListServlet extends AbstractCoreServlet
{
	private static final long serialVersionUID = 2360660420488401033L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String stockId = request.getParameter("stockIdToBeAdded");
		String[] stockIdsToBeRemoved = request.getParameterValues("stockIdsToBeRemoved");
		MaintainWatchListProcessor maintainWatchListProcessor = new MaintainWatchListProcessor(stockId, stockIdsToBeRemoved);
		List<String> stocks = maintainWatchListProcessor.execute();
		request.setAttribute("stocks", stocks);
		this.despatchTo(request, response, "maintainWatchList.jsp");
	}

}
