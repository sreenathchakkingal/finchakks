package com.finanalyzer.helloworld;

import org.springframework.stereotype.Controller;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.servlet.ModelAndView;  

@Controller  
public class HelloWorldController {

	@RequestMapping("/snDaysHistory")  
    public ModelAndView snDaysHistory() {  
        String message = "snDaysHistory";  
        return new ModelAndView("test", "message", message);  
    }  
	
	@RequestMapping("/hello")  
    public ModelAndView helloWorld() {  
        String message = "HELLO SPRING MVC HOW R U";  
        return new ModelAndView("test", "message", message);  
    }  
}
