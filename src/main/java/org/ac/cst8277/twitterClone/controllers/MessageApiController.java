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
import org.ac.cst8277.twitterClone.payloads.ResponsePayload;
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
	
	//--
	//sending message
	@PostMapping("/send-message")
	public ResponseEntity<?> sendMessage(@RequestBody Message message){
		
		User userByTokenOrId = userServices.getUserByTokenOrId(message.getProducer().getToken(), message.getProducer().getId());
		if(userByTokenOrId == null) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ApiResponse(HttpStatus.BAD_GATEWAY.toString(), "Invalid User Token or id"));
		}		
		
		//checking for if the user if a producer or not
		User checkByRole = userServices.checkByRole(userByTokenOrId, Constant.PRODUCER_ID);
		if(checkByRole == null) {
			return ResponseEntity.badRequest().body(new ApiResponse(HttpStatus.BAD_REQUEST.toString(), "You are not a PRODUCER"));
		}		

		if(message.getMessage().isBlank()) {
			return ResponseEntity.badRequest().body(new ApiResponse(HttpStatus.BAD_REQUEST.toString(), "Message Can not be empty"));
		}

        // Create a java.util.Date object from the current time
        Date currentDate = new Date(System.currentTimeMillis());
        // Convert the java.util.Date object to a java.sql.Timestamp object
        Timestamp sqlTimestamp = new Timestamp(currentDate.getTime());
		
		message.setTimestamp(sqlTimestamp);
		
		message.setProducer(checkByRole);
		
		messageServices.addMessage(message);		
		
		return ResponseEntity.ok(new ResponsePayload(HttpStatus.OK.toString(), message, "Message Send Successfully to your subscribers"));
	}
	
	
	
	
	//--
	//get subscriber all messages
	@GetMapping("/messages/subscriber/{subscriberIdOrToken}")
	public ResponseEntity<?> myInbox(@PathVariable("subscriberIdOrToken") String subscriberIdentity){
		
		User subscriber = userServices.getUserByTokenOrId(subscriberIdentity);
		if(subscriber == null) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ApiResponse(HttpStatus.BAD_GATEWAY.toString(), "Invalid User Token or id"));
		}	
		
		List<MessagePayload> myAllMessage = messageServices.getMyAllMessage(subscriber);
		return ResponseEntity.ok(new ResponsePayload(HttpStatus.OK.toString(), myAllMessage, "A list of my inbox"));
	}
	
	
	//--
	//get Producers all Message
	@GetMapping("/messages/producer/{producerIdOrToken}")
	public ResponseEntity<?> producerMessages(@PathVariable("producerIdOrToken") String producerIdentity){
		
		User producer = userServices.getUserByTokenOrId(producerIdentity);
		if(producer == null) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ApiResponse(HttpStatus.BAD_GATEWAY.toString(), "Invalid Producer Token or id"));
		}	
		
		User checkByRole = userServices.checkByRole(producer, Constant.PRODUCER_ID);
		if(checkByRole == null) {
			return ResponseEntity.badRequest().body(new ApiResponse(HttpStatus.BAD_REQUEST.toString(), "You are not a PRODUCER"));
		}	
		
		MessagePayload messagesOfProducer = messageServices.getMessagesOfProducer(producer);
		return ResponseEntity.ok(new ResponsePayload(HttpStatus.OK.toString(), messagesOfProducer, "A list of all Message of producer " + producer.getName()));
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
			return ResponseEntity.badRequest().body(new ApiResponse(HttpStatus.BAD_REQUEST.toString(), "Invalid Request"));
		}
		
		
		UserSubscribed checkUserSubscribed = userSubscribeServices.checkUserSubscribed(subscriber, producer);
		if(checkUserSubscribed == null) {
			return ResponseEntity.badRequest().body(new ApiResponse(HttpStatus.BAD_REQUEST.toString(), "You are not subscribed to this PRODUCER"));
		}
		
		MessagePayload messagesOfProducer = messageServices.getMessagesOfProducer(producer);
		
		return ResponseEntity.ok(new ResponsePayload(HttpStatus.OK.toString(), messagesOfProducer, "A list of all messages of the Producer"));
	}
	
	
	
	//--
	//search message
	@GetMapping("/subscriber/{subscriberIdOrToken}/search")
    public ResponseEntity<?> searchMessages(
            @PathVariable("subscriberIdOrToken") String subscriberIdOrToken,
            @RequestParam(value = "search_query", defaultValue = "", required = false) String searchQuery) {

		
		
		User user = userServices.getUserByTokenOrId(subscriberIdOrToken);
		
		if(user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(HttpStatus.NOT_FOUND.toString(), "Invalid Subscriber"));
		}
		
		
		List<Message> searchByMessage = messageServices.searchByMessage(user, searchQuery);		
		if(searchByMessage == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(HttpStatus.NOT_FOUND.toString(), "Nothing found with this query"));
		}


        return ResponseEntity.ok(new ResponsePayload(HttpStatus.OK.toString(), searchByMessage, "It will Search by queries from messages to whom the subscriber is Subscribed"));
    }
	
	
}
