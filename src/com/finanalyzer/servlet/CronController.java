package com.finanalyzer.servlet;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
 
@Controller

public class CronController {
 
	public static int total = 0; 
 
	@RequestMapping(value="/count", method = RequestMethod.GET)
	public String getCount(ModelMap model) {
 
		model.addAttribute("total", total++);
 
		return "list";
 
	}
  
}