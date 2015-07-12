package com.finanalyzer.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.finanalyzer.db.RatingDb;
import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.StockRatingValue;
import com.finanalyzer.domain.StockRatingValuesEnum;
import com.finanalyzer.domain.jdo.AllScripsDbObject;
import com.finanalyzer.processors.MaintainStockRatingsProcessor;
import com.finanalyzer.processors.Processor;
import com.finanalyzer.util.StringUtil;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;

@Controller  
public class MaintainStockRatingsController
{

	private static final long serialVersionUID = 1L;

	@RequestMapping("/maintainStockRatings") 
	public ModelAndView maintainStockRatings(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		String stockName = request.getParameter("stockName");
		boolean isAddOrUpdate = request.getParameter("addOrUpdate") != null;
		
		Processor<AllScripsDbObject> processor = new MaintainStockRatingsProcessor(stockName, isAddOrUpdate, request.getParameterMap());

		AllScripsDbObject allScripsDbObject = processor.execute();

//		request.setAttribute("stock", allScripsDbObject);
		
		return new ModelAndView("maintainStockRatings", "stock", allScripsDbObject);  
	}
}
