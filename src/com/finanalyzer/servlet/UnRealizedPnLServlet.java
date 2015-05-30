package com.finanalyzer.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.finanalyzer.domain.Stock;
import com.finanalyzer.processors.UnRealizedPnLProcessor;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.JsonObject;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.set.mutable.UnifiedSet;

public class UnRealizedPnLServlet extends AbstractCoreServlet
{
	private static final long serialVersionUID = 1L;

	//	private static final Predicate<Stock> VALID_SELL_DATE_AND_MATURITY = new Predicate<Stock>()
	//
	//			{
	//		private static final long serialVersionUID = 1L;
	//
	//		@Override
	//		public boolean accept(Stock stock)
	//		{
	//			boolean isMoreThanAQuarter = stock.getDifferenceBetweeBuyDateAndSellDate() > 3*DateUtil.NUMBER_OF_DAYS_IN_MONTH;
	//
	//			return (isMoreThanAQuarter ||"".equals(stock.getStockName()));
	//		}
	//			};


	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		UserService userService = UserServiceFactory.getUserService();
		String thisURL = request.getRequestURI();

		if (request.getUserPrincipal() != null && "sreenathchakkingal04@gmail.com".equals(request.getUserPrincipal().getName()))
		{

			String stockName = request.getParameter("stockName");

			ServletFileUpload upload = new ServletFileUpload();
			FileItemIterator fileItemIterator = null;
			try
			{
				fileItemIterator = upload.getItemIterator(request);
			} catch (FileUploadException e)
			{
				e.printStackTrace();
			}
			// InputStream statusInputStream =
			// getResourceAsStream("UnRealizedPnL.csv");
			// UnRealizedPnLProcessor processor = new
			// UnRealizedPnLProcessor(stream,stockName);

			UnRealizedPnLProcessor processor = new UnRealizedPnLProcessor(fileItemIterator, stockName);

			FastList<Stock> stocks = processor.execute();

			PartitionMutableList<Stock> stocksPartitioned = stocks.partition(UnRealizedPnLProcessor.MATURITY_MORE_THAN_A_QUARTER);

			List<Stock> requestedStockDetail = processor.getRequestedStockStatusInfo(stocksPartitioned.getSelected());

			FastList<Stock> stocksSummary = processor.fetchStockSummaryStatus((FastList<Stock>) stocksPartitioned.getSelected());
			List<Stock> requestedStockSummary = processor.getRequestedStockStatusInfo(stocksSummary);

			JsonObject stockInvestmentChart = processor.getStockInvestmentChart(stocksSummary);
			JsonObject requestedStockInvestmentChart = processor.getRequestedStockInvestmentChart(stockInvestmentChart);
			request.setAttribute("stocks", requestedStockDetail);
			request.setAttribute("stocksSummary", requestedStockSummary);
			request.setAttribute("stocksException", UnifiedSet.newSet(stocksPartitioned.getRejected()));
			request.setAttribute("stockInvestmentChart", requestedStockInvestmentChart);

			this.despatchTo(request, response, "unRealizedPnL.jsp");
		}

		else
		{
			if (request.getUserPrincipal() != null)
			{
				response.getWriter().println(
						"<p>Hello, " + request.getUserPrincipal().getName() + "!  You can <a href=\"" + userService.createLogoutURL(thisURL)
						+ "\">sign out</a>.</p>");
			}
			response.getWriter().println(
					"<p> Only SreenathChakkingal can access. Please <a href=\"" + userService.createLoginURL(thisURL)
					+ "\">sign in as SreenathChakkingal</a>.</p>");
		}

	}

}
