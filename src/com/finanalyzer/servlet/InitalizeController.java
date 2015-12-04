package com.finanalyzer.servlet;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.finanalyzer.domain.Stock;
import com.finanalyzer.processors.UnRealizedPnLProcessor;
import com.finanalyzer.util.DateUtil;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.utility.Iterate;

public class InitalizeController implements Controller{
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse res) throws Exception {
if(false)
{
	PnlController controller = new PnlController();
	if(controller.isAuthorizedUser(request))
	{
		UnRealizedPnLProcessor unRealizedPnLProcessor = new UnRealizedPnLProcessor(null, null);
		final List<Stock> unrealizedCalculatedStocks = unRealizedPnLProcessor.execute();
		
		final Collection<Stock> lowReturnStocks = Iterate.select(unrealizedCalculatedStocks, new Predicate<Stock>() {

			@Override
			public boolean accept(Stock stock) {
				final int differenceBetweenTwoDates = DateUtil.differenceBetweenTwoDates(DateUtil.getCurrentDay(), stock.getBuyDate());
				return stock.getReturnTillDate()<10.0f && differenceBetweenTwoDates>90;
			}
		});
		
		return new ModelAndView("index", "stocks", lowReturnStocks);  	
	}
		
	return controller.handleUnAuthUser(request);
	
}
		return new ModelAndView("index");  	

	}

}
