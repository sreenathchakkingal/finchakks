package com.finanalyzer.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.finanalyzer.domain.Stock;
import com.finanalyzer.processors.Processor;
import com.finanalyzer.processors.QuandlNDaysPricesProcessor;

@Controller  
public class NDaysHistoryController {
	
	@RequestMapping("/nDaysHistory")  
    public ModelAndView nDaysHistory(HttpServletRequest request,HttpServletResponse res) {  
		String numOfDays = request.getParameter("numOfDays");
		String simpleMovingAverage = request.getParameter("simpleMovingAverage");

		Processor<List<Stock>> processor = new QuandlNDaysPricesProcessor(numOfDays, simpleMovingAverage);
		List<Stock> stocks = processor.execute();
		
//		request.setAttribute("stocks", stocks);
		
        return new ModelAndView("nDaysHistory", "stocks", stocks);  
    }  
}
