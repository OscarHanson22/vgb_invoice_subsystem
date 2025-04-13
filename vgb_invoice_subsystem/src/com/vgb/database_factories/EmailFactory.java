package com.vgb.database_factories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmailFactory {
	public static List<String> loadEmails(Connection connection, int personId) {
		List<String> emails = new ArrayList<>();
		
		String query = "select email from Email where personId = ?";
		
		PreparedStatement statement  = null;
		ResultSet results = null;
		
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, personId);
			results = statement.executeQuery();
			while (results.next()) {
				String email = results.getString("email");
				emails.add(email);
			}
			results.close();
			statement.close();
		} catch (SQLException e) {
			System.err.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return emails;
	}
}