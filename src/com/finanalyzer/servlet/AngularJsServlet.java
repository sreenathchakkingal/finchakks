package com.finanalyzer.servlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
public class AngularJsServlet extends HttpServlet{
	 private static final long serialVersionUID = 1L;
	 
	  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	  {
	    StringBuffer sb = new StringBuffer();
	 
	    String user ="sreenath";
	 
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    out.write("A new user " + user + " has been created.");
	    out.flush();
	    out.close();
	  }
}
