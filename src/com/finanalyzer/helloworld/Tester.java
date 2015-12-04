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
	public static void main(String[] args) {
		float ratio=12/25f;
		float quantity=10;
		System.out.println(ratio);
		System.out.println(Math.round(10+ratio*quantity));
		
	}

}
