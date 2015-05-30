package com.finanalyzer.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finanalyzer.domain.MappingStockId;
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
		String[] selectedMappings = request.getParameterValues("selectedMappings");
		boolean isDelete = "delete".equals(request.getParameter("action"));
		MaintainMappingProcessor processor = new MaintainMappingProcessor(moneyControlId, yahooBoId, nseId,selectedMappings,isDelete);
		List<MappingStockId> allMappingEntries = processor.execute();
		request.setAttribute("allMappingEntries",allMappingEntries);

		this.despatchTo(request, response, "maintainMapping.jsp");
	}

}
