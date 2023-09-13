package org.ac.cst8277.twitterClone.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.ac.cst8277.twitterClone.entities.User;
import org.ac.cst8277.twitterClone.entities.UserRole;
import org.ac.cst8277.twitterClone.services.Constant;
import org.ac.cst8277.twitterClone.services.UserRoleServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	

	@Autowired
	private UserRoleServices userRoleServices;
	
	
	
	//add user
	public boolean addUser(User user) {
		
		user.setToken(UUID.randomUUID().toString());
		
		String sql = "INSERT INTO `users`(`name`, `email`, `password`, `token`) VALUES (?,?,?,?)";		
		jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getPassword(), user.getToken());
		
		// Get the added user ID
		String sqlGetId = "SELECT id FROM users WHERE name = ? and email = ?";
		int userId = jdbcTemplate.queryForObject(sqlGetId, Integer.class, user.getName(), user.getEmail());		
		
		//updating user role
		String sqlForRole = "INSERT INTO `user_and_role`(`user_id`, `role_id`) VALUES (?,?)";
		jdbcTemplate.update(sqlForRole, userId, Constant.SUBSCRIBER_ID);
		
		return true;
	}	
	
	
	
	
	//get user By Email
	public User getUserByEmail(String email) {
		String sql = "select * from users where email=?";		
		List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), email);	
		
		if(users.size() == 0) {
			return null;
		}
		
		User user = users.get(0);
		
		List<UserRole> roleByUser = userRoleServices.getRoleByUser(user);
		
		user.setUserRoles(roleByUser);
		
		return user;
	}
	
	
	//get User by Token
	public User getUserByToken(String token) {
		String sql = "select * from users where token=?";		
		List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), token);	
		
		if(users.size() == 0) {
			return null;
		}
		
		User user = users.get(0);
		
		List<UserRole> roleByUser = userRoleServices.getRoleByUser(user);
		
		user.setUserRoles(roleByUser);
		
		return user;		
	}
	
	
	// get user By user id
	public User getUserById(int id) {
		String sql = "select * from users where id=?";
		List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), id);

		if (users.size() == 0) {
			return null;
		}

		User user = users.get(0);

		List<UserRole> roleByUser = userRoleServices.getRoleByUser(user);

		user.setUserRoles(roleByUser);

		return user;
	}
		
	
	//get user by id or token
	public User getUserByTokenOrId(String data) {
		String sql = "select * from users where id=? OR token=?";
		List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), data, data);

		if (users.size() == 0) {
			return null;
		}

		User user = users.get(0);

		List<UserRole> roleByUser = userRoleServices.getRoleByUser(user);

		user.setUserRoles(roleByUser);

		return user;
	}
	
	public User getUserByTokenOrId(String token, int id) {
		String sql = "select * from users where id=? OR token=?";
		List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), id, token);

		if (users.size() == 0) {
			return null;
		}

		User user = users.get(0);

		List<UserRole> roleByUser = userRoleServices.getRoleByUser(user);

		user.setUserRoles(roleByUser);

		return user;
	}
	
	
	//get Users by Role
	public List<User> getUsersByRole(String role){
		String sql = "SELECT u.id, u.name, u.email, u.token\r\n"
				+ "FROM users u\r\n"
				+ "JOIN user_and_role uar ON u.id = uar.user_id\r\n"
				+ "JOIN user_role ur ON uar.role_id = ur.id\r\n"
				+ "WHERE ur.role = ? ORDER BY u.id DESC";
		
		List<User> userWithOutRole = jdbcTemplate.query(sql, new UserRowMapper(), role);	
		
		if(userWithOutRole.size() == 0) {
			return null;
		}		
		
		List<User> users = new ArrayList<User>();
		
		for(User user: userWithOutRole) {
			List<UserRole> roleByUser = userRoleServices.getRoleByUser(user);
			user.setUserRoles(roleByUser);
			
			users.add(user);
		}		
		return users;		
	}
	
	
	
	
	
	//check by role
	public User checkByRole(User user, int role) {
		String sql = "SELECT u.id, u.name, u.email, u.token FROM users u JOIN user_and_role uar ON u.id = uar.user_id JOIN user_role ur ON uar.role_id = ur.id WHERE u.id = ? AND ur.id = ? ";

		List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), user.getId(), role);

		if (users.size() == 0) {
			return null;
		}
		User userx = users.get(0);

		List<UserRole> roleByUser = userRoleServices.getRoleByUser(userx);
		userx.setUserRoles(roleByUser);
		
		return userx;
	}
	
	
	
	
	public User addRole(User user, int role) {
		String sql = "INSERT INTO `user_and_role`(`user_id`, `role_id`) VALUES (?, ?)";
		jdbcTemplate.update(sql, user.getId(), role);
		User userById = this.getUserById(user.getId());

		return userById;
	}
	
	
}
