package com.finanalyzer.servlet;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finanalyzer.domain.AutoCompleteData;
import com.finanalyzer.domain.StockRatingValuesEnum;
import com.google.gson.Gson;
import com.gs.collections.impl.set.mutable.UnifiedSet;

public class ValidRatingsAutoCompleteServlet extends HttpServlet
{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("application/json");
		try
		{
			String terms = request.getParameter("term");
			Set<AutoCompleteData> matchingRatings = UnifiedSet.newSet();
			StockRatingValuesEnum[] values = StockRatingValuesEnum.values();	
			for (StockRatingValuesEnum value : values)
			{
				if(value.getDescription().toLowerCase().contains(terms.toLowerCase()))
				{
					matchingRatings.add(new AutoCompleteData(value.getDescription()+" , "+value.getRating(), value.getDescription()));	
				}
			}

			String searchList = new Gson().toJson(matchingRatings);
			response.getWriter().write(searchList);
		} catch (Exception e)
		{
			System.err.println(e.getMessage());
		}
	}}
