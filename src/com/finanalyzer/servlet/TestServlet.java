package com.finanalyzer.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
 

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.resource.factory.SmsFactory;
import com.twilio.sdk.resource.instance.Account;

public class TestServlet extends HttpServlet
{
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		try {
			String ACCOUNT_SID = "AC52d77f234adb3ade2bca3dfc87d56dbe";
			String AUTH_TOKEN = "79df249d22997663ca019140420ff484";
 
			// Create a rest client
			final TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
 
			// Get the main account (The one we used to authenticate the client)
			final Account mainAccount = client.getAccount();
 
			// Send a text message
			final SmsFactory smsFactory = mainAccount.getSmsFactory();
			final Map<String, String> smsParams = new HashMap<String, String>();
			smsParams.put("To", "+919632610300"); // The number to send the text to
			smsParams.put("From", "+919632610300"); // A Twilio number you purchased
			smsParams.put("Body", "This is a test message!");
			smsFactory.create(smsParams);
 
			response.getWriter().println("text sent!");
 
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e); 
		}
	}
}

