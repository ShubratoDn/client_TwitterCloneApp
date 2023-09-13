package org.ac.cst8277.twitterClone.controllers;

import java.util.List;

import org.ac.cst8277.twitterClone.entities.User;
import org.ac.cst8277.twitterClone.entities.UserSubscribed;
import org.ac.cst8277.twitterClone.payloads.ApiResponse;
import org.ac.cst8277.twitterClone.payloads.ResponsePayload;
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
	
	
	
	//--
	//get my subscribed producer
	@GetMapping("/my-subscriptions/{subscriberIdOrToken}")
	public ResponseEntity<?> getMyProducers(@PathVariable("subscriberIdOrToken") String data){		
		User user = userServices.getUserByTokenOrId(data);
		if(user == null) {
			return ResponseEntity.badRequest().body(new ApiResponse(HttpStatus.BAD_REQUEST.toString(), "User id or token not valid"));
		}

		List<UserSubscribed> mySubscriptions = userSubscribeServices.getMySubscriptions(user);
		
		return ResponseEntity.ok(new ResponsePayload(HttpStatus.OK.toString(), mySubscriptions, "List of my all subscribed producers"));
	}
	
	

	//--
	//get my subscriber
	@GetMapping("/my-subscribers/producer/{producerIdOrToken}")
	public ResponseEntity<?> getMySubscribers(@PathVariable("producerIdOrToken") String data){		
		
		User user = userServices.getUserByTokenOrId(data);
		if(user == null) {
			return ResponseEntity.badRequest().body(new ApiResponse(HttpStatus.BAD_REQUEST.toString(), "User id or token not valid"));
		}
		
		User checkByRole = userServices.checkByRole(user, Constant.PRODUCER_ID);		
		
		if(checkByRole == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("error", "You are not a PRODUCER"));
		}
		
		List<User> mySubscriber = userSubscribeServices.getMySubscriber(user);
		
		return ResponseEntity.ok(new ResponsePayload(HttpStatus.OK.toString(), mySubscriber, "List of my Subscribers"));
	}
	
	
	
	//--
	//be a Producer
	@PostMapping("/be-producer")
	public ResponseEntity<?> beProducer(@RequestBody User user){
		
		User userByToken = userServices.getUserByToken(user.getToken());
		
		if(userByToken == null) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ApiResponse("error", "Invalid User Token"));
		}		
		
		User checkByRole = userServices.checkByRole(userByToken, Constant.PRODUCER_ID);		
		if(checkByRole != null) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ApiResponse("error", "You are already a Producer"));
		}
		
		User addRole = userServices.addRole(userByToken, Constant.PRODUCER_ID);
		
		return ResponseEntity.ok(new ApiResponse("success", "Congratulations, "+addRole.getName()+"!! You are now a PRODUCER"));
	}


	
	
	//--
	//subscribe to producer
	@PostMapping("/subscribe")
	public ResponseEntity<?> subscribeToProducer(@RequestBody SubscribePayload subscribePayload){
		
		User subscriber = userServices.getUserByTokenOrId(subscribePayload.getSubscriber());
		if(subscriber == null) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ApiResponse("error","Invalid Subscriber"));
		}		
		User producer = userServices.getUserByTokenOrId(subscribePayload.getProducer());
		if(producer == null) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ApiResponse("error","Invalid Producer"));
		}	
		
		
		if(subscriber.getId() == producer.getId()) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ApiResponse("error","You can not subscribe to yourself"));
		}
		
		
				
		//CHECKING IF THE PRODUCER IS valid				
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
