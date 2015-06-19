package com.finanalyzer.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finanalyzer.domain.jdo.AllScripsDbObject;
import com.finanalyzer.processors.MaintainMappingProcessor;

public class MaintainMappingServlet extends AbstractCoreServlet
{
	private static final long serialVersionUID = -1069537395124551413L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String moneyControlId = request.getParameter("moneyControlId");
		String yahooBoId = request.getParameter("yahooBoId");
		String nseId = request.getParameter("nseId");
		String bseId = request.getParameter("bseId");
		String[] selectedMappings = request.getParameterValues("selectedMappings");
		boolean isDelete = "delete".equals(request.getParameter("action"));
		MaintainMappingProcessor processor = new MaintainMappingProcessor(moneyControlId, yahooBoId, nseId, bseId, selectedMappings,isDelete);
		List<AllScripsDbObject> allMappingEntries = processor.execute();
		request.setAttribute("allMappingEntries",allMappingEntries);

		this.despatchTo(request, response, "maintainMapping.jsp");
	}

}
