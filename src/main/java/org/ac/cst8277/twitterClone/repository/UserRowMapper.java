package org.ac.cst8277.twitterClone.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.ac.cst8277.twitterClone.entities.User;
import org.springframework.jdbc.core.RowMapper;

public class UserRowMapper implements RowMapper<User> {

	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setId(rs.getInt("id"));
		user.setName(rs.getString("name"));
		user.setEmail(rs.getString("email"));		
		user.setToken(rs.getString("token"));
		return user;
	}

	
}
