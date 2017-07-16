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

import com.finanalyzer.processors.MaintainBlackListProcessor;
import com.finanalyzer.processors.MaintainProcessor;
import com.finanalyzer.processors.MaintainWatchListProcessor;
import com.finanalyzer.util.StringUtil;
import com.gs.collections.impl.list.mutable.FastList;

@Controller
public class MaintainController
{
	
	@RequestMapping("/maintainList")
	public ModelAndView maintainWatchList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		String stockIdToBeAdded = request.getParameter("stockIdToBeAdded");
		String[] stockIdsToBeRemoved = request.getParameterValues("stockIdsToBeRemoved");
		String actionName = request.getParameter("action");
		
		boolean isAddRequest = StringUtil.isValidValue(stockIdToBeAdded);
		boolean isWriteRequest = isAddRequest || StringUtil.isValidValue(stockIdsToBeRemoved);
		List<String> stockIds =null;
		
		if(isWriteRequest)
		{
			stockIds =isAddRequest ? FastList.newListWith(stockIdToBeAdded) : Arrays.asList(stockIdsToBeRemoved);	
		}
		
		MaintainProcessor maintainProcessor="watchList".equals(actionName) ? new MaintainWatchListProcessor(stockIds,isWriteRequest, isAddRequest) : 
			new MaintainBlackListProcessor(stockIds,isWriteRequest, isAddRequest);
		
		List<String> stocks = maintainProcessor.execute();
		
		return new ModelAndView(maintainProcessor.getViewName(),"stocks",stocks );
	}

}
