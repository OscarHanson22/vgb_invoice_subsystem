/**
 * Authors: Oscar Hanson and Ermias Wolde
 * Date: 5/9/2025
 * Purpose: Class that reads Person objects from the database.
 */

package com.vgb.database_factories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vgb.Person;

/**
 * A class that loads Person objects and related information from the Person table in the database.
 */
public class PersonFactory {
    private static final Logger logger = LogManager.getLogger(PersonFactory.class);
	
    /**
     * Loads a single Person object with the specified personId from the Person table in the database. 
     * 
     * @param connection The connection to the database. 
     * @param personId The personId column value of the Person in the database table.
     */
	public static Person loadPerson(Connection connection, int personId) {
		if (connection == null) {
			logger.error("Cannot load a person with a null database connection.");
			throw new IllegalArgumentException("Database connection cannot be null.");
		}
		
		Person person = null;
		String uuidString = null;
		
		String query = "select uuid, firstName, lastName, phoneNumber from Person where personId = ?;";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, personId);
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				uuidString = results.getString("uuid");
				UUID uuid = UUID.fromString(uuidString);
				String firstName = results.getString("firstName");
				String lastName = results.getString("lastName");
				String phoneNumber = results.getString("phoneNumber");
				List<String> emailAddresses = EmailFactory.loadEmails(connection, personId);
				person = new Person(uuid, firstName, lastName, phoneNumber, emailAddresses);
			} else {
				logger.error("Person with personId: " + personId + " not found in the database.");
				throw new IllegalStateException("Person with personId: " + personId + " not found in the database.");
			}
		// Only occurs when the `UUID.fromString(...)` above fails
		} catch (IllegalArgumentException e) {
			logger.error("UUID for person with personId: " + personId + " is formatted incorrectly: \"" + uuidString + "\".");
			throw new RuntimeException(e);
		} catch (SQLException e) {
			logger.error("SQLException encountered while loading Person with id: {}", personId);
			throw new RuntimeException(e);
		} 
			
		return person;
	}
	
	/**
     * Gets the personId of a person with the specified UUID.
     * 
     * @param connection The connection to the database. 
     * @param uuid The uuid column value of the Person table in the database.
     */
	public static Optional<Integer> getId(Connection connection, UUID uuid) {
		Optional<Integer> personId = Optional.empty();

		if (connection == null) {
			logger.warn("Cannot find a person with a null database connection.");
			return personId;
		}
		
		if (uuid == null) {
			logger.warn("Cannot find a person with a null UUID.");
			return personId;
		}
		
		String query = "select personId from Person where uuid = ?;";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, uuid.toString());
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				personId = Optional.of(results.getInt("personId"));
			} else {
				logger.info("Person with UUID: \"" + uuid + "\" not found in the database.");
			}
		} catch (SQLException e) {
			logger.error("SQLException encountered while getting personId for person with UUID: \"" + uuid + "\".");
			throw new RuntimeException(e);
		} 
			
		return personId;
	}
	
	/**
     * Loads all Person objects from the Person table in the database. 
     * 
     * @param connection The connection to the database. 
     */
	public static List<Person> loadAllPeople(Connection connection) {
		if (connection == null) {
			logger.error("Database connection cannot be null.");
			throw new IllegalArgumentException("Database connection cannot be null.");
		}
		
		List<Person> people = new ArrayList<>();
		String query = "select personId from Person";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				int personId = results.getInt("personId");
				Person person = loadPerson(connection, personId);
				people.add(person);
			}
		} catch (SQLException e) {
			logger.error("SQLException encountered while loading all people from the database.");
			throw new RuntimeException(e);
		}
			
		return people;
	}
}