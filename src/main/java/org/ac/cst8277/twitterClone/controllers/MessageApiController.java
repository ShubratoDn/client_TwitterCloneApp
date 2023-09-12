package org.ac.cst8277.twitterClone.controllers;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.ac.cst8277.twitterClone.entities.Message;
import org.ac.cst8277.twitterClone.entities.User;
import org.ac.cst8277.twitterClone.entities.UserSubscribed;
import org.ac.cst8277.twitterClone.payloads.ApiResponse;
import org.ac.cst8277.twitterClone.payloads.MessagePayload;
import org.ac.cst8277.twitterClone.services.Constant;
import org.ac.cst8277.twitterClone.services.MessageServices;
import org.ac.cst8277.twitterClone.services.UserServices;
import org.ac.cst8277.twitterClone.services.UserSubscribeServices;

@RestController
@RequestMapping("/api")
public class MessageApiController {

	@Autowired
	private UserServices userServices;
	
	@Autowired
	private MessageServices messageServices;	
	
	@Autowired
	private UserSubscribeServices userSubscribeServices;
	
	//sending message
	@PostMapping("/send-message")
	public ResponseEntity<?> sendMessage(@RequestBody Message message){
		
		//checking for if the user if a producer or not
		User checkByRole = userServices.checkByRole(message.getProducer(), Constant.PRODUCER_ID);
		if(checkByRole == null) {
			return ResponseEntity.badRequest().body(new ApiResponse("error", "You are not a PRODUCER"));
		}
		


        // Create a java.util.Date object from the current time
        Date currentDate = new Date(System.currentTimeMillis());
        // Convert the java.util.Date object to a java.sql.Timestamp object
        Timestamp sqlTimestamp = new Timestamp(currentDate.getTime());
		
		message.setTimestamp(sqlTimestamp);
		
		messageServices.addMessage(message);		
		
		return ResponseEntity.ok(message);
	}
	
	
	
	//get subscriber all messages
	@GetMapping("/my-inbox/{subscriber_id}")
	public ResponseEntity<?> myInbox(@PathVariable int subscriber_id){
		
		User subscriber = new User();
		subscriber.setId(subscriber_id);
		
		List<MessagePayload> myAllMessage = messageServices.getMyAllMessage(subscriber);		
		return ResponseEntity.ok(myAllMessage);
	}
	
	
	//=========SEARCHING============
	
	//get subscriber's messages by producer
	@GetMapping("/messages/subscriber/{subscriber_id}/producer/{producer_id}")
	public ResponseEntity<?> searchByProducer(@PathVariable int subscriber_id, @PathVariable int producer_id){		
		
		User subscriber = new User();
		subscriber.setId(subscriber_id);
		
		User producer = new User();
		producer.setId(producer_id);	
	
		
		if(subscriber_id == producer_id) {
			return ResponseEntity.badRequest().body(new ApiResponse("error", "Invalid Request"));
		}
		
		
		UserSubscribed checkUserSubscribed = userSubscribeServices.checkUserSubscribed(subscriber, producer);
		if(checkUserSubscribed == null) {
			return ResponseEntity.badRequest().body(new ApiResponse("error", "You are not subscribed to this PRODUCER"));
		}
		
		MessagePayload messagesOfProducer = messageServices.getMessagesOfProducer(producer);
		
		return ResponseEntity.ok(messagesOfProducer);
		
	}
	
	
	
	
	//search message
	@GetMapping("/subscriber/{subscriber_id}/search")
    public ResponseEntity<?> searchMessages(
            @PathVariable("subscriber_id") int subscriberId,
            @RequestParam(value = "search_query", defaultValue = "", required = false) String searchQuery) {

		
		
		User user = userServices.getUserById(subscriberId);
		
		if(user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", "Invalid Subscriber"));
		}
		
		
		List<Message> searchByMessage = messageServices.searchByMessage(user, searchQuery);		
		if(searchByMessage == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", "Nothing found with this query"));
		}


        return ResponseEntity.ok(searchByMessage);
    }
	
	
}
