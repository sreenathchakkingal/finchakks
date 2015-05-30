package com.finanalyzer.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.finanalyzer.domain.Stock;
import com.finanalyzer.processors.RealizedPnLProcessor;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.gs.collections.impl.list.mutable.FastList;

public class RealizedPnLServlet extends AbstractCoreServlet
{
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		UserService userService = UserServiceFactory.getUserService();
		String thisURL = request.getRequestURI();
		
		if (request.getUserPrincipal()!=null && "sreenathchakkingal04@gmail.com".equals(request.getUserPrincipal().getName()))
		{
			InputStream statusInputStream = getResourceAsStream("RealizedPnL.csv");
			RealizedPnLProcessor processor = new RealizedPnLProcessor(statusInputStream);

			FastList<Stock> stocks = processor.execute();

			List<Stock> stocksSummary = processor.fetchStockSummaryStatus(stocks);

			request.setAttribute("stocks", stocks);
			request.setAttribute("stocksSummary", stocksSummary);

			despatchTo(request, response, "realizedPnL.jsp");
		}
		else
		{
			if(request.getUserPrincipal()!=null)
			{
				response.getWriter().println("<p>Hello, " +
                          request.getUserPrincipal().getName() +
                          "!  You can <a href=\"" +
                          userService.createLogoutURL(thisURL) +
                          "\">sign out</a>.</p>");
			}
			response.getWriter().println("<p> Only SreenathChakkingal can access. Please <a href=\"" +
                    userService.createLoginURL(thisURL) +
                    "\">sign in as SreenathChakkingal</a>.</p>");
		}


	}

}
