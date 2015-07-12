package com.finanalyzer.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.finanalyzer.domain.jdo.AllScripsDbObject;
import com.finanalyzer.processors.MaintainMappingProcessor;

@Controller
public class MaintainMappingController
{
	@RequestMapping("/maintainMapping")
	public ModelAndView maintainMapping(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String moneyControlId = request.getParameter("moneyControlId");
		String yahooBoId = request.getParameter("yahooBoId");
		String nseId = request.getParameter("nseId");
		String bseId = request.getParameter("bseId");
		String[] selectedMappings = request.getParameterValues("selectedMappings");
		boolean isDelete = "delete".equals(request.getParameter("action"));
		
		MaintainMappingProcessor processor = new MaintainMappingProcessor(moneyControlId, yahooBoId, nseId, bseId, selectedMappings,isDelete);
		List<AllScripsDbObject> allMappingEntries = processor.execute();

		return new ModelAndView("maintainMapping", "allMappingEntries",allMappingEntries);
	}

}
