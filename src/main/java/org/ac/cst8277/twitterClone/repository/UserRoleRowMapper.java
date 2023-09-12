package org.ac.cst8277.twitterClone.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.ac.cst8277.twitterClone.entities.UserRole;
import org.springframework.jdbc.core.RowMapper;

public class UserRoleRowMapper implements RowMapper<UserRole> {

	public UserRole mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserRole userRole = new UserRole();
		userRole.setId(rs.getInt("id"));
		userRole.setRole(rs.getString("role"));
				
		return userRole;
	}

}
