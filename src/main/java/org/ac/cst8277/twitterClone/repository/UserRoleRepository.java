package org.ac.cst8277.twitterClone.repository;

import java.util.List;

import org.ac.cst8277.twitterClone.entities.User;
import org.ac.cst8277.twitterClone.entities.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserRoleRepository {

	private JdbcTemplate jdbcTemplate;
	
//	@Autowired
//	private UserServices userServices;
	
	@Autowired
	public UserRoleRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	

	
	
	//get role for user
	public List<UserRole> getRoleByUser(User user) {
		
		String sqlRole = "SELECT ur.id, ur.role FROM users u JOIN user_and_role uar ON u.id = uar.user_id JOIN user_role ur ON uar.role_id = ur.id WHERE u.id = ?";
		
		List<UserRole> userRoles = this.jdbcTemplate.query(sqlRole, new UserRoleRowMapper(), user.getId());
		
		return userRoles;
	}
	
	

//	//add role
//	public User addRole(User user, int role){
//		String sql = "INSERT INTO `user_and_role`(`user_id`, `role_id`) VALUES (?, ?)";
//		jdbcTemplate.update(sql, user.getId(), role);
//		
//		User userById = userServices.getUserById(user.getId());
//		
//		return userById;
//	}
	
	
	
}
