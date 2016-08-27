package com.finanalyzer.domain;

public class EndPointResponse {
	
	private boolean isSuccess;
	private String statusMessage;
	
	public EndPointResponse(boolean isSuccess, String statusMessage) 
	{
		this.isSuccess = isSuccess;
		this.statusMessage = statusMessage;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	
	@Override
	public String toString()
	{
		return this.isSuccess+" : "+this.statusMessage;
	}
	
}
