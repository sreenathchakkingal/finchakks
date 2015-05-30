package com.finanalyzer.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finanalyzer.processors.MaintainRatingProcessor;

public class MaintainRatingServlet extends AbstractCoreServlet
{

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		String rating = request.getParameter("ratingToBeAdded");
		String[] ratingsToBeRemoved = request.getParameterValues("ratingsToBeRemoved");
		MaintainRatingProcessor maintainRatingProcessor = new MaintainRatingProcessor(rating, ratingsToBeRemoved);
		List<String> ratings = maintainRatingProcessor.execute();
		request.setAttribute("ratings", ratings);
		this.despatchTo(request, response, "maintainRatings.jsp");
	}

}
