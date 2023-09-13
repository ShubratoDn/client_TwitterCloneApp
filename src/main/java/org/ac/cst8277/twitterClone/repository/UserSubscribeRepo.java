package org.ac.cst8277.twitterClone.repository;

import java.util.ArrayList;
import java.util.List;

import org.ac.cst8277.twitterClone.entities.User;
import org.ac.cst8277.twitterClone.entities.UserSubscribed;
import org.ac.cst8277.twitterClone.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserSubscribeRepo {
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private UserServices userServices;
	
	public UserSubscribeRepo(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	
	//check subscription
	public UserSubscribed checkUserSubscribed(User subscriber, User producer) {		
		String sql = "SELECT * FROM user_subscribed WHERE subscriber_id = ? AND producer_id = ?";		
		List<UserSubscribed> query = jdbcTemplate.query(sql, new UserSubscribeRowMapper(), subscriber.getId(), producer.getId());
		
		if(query.size() == 0) {
			return null;
		}
		
		return query.get(0);
	}
	
	
	//unsubscribe
	public void unsubscribe(User subscriber, User producer) {		
		String sql = "DELETE FROM `user_subscribed` WHERE subscriber_id = ? AND producer_id = ?";	
		jdbcTemplate.update(sql,subscriber.getId(), producer.getId());
	}
	
	
	//get my all subscription
	public List<UserSubscribed> getMySubscriptions(User subscriber) {		
		String sql = "SELECT * FROM user_subscribed WHERE subscriber_id = ?";		
		List<UserSubscribed> query = jdbcTemplate.query(sql, new UserSubscribeRowMapper(), subscriber.getId());
		if(query.size() == 0) {
			return null;
		}
		
		List<UserSubscribed> subscriptions = new ArrayList<UserSubscribed>();
		
		for(UserSubscribed subscribed : query) {
			User subscriber1 = userServices.getUserById(subscribed.getSubscriber().getId());
			User producer1 = userServices.getUserById(subscribed.getProducer().getId());
			
			subscribed.setProducer(producer1);
			subscribed.setSubscriber(subscriber1);
			
			subscriptions.add(subscribed);
			
		}
		
		return subscriptions;
	}
	
	
	
	
	// get my subscribers
	public List<User> getMySubscriber(User producer) {
		String sql = "SELECT u.id, u.name, u.email, u.token FROM users u JOIN user_subscribed us ON u.id = us.subscriber_id WHERE us.producer_id = ?";
		List<User> query = jdbcTemplate.query(sql, new UserRowMapper(), producer.getId());

		List<User> users = new ArrayList<User>();

		for (User user : query) {
			User userById = userServices.getUserById(user.getId());
			users.add(userById);
		}

		return users;
	}
	
	
	
	//add subscription
	public UserSubscribed addSubscription(User subscriber, User producer) {
		
		User subscriber2 = userServices.getUserById(subscriber.getId());
		User producer2 = userServices.getUserById(producer.getId());
		
		String sql = "INSERT INTO `user_subscribed`(`subscriber_id`, `producer_id`) VALUES (?,?)";		
		jdbcTemplate.update(sql, subscriber.getId(), producer.getId());
		
		UserSubscribed userSubscribed = new UserSubscribed();
		userSubscribed.setProducer(producer2);
		userSubscribed.setSubscriber(subscriber2);
		return userSubscribed;
		
	}
	
	
}
