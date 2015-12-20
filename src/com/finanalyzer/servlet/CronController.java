package com.finanalyzer.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.finanalyzer.domain.StockRatingValue;
 
@Controller
public class CronController {
 
	public static int total = 0; 
 
	@RequestMapping("/count")  
    public ModelAndView nDaysHistory(HttpServletRequest request,HttpServletResponse res) { 
		
		Integer numOfDays = Integer.parseInt(request.getParameter("numberOfYears"));
		total = total+numOfDays;
		return new ModelAndView("list", "total", total);  
	}
	
//	@RequestMapping(value="/count", method = RequestMethod.GET)
//	public String getCount(ModelMap model) {
// 
//		model.addAttribute("total", total++);
// 
//		return "list";
// 
//	}
  
}