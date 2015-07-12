package com.finanalyzer.servlet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class PnlController {
	

	protected boolean isAuthorizedUser(HttpServletRequest request) {
		return request.getUserPrincipal() != null && "sreenathchakkingal04@gmail.com".equals(request.getUserPrincipal().getName());
	}
	
	protected ModelAndView handleUnAuthUser(HttpServletRequest request)
	{
		UserService userService = UserServiceFactory.getUserService();
		String thisURL = request.getRequestURI();
		String message="";
		if (request.getUserPrincipal() != null)
		{
			message = "<p>Hello, " + request.getUserPrincipal().getName() + "!  You can <a href=\"" + userService.createLogoutURL(thisURL)
			+ "\">sign out</a>.</p>";
		}
		
		final String loginMessage = "<p> Only SreenathChakkingal can access. Please <a href=\"" + userService.createLoginURL(thisURL)
		+ "\">sign in as SreenathChakkingal</a>.</p>";
		
		message+=loginMessage;
		
		return new ModelAndView("authErrorPage", "message", message);
	}
}
