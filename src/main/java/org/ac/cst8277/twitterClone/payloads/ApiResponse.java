package org.ac.cst8277.twitterClone.payloads;

public class ApiResponse {

	private String code;
	private String message;
	
	public ApiResponse() {
		// TODO Auto-generated constructor stub
	}
	
	public ApiResponse(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	public String getcode() {
		return code;
	}
	public void setcode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
