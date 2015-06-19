package com.finanalyzer.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finanalyzer.domain.NDaysPrice;
import com.finanalyzer.processors.MaintainWatchListProcessor;
import com.finanalyzer.processors.Processor;
import com.finanalyzer.processors.YahooNDaysPricesProcessor;
import com.finanalyzer.util.StringUtil;
import com.gs.collections.impl.list.mutable.FastList;

public class MaintainWatchListServlet extends AbstractCoreServlet
{
	private static final long serialVersionUID = 2360660420488401033L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String stockId = request.getParameter("stockIdToBeAdded");
		String[] stockIdsToBeRemoved = request.getParameterValues("stockIdsToBeRemoved");
		boolean isAddRequest = StringUtil.isValidValue(stockId);
		boolean isWriteRequest = isAddRequest || StringUtil.isValidValue(stockIdsToBeRemoved);
		List<String> stockIds =null;
		if(isWriteRequest)
		{
			stockIds =isAddRequest ? FastList.newListWith(stockId) : Arrays.asList(stockIdsToBeRemoved);	
		}
		Processor<List<String>> maintainWatchListProcessor = new MaintainWatchListProcessor(stockIds,isWriteRequest, isAddRequest);
		List<String> stocks = maintainWatchListProcessor.execute();
		request.setAttribute("stocks", stocks);
		this.despatchTo(request, response, "maintainWatchList.jsp");
	}

}
