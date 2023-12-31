package org.ac.cst8277.twitterClone.controllers;

import org.ac.cst8277.twitterClone.entities.User;
import org.ac.cst8277.twitterClone.payloads.ApiResponse;
import org.ac.cst8277.twitterClone.payloads.ResponsePayload;
import org.ac.cst8277.twitterClone.services.UserServices;

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
public class AuthRestController {

	@Autowired
	private UserServices userServices;


	// register user
	@PostMapping(value = "/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
		if (user.getName().isBlank() || user.getEmail().isBlank() || user.getPassword().isBlank()) {
			return ResponseEntity.badRequest().body(new ApiResponse(HttpStatus.BAD_REQUEST.toString(), "Insert all field"));
		}

		User userByEmail = userServices.getUserByEmail(user.getEmail());
		if (userByEmail != null) {
			return ResponseEntity.badRequest().body(new ApiResponse(HttpStatus.BAD_REQUEST.toString(), "Email already exits"));
		}

		if (user.getPassword().length() < 4) {
			System.out.println("Password needs a minimum of 4 characters");
			return ResponseEntity.badRequest()
					.body(new ApiResponse("error", "Password needs a minimum of 4 characters"));
		}

		userServices.addUser(user);

		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(HttpStatus.OK.toString(), "Register Successfull"));
	}
	
	
	//--
	//get user by token or id
	@GetMapping("/user/{tokenOrId}")
	public ResponseEntity<?> getUserByTokenOrID(@PathVariable String tokenOrId){
		User userByTokenOrId = userServices.getUserByTokenOrId(tokenOrId);
		return ResponseEntity.ok(new ResponsePayload(HttpStatus.OK.toString(), userByTokenOrId, "User by given id"));
	}

}
