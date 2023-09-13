package org.ac.cst8277.twitterClone.controllers;

import java.util.List;

import org.ac.cst8277.twitterClone.entities.User;
import org.ac.cst8277.twitterClone.entities.UserSubscribed;
import org.ac.cst8277.twitterClone.payloads.ApiResponse;
import org.ac.cst8277.twitterClone.payloads.SubscribePayload;
import org.ac.cst8277.twitterClone.services.Constant;
import org.ac.cst8277.twitterClone.services.UserServices;
import org.ac.cst8277.twitterClone.services.UserSubscribeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SubscriberApiController {
	
	@Autowired
	private UserServices userServices;
	
	@Autowired
	private UserSubscribeServices userSubscribeServices;


	// get all producer list
	@GetMapping("/get-producers")
	public ResponseEntity<?> getAllProducer() {
		List<User> usersByRole = userServices.getUsersByRole("producer");
		return ResponseEntity.ok(usersByRole);
	}

	
	// get all producer list
	@GetMapping("/get-subscribers")
	public ResponseEntity<?> getAllSubscribers() {
		List<User> usersByRole = userServices.getUsersByRole("subscriber");
		return ResponseEntity.ok(usersByRole);
	}
	
	
	
	
	//get all of my PRODUCERS
	@GetMapping("/my-subscriptions/{subscriber_id}")
	public ResponseEntity<?> getMyProducers(@PathVariable int subscriber_id){
		User user = new User();
		user.setId(subscriber_id);
		
		List<UserSubscribed> mySubscriptions = userSubscribeServices.getMySubscriptions(user);
		
		return ResponseEntity.ok(mySubscriptions);
	}
	
	

	//get my subscriber
	@GetMapping("/my-subscribers/producer/{producer_id}")
	public ResponseEntity<?> getMySubscribers(@PathVariable int producer_id){		
		
		User user = userServices.getUserById(producer_id);		
		User checkByRole = userServices.checkByRole(user, Constant.PRODUCER_ID);
		
		
		if(checkByRole == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("error", "You are not a PRODUCER"));
		}
		
		List<User> mySubscriber = userSubscribeServices.getMySubscriber(checkByRole);
		
		return ResponseEntity.ok(mySubscriber);
	}
	
	
	
	
	//be a Producer
	@PostMapping("/be-producer")
	public ResponseEntity<?> beProducer(@RequestBody User user){
		
		User userById = userServices.getUserById(user.getId());
		if(userById == null) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ApiResponse("error", "Invalid User Id"));
		}
		
		
		User checkByRole = userServices.checkByRole(user, Constant.PRODUCER_ID);		
		if(checkByRole != null) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ApiResponse("error", "You are already a Producer"));
		}
		
		User addRole = userServices.addRole(user, Constant.PRODUCER_ID);
		
		return ResponseEntity.ok(new ApiResponse("success", "Congratulations, "+addRole.getName()+"!! You are now a PRODUCER"));
	}
	
	
	//subscribe to producer
	@PostMapping("/subscribe")
	public ResponseEntity<?> subscribeToProducer(@RequestBody SubscribePayload subscribePayload){
		
		//		
		if(subscribePayload.getProducer() == subscribePayload.getSubscriber()) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ApiResponse("error","You can not subscribe to yourself"));
		}
		
		//checking if the SUBSCRIBER is valid
		User subscriber = new User();
		subscriber.setId(subscribePayload.getSubscriber());
		User userById = userServices.getUserById(subscriber.getId());
		if(userById == null) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ApiResponse("error","Invalid Subscriber"));
		}
		
		
		//CHECKING IF THE PRODUCER IS valid
		User producer = new User();
		producer.setId(subscribePayload.getProducer());		
		User checkByRole = userServices.checkByRole(producer, Constant.PRODUCER_ID);
		if(checkByRole == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error","This user is not a Producer"));
		}
		
		
		
		//checking if the user is subscribed to the producer
		UserSubscribed checkUserSubscribed = userSubscribeServices.checkUserSubscribed(subscriber, producer);
		if(checkUserSubscribed != null) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ApiResponse("error","Already Subscribed!"));
		}
		
		
		//Subscribing to producer
		UserSubscribed addSubscription = userSubscribeServices.addSubscription(subscriber, producer);
		
		
		
		return ResponseEntity.ok(new ApiResponse("success", "Successfully subscribed to Producer " + addSubscription.getProducer().getName()));
	}

}
