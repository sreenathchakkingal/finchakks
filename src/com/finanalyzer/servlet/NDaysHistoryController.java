package com.finanalyzer.servlet;

import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import sun.util.logging.resources.logging;

import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.domain.Stock;
import com.finanalyzer.domain.jdo.NDaysHistoryDbObject;
import com.finanalyzer.domain.jdo.NDaysHistoryFlattenedDbObject;
import com.finanalyzer.processors.Processor;
import com.finanalyzer.processors.QuandlNDaysPricesProcessor;
import com.finanalyzer.util.ConverterUtil;

@Controller  
public class NDaysHistoryController {
	
	private static final Logger LOG = Logger.getLogger(NDaysHistoryController.class.getName());

	@RequestMapping("/nDaysHistory")  
    public ModelAndView nDaysHistory(HttpServletRequest request,HttpServletResponse res) {  
		
		LOG.info("in nDaysHistory");
		
		String numOfDays = request==null ? "" :  request.getParameter("numOfDays");
		String simpleMovingAverage = request==null ? "": request.getParameter("simpleMovingAverage");
		
		Processor<List<Stock>> processor = new QuandlNDaysPricesProcessor(numOfDays, simpleMovingAverage);
		List<Stock> stocks = processor.execute();
		
//		persistResult(stocks);
		
		persistFlattnedResult(stocks);
		
		LOG.info("out nDaysHistory");
		
		return new ModelAndView("nDaysHistory", "stocks", stocks);  
    }

	private void persistFlattnedResult(List<Stock> stocks) {
		List<NDaysHistoryFlattenedDbObject> nDaysHistoryFlattenedDbObject = ConverterUtil.stockToNDaysHistoryFlattenedDbObject(stocks);
		JdoDbOperations<NDaysHistoryFlattenedDbObject>  dbOperations = new JdoDbOperations<>(NDaysHistoryFlattenedDbObject.class);
		dbOperations.deleteAndInsertEntries(nDaysHistoryFlattenedDbObject);
	}

	private void persistResult(List<Stock> stocks) 
	{
		List<NDaysHistoryDbObject> ndaysHistoryDbObjects = ConverterUtil.stockToNdaysHistoryDbObject(stocks);
		JdoDbOperations<NDaysHistoryDbObject>  dbOperations = new JdoDbOperations<>(NDaysHistoryDbObject.class);
		dbOperations.deleteAndInsertEntries(ndaysHistoryDbObjects);
	}  
}
