package com.finanalyzer.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.finanalyzer.processors.MaintainRatingProcessor;

@Controller
public class MaintainRatingController 
{

	@RequestMapping("/maintainRating")
	public ModelAndView maintainRating(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		String rating = request.getParameter("ratingToBeAdded");
		String[] ratingsToBeRemoved = request.getParameterValues("ratingsToBeRemoved");

		MaintainRatingProcessor maintainRatingProcessor = new MaintainRatingProcessor(rating, ratingsToBeRemoved);
		List<String> ratings = maintainRatingProcessor.execute();
		
		return new ModelAndView("maintainRatings", "ratings", ratings);
	}

}
