package com.finanalyzer.servlet;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
 
@Controller

public class CronController1 {
 
	@RequestMapping(value="/addCount/{num}", method = RequestMethod.GET)
	public String addCount(@PathVariable int num, ModelMap model) {
 
		CronController.total += num; 
		model.addAttribute("total", CronController.total);
 
		return "list";
 
	}
//	@RequestMapping(value="/addCount", method = RequestMethod.GET)
//	public String addCount(ModelMap model) {
// 
//		model.addAttribute("total", CronController.total);
// 
//		return "list";
// 
//	}
 
}