package org.ac.cst8277.twitterClone.services;

import java.util.List;

import org.ac.cst8277.twitterClone.entities.User;
import org.ac.cst8277.twitterClone.entities.UserSubscribed;
import org.ac.cst8277.twitterClone.repository.UserSubscribeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class UserSubscribeServices {


	@Autowired
	private UserSubscribeRepo userSubscriberRepo;
	
	
	//check user subscribed
	public UserSubscribed checkUserSubscribed(User subscriber, User producer) {
		UserSubscribed checkUserSubscribed = userSubscriberRepo.checkUserSubscribed(subscriber, producer);
		return checkUserSubscribed;
	}
	
	public void unsubscribe(User subscriber, User producer) {
		userSubscriberRepo.unsubscribe(subscriber, producer);
	}
	
	//get my subscriptions
	public List<UserSubscribed> getMySubscriptions(User subscriber) {
		return userSubscriberRepo.getMySubscriptions(subscriber);
	}
	
	//get my subscribers
	public List<User> getMySubscriber(User producer){
		return userSubscriberRepo.getMySubscriber(producer);
	}
	
	public UserSubscribed addSubscription(User subscriber, User producer) {
		return userSubscriberRepo.addSubscription(subscriber, producer);
	}
	
	
	
	
}
