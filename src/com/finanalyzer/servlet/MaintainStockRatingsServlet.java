package com.finanalyzer.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finanalyzer.db.RatingDb;
import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.StockRatingValue;
import com.finanalyzer.domain.StockRatingValuesEnum;
import com.finanalyzer.processors.MaintainStockRatingsProcessor;
import com.finanalyzer.processors.Processor;
import com.finanalyzer.util.StringUtil;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;

public class MaintainStockRatingsServlet extends AbstractCoreServlet
{

	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		String stockName = request.getParameter("stockName");
		boolean isAddOrUpdate = request.getParameter("Add/Update") != null;
		
		Processor<Stock> processor = new MaintainStockRatingsProcessor(
		stockName, isAddOrUpdate, request.getParameterMap());

		Stock stock = processor.execute();

		request.setAttribute("stock", stock);
		this.despatchTo(request, response, "maintainStockRatings.jsp");
	}
}
