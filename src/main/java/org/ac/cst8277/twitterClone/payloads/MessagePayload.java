package org.ac.cst8277.twitterClone.payloads;

import java.util.List;

import org.ac.cst8277.twitterClone.entities.User;

public class MessagePayload {

	private User producer;
	private List<MessageContent> content;
	
	public User getProducer() {
		return producer;
	}
	public void setProducer(User producer) {
		this.producer = producer;
	}
	public List<MessageContent> getMsg() {
		return content;
	}
	public void setMsg(List<MessageContent> msg) {
		this.content = msg;
	}
	
	
	
}
