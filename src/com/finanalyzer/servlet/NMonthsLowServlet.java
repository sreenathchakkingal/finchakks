package com.finanalyzer.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finanalyzer.domain.Stock;
import com.finanalyzer.processors.NMonthsLowProcessor;

public class NMonthsLowServlet extends AbstractCoreServlet
{
  private static final long serialVersionUID = 1L;
  
  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    InputStream stocksAsInputStream = getResourceAsStream("DataYahoo.txt");
    String numOfMonths = request.getParameter("numOfMonths");
    NMonthsLowProcessor processor = new NMonthsLowProcessor(numOfMonths, stocksAsInputStream);
    List<Stock> stocks = processor.execute();
    request.setAttribute("stocks", stocks);
    
    despatchTo(request, response, "nMonthsLow.jsp");
    
  }
}
