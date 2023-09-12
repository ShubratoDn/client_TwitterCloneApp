package org.ac.cst8277.twitterClone.repository;

import java.util.ArrayList;
import java.util.List;

import org.ac.cst8277.twitterClone.entities.Message;
import org.ac.cst8277.twitterClone.entities.User;
import org.ac.cst8277.twitterClone.entities.UserSubscribed;
import org.ac.cst8277.twitterClone.payloads.MessageContent;
import org.ac.cst8277.twitterClone.payloads.MessagePayload;
import org.ac.cst8277.twitterClone.services.UserServices;
import org.ac.cst8277.twitterClone.services.UserSubscribeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class MessageRepository {

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private UserSubscribeServices userSubscribeServices;
	
	@Autowired
	private UserServices userServices;
	
	@Autowired
	public MessageRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	
	//add message
	public boolean addMessage(Message msg) {		
		String sql = "INSERT INTO `messages`(`producer_id`, `message`, `timestamp`) VALUES (?,?,?)";
		int update = this.jdbcTemplate.update(sql, msg.getProducer().getId(), msg.getMessage(), msg.getTimestamp());
		
		return true;
	}
	
	
	
	//get producers all message
	public List<Message> getProducersAllMessage(User producer) {		
		String sql = "SELECT * FROM `messages` WHERE producer_id = ?";
		List<Message> query = jdbcTemplate.query(sql, new MessageRowMapper(), producer.getId());		
		return query;		
	}
	
	
	
	//get My All messages
	public List<MessagePayload> getMyAllMessage(User subscriber) {

		// it will return my all subscriptions
		List<UserSubscribed> mySubscriptions = userSubscribeServices.getMySubscriptions(subscriber);

		List<MessagePayload> messages = new ArrayList<MessagePayload>();

		for (UserSubscribed subscribed : mySubscriptions) {
			
			MessagePayload messagePayload = new MessagePayload();
			messagePayload.setProducer(subscribed.getProducer());

			List<MessageContent> msgContents = new ArrayList<MessageContent>();

			// i will get all the messages of each producer
			List<Message> producersAllMessage = this.getProducersAllMessage(subscribed.getProducer());

			// setting message
			for (Message msg : producersAllMessage) {
				MessageContent msgContent = new MessageContent();
				msgContent.setMessage(msg.getMessage());
				msgContent.setTime(msg.getTimestamp());

				msgContents.add(msgContent);
			}

			messagePayload.setMsg(msgContents);
			
			//adding the message payloads
			messages.add(messagePayload);
		}
		
		return messages;
		
	}
	
	
		
	//get specific producers all message()
	public MessagePayload getMessagesOfProducer(User producer){
		String sql = "SELECT * FROM `messages` WHERE producer_id = ?";
		List<Message> query = jdbcTemplate.query(sql, new MessageRowMapper(), producer.getId());
		
		MessagePayload message = new MessagePayload();
		User producerInfo = userServices.getUserById(producer.getId());
		message.setProducer(producerInfo);
		
		List<MessageContent> msgContents = new ArrayList<MessageContent>();
		for(Message queryMsg : query) {
			MessageContent msgContent = new MessageContent();
			msgContent.setMessage(queryMsg.getMessage());
			msgContent.setTime(queryMsg.getTimestamp());	
			
			msgContents.add(msgContent);					
		}
		
		message.setMsg(msgContents);
		
		return message;
		
	}
	
	
	//search messages
	public List<Message> searchByMessage(User subscriber, String search_query){
		String sql = "SELECT m.id, m.producer_id, m.message, m.timestamp FROM messages m INNER JOIN user_subscribed s ON m.producer_id = s.producer_id WHERE s.subscriber_id = ? AND m.message LIKE '%"+search_query+"%'";		
		List<Message> messagesResult = this.jdbcTemplate.query(sql, new MessageRowMapper(), subscriber.getId());
		
		List<Message> messages = new ArrayList<Message>();
		
		for(Message message: messagesResult) {
			User userById = userServices.getUserById(message.getProducer().getId());
			message.setProducer(userById);
			
			messages.add(message);
		}
		
		if(messages.size() == 0) {			
			return null;
		}
		return messages;
		
		
	}
	
	
}
