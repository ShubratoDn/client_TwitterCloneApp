package org.ac.cst8277.twitterClone.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.ac.cst8277.twitterClone.entities.Message;
import org.ac.cst8277.twitterClone.entities.User;
import org.springframework.jdbc.core.RowMapper;

public class MessageRowMapper implements RowMapper<Message> {

	public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Message msg = new Message();
		
		User producer = new User();
		producer.setId(rs.getInt("producer_id"));
		
		msg.setId(rs.getInt("id"));
		msg.setMessage(rs.getString("message"));
		msg.setProducer(producer);
		msg.setTimestamp(rs.getTimestamp("timestamp"));
		
		return msg;
	}	
	
}
