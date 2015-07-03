package com.finanalyzer.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.domain.StockRatingValue;
import com.finanalyzer.domain.StockRatingValuesEnum;
import com.finanalyzer.domain.jdo.AllScripsDbObject;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;

public class TestServlet extends AbstractCoreServlet
{
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		JdoDbOperations dbOperations = new JdoDbOperations();
		if(request.getParameter("step1")!=null)
		{
			dbOperations.deleteAllScrips();	
		}
		else if(request.getParameter("step2")!=null)
		{
			dbOperations.populateAllScrips();	
		}
		else if(request.getParameter("step3")!=null)
		{
			dbOperations.updateStockIdConversion();	
		}		
		else if(request.getParameter("step4")!=null)
		{
			dbOperations.populateWatchList();	
		}	
		else if(request.getParameter("step5")!=null)
		{
			dbOperations.populateScripsWithRatings();	
		}	
//		final boolean populateRatings = dbOperations.();
//		final boolean populateScripsWithRatings = dbOperations.();
		
	}
}

