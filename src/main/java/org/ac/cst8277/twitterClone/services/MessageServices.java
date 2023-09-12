package org.ac.cst8277.twitterClone.services;

import java.util.List;

import org.ac.cst8277.twitterClone.entities.Message;
import org.ac.cst8277.twitterClone.entities.User;
import org.ac.cst8277.twitterClone.payloads.MessagePayload;
import org.ac.cst8277.twitterClone.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageServices {
	
	@Autowired
	private MessageRepository messageRepo;	
	
		
	//add message
	public boolean addMessage(Message msg) {
		return messageRepo.addMessage(msg);
	}

	
	//get my all messages
	public List<MessagePayload> getMyAllMessage(User subscriber) {
		return messageRepo.getMyAllMessage(subscriber);
	}
	
	//get all messages of a Producer
	public MessagePayload getMessagesOfProducer(User producer){
		return messageRepo.getMessagesOfProducer(producer);
	}
	
	
	//search messages
	public List<Message> searchByMessage(User subscriber, String search_query){
		return messageRepo.searchByMessage(subscriber, search_query);
	}
	
}
