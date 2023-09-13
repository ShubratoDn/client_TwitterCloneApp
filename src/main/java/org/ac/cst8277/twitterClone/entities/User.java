package org.ac.cst8277.twitterClone.entities;

import java.util.List;

public class User {
	private int id;
	private String name;
	private String email;
	private String password;
	private String token;
	
	private List<UserRole> userRoles;
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public List<UserRole> getUserRoles() {
		return userRoles;
	}




	public void setUserRoles(List<UserRole> userRoles) {
		this.userRoles = userRoles;
	}




	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}	
	
	
	
	
}
