package org.ac.cst8277.twitterClone.entities;

public class UserSubscribed {

	private int id;
	private User subscriber;
	private User producer;
	
	public UserSubscribed() {
		// TODO Auto-generated constructor stub
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getSubscriber() {
		return subscriber;
	}
	public void setSubscriber(User subscriber) {
		this.subscriber = subscriber;
	}
	public User getProducer() {
		return producer;
	}
	public void setProducer(User producer) {
		this.producer = producer;
	}
	
	
	
}
