package org.ac.cst8277.twitterClone.payloads;

public class ApiResponse {

	private String messageStatus;
	private String message;
	
	public ApiResponse() {
		// TODO Auto-generated constructor stub
	}
	
	public ApiResponse(String messageStatus, String message) {
		super();
		this.messageStatus = messageStatus;
		this.message = message;
	}
	public String getMessageStatus() {
		return messageStatus;
	}
	public void setMessageStatus(String messageStatus) {
		this.messageStatus = messageStatus;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
