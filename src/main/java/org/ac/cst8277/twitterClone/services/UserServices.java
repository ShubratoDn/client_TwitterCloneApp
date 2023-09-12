package org.ac.cst8277.twitterClone.services;

import java.util.List;

import org.ac.cst8277.twitterClone.entities.User;
import org.ac.cst8277.twitterClone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UserServices {

	@Autowired
	private  UserRepository userRepo;


	// add user
	public boolean addUser(User user) {
		boolean flag = userRepo.addUser(user);
		return flag;
	}

	// get user by email
	public User getUserByEmail(String name) {
		User userByEmail = userRepo.getUserByEmail(name);
		return userByEmail;
	}

	// get user by id
	public User getUserById(int id) {
		User userByEmail = userRepo.getUserById(id);
		return userByEmail;
	}

	// get user by role
	public List<User> getUsersByRole(String role) {
		return userRepo.getUsersByRole(role);
	}

	// check by user Role
	public User checkByRole(User user, int role) {
		return userRepo.checkByRole(user, role);
	}

	//add role
	public User addRole(User user, int role) {
		return userRepo.addRole(user, role);
	}
}
