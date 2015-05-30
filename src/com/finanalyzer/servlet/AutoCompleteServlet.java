package com.finanalyzer.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finanalyzer.db.AllScripsUtil;
import com.finanalyzer.domain.AutoCompleteData;
import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;
import com.gs.collections.impl.set.mutable.UnifiedSet;

public class AutoCompleteServlet extends HttpServlet
{

	private static final String SPACE = "\\s+";
	private static final long serialVersionUID = 1L;
	private static final int NUMBER_OF_RECORDS = 10;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("application/json");
		try
		{
			String[] terms = request.getParameter("term").split(SPACE);
			AllScripsUtil allScriptsUtil = AllScripsUtil.getInstance();
			List<Entity> entities = allScriptsUtil.retrieveAllEntities();
			Set<AutoCompleteData> matchingStockNames = UnifiedSet.newSet();

			for (Entity entity : entities)
			{
				boolean allTermsPresent = true;
				String stockName = (String) entity.getProperty(allScriptsUtil.STOCK_NAME);
				String nseId = (String) entity.getProperty(allScriptsUtil.NSE_ID);
				for (int i = 0; i < terms.length; i++)
				{
					if (!stockName.toLowerCase().contains(terms[i].toLowerCase()))
					{
						allTermsPresent=false;
						break;
					}
				}

				if (allTermsPresent)
				{
					matchingStockNames.add(new AutoCompleteData(stockName+" , "+nseId, nseId));
				}
				if(matchingStockNames.size()>=NUMBER_OF_RECORDS)
				{
					break;
				}
			}

			String searchList = new Gson().toJson(matchingStockNames);
			response.getWriter().write(searchList);
		} catch (Exception e)
		{
			System.err.println(e.getMessage());
		}
	}
}
