package org.ac.cst8277.twitterClone.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.ac.cst8277.twitterClone.entities.User;
import org.ac.cst8277.twitterClone.entities.UserSubscribed;
import org.ac.cst8277.twitterClone.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

public class UserSubscribeRowMapper implements RowMapper<UserSubscribed> {

	@Autowired
	private UserServices userServices;
    
	public UserSubscribed mapRow(ResultSet rs, int rowNum) throws SQLException {
		
//		User subscriber = userServices.getUserById(rs.getInt("subscriber_id"));
//		User producer = userServices.getUserById(rs.getInt("producer_id"));
//		
		User subscriber = new User();
		subscriber.setId(rs.getInt("subscriber_id"));
		
		User producer = new User();
		producer.setId(rs.getInt("producer_id"));
		
		
		UserSubscribed userSubscribed = new UserSubscribed();
		userSubscribed.setId(rs.getInt("id"));
		userSubscribed.setSubscriber(subscriber);
		userSubscribed.setProducer(producer);
		
		
		return userSubscribed;
	}
	
}
