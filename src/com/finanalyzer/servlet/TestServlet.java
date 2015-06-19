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
//		dbOperations.populateAllScrips();
//		dbOperations.updateStockIdConversion();
//		dbOperations.populateWatchList();
//		dbOperations.populateRatings();
		dbOperations.populateScripsWithRatings();
	}
}

