package com.finanalyzer.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.finanalyzer.domain.NDaysPrice;
import com.finanalyzer.domain.jdo.AllScripsDbObject;
import com.finanalyzer.processors.MaintainProcessor;
import com.finanalyzer.processors.MaintainWatchListProcessor;
import com.finanalyzer.processors.Processor;
import com.finanalyzer.processors.YahooNDaysPricesProcessor;
import com.finanalyzer.util.StringUtil;
import com.gs.collections.impl.list.mutable.FastList;

@Controller
public class MaintainWatchListController
{
	@RequestMapping("/maintainWatchList1")
	public ModelAndView maintainWatchList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
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
		
		MaintainProcessor maintainWatchListProcessor = new MaintainWatchListProcessor
				(stockIds,isWriteRequest, isAddRequest, AllScripsDbObject.IS_WATCHLISTED);
		List<String> stocks = maintainWatchListProcessor.execute();
		
		return new ModelAndView("maintainWatchList","stocks",stocks );
	}

}
