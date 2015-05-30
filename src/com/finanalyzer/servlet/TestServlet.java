package com.finanalyzer.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finanalyzer.db.StockRatingsDb;

public class TestServlet extends AbstractCoreServlet
{
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		StockRatingsDb db = new StockRatingsDb();
		db.removeAllEntities();
	}
}

