package com.finanalyzer.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractCoreServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	@Override
	public abstract void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
	{
		doPost(req, resp);
	}

	public InputStream getResourceAsStream(String fileName)
	{
		return getServletContext().getResourceAsStream("/WEB-INF/data/"+fileName);
	}
	
	public void despatchTo(HttpServletRequest request, HttpServletResponse response, String resultJsp) throws ServletException, IOException
	{
	    RequestDispatcher requestDispatcher = request.getRequestDispatcher(resultJsp);
	    requestDispatcher.forward(request, response);
	}
	
	
}
