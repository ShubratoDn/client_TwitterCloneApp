package org.ac.cst8277.twitterClone.services;

import java.util.List;

import org.ac.cst8277.twitterClone.entities.User;
import org.ac.cst8277.twitterClone.entities.UserRole;
import org.ac.cst8277.twitterClone.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRoleServices {
	
	@Autowired
	private UserRoleRepository userRoleRepo;
	
	
	public List<UserRole> getRoleByUser(User user){
		return userRoleRepo.getRoleByUser(user);
	}
	
	
//	public User addRole(User user, int role){
//		return userRoleRepo.addRole(user, role);
//	}
	
	
}
