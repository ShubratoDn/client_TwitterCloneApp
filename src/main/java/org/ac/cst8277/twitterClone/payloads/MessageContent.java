package org.ac.cst8277.twitterClone.payloads;

import java.sql.Timestamp;

public class MessageContent {

	private String message;
	private Timestamp time;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	
	
}
