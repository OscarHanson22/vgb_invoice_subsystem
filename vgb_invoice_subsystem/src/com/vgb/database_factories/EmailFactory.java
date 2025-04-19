package com.vgb.database_factories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A class that loads email strings from the Email table in the database.
 */
public class EmailFactory {
    private static final Logger logger = LogManager.getLogger(EmailFactory.class);

    /**
     * Loads all of the emails for the person with the specified personId from the Email table in the database. 
     * 
     * @param connection The connection to the database. 
     * @param personId The personId column value of the Person in the database table.
     */
	public static List<String> loadEmails(Connection connection, int personId) {
		List<String> emails = new ArrayList<>();
		
		String query = "select email from Email where personId = ?";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, personId);
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				String email = results.getString("email");
				emails.add(email);
			}
		} catch (SQLException e) {
			logger.error("SQLException encountered while loading emails for person with personId: " + personId + ".");
			throw new RuntimeException(e);
		}
		
		if (emails.size() == 0) {
			logger.warn("No emails registered for person with personId: " + personId + ".");
		}
		
		return emails;
	}
}