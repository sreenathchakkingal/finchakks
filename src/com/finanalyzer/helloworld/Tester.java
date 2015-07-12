package com.finanalyzer.helloworld;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
 
import java.util.ArrayList;
import java.util.List;

public class Tester {
	
		public static final String ACCOUNT_SID = "AC52d77f234adb3ade2bca3dfc87d56dbe";
		  public static final String AUTH_TOKEN = "79df249d22997663ca019140420ff484";
		 
		  public static void main(String[] args) throws TwilioRestException {
		    TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
		 
		    // Build a filter for the MessageList
		    List<NameValuePair> params = new ArrayList<NameValuePair>();
		    params.add(new BasicNameValuePair("Body", "My Test message"));
		    params.add(new BasicNameValuePair("To", "+919632610300"));
		    params.add(new BasicNameValuePair("From", "+919632610300"));
		 
		    MessageFactory messageFactory = client.getAccount().getMessageFactory();
		    Message message = messageFactory.create(params);
		    System.out.println(message.getSid());
		
	}
}
