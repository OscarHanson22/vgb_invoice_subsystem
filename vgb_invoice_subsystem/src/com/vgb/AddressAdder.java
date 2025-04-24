package com.vgb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddressAdder {
    private static final Logger logger = LogManager.getLogger(AddressAdder.class);
	
	public static int addAddress(Connection connection, String street, String city, String state, String zip) {
		int addressId = 0;
		
		int stateId = 0;
		Optional<Integer> foundStateId = StateFinder.findState(connection, state);
		if (foundStateId.isEmpty()) {
			stateId = StateAdder.addState(connection, state);
		} else {
			stateId = foundStateId.get();
		}
		
		int zipId = 0;
		Optional<Integer> foundZipId = ZipcodeFinder.findZipcode(connection, zip);
		if (foundZipId.isEmpty()) {
			zipId = ZipcodeAdder.addZipcode(connection, zip, stateId);
		} else {
			zipId = foundZipId.get();
		}
				
		String query = "insert into Address(street, city, zipcodeId) values (?, ?, ?);";
		
		try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, street);
			statement.setString(2, city);
			statement.setInt(3, zipId);
			statement.executeUpdate();
			ResultSet key = statement.getGeneratedKeys();
			if (key.next()) {
				addressId = key.getInt(1);
			} else {
				logger.error("Address: \"" + street + ", " + city + ", " + state + ", " + zip + "\" could not be inserted into the database.");
				throw new RuntimeException("Address: \"" + street + ", " + city + ", " + state + ", " + zip + "\" could not be inserted into the database.");
			}
		} catch (SQLException e) {
			logger.error("SQLException while adding address: \"" + street + ", " + city + ", " + state + ", " + zip + "\" to the database.");
			throw new RuntimeException(e);
		} 
		
		return addressId;
	}
	
	public class ZipcodeFinder {
		public static Optional<Integer> findZipcode(Connection connection, String zipcode) {
			int zipcodeId = 0;
			String query = "select zipcodeId from Zipcode where zipcode = ?;";
			
			try (PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setString(1, zipcode);
				ResultSet results = statement.executeQuery();
				if (results.next()) {
					zipcodeId = results.getInt("zipcodeId");
				} else {
					return Optional.empty();
				}
				// if there is another result, it is a duplicate Zipcode entry that indicates a data abnormality
				if (results.next()) {
					int duplicateZipcodeId = results.getInt("zipcodeId");
					throw new IllegalStateException("Zipcode: \"" + zipcode + "\" with id: " + zipcodeId + " has duplicate with id: " + duplicateZipcodeId + " in the database.");
				}
			} catch (SQLException e) {
				System.err.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			} 
			
			return Optional.of(zipcodeId);
		}
	}
	
	public class ZipcodeAdder {
		public static int addZipcode(Connection connection, String zipcode, int stateId) {
			int zipcodeId = 0;
			String query = "insert into Zipcode(stateId, zipcode) values (?, ?);";
							
			try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
				statement.setInt(1, stateId);
				statement.setString(2, zipcode);
				statement.executeUpdate();
				ResultSet key = statement.getGeneratedKeys();
				if (key.next()) {
					zipcodeId = key.getInt(1);
				} else {
					throw new RuntimeException("Zipcode: \"" + zipcode + "\" could not be inserted into the database.");
				}
			} catch (SQLException e) {
				System.err.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			} 
			
			return zipcodeId;
		}
	}
	
	public class StateFinder {
		public static Optional<Integer> findState(Connection connection, String state) {
			int stateId = 0;
			String query = "select stateId from State where stateName = ?;";
			
			try (PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setString(1, state);
				ResultSet results = statement.executeQuery();
				if (results.next()) {
					stateId = results.getInt("stateId");
				} else {
					return Optional.empty();
				}
				// if there is another result, it is a duplicate State entry that indicates a data abnormality
				if (results.next()) {
					stateId = results.getInt("zipcodeId");
					throw new IllegalStateException("Duplicate State \"" + state + "\" with id: " + stateId + " found in the database.");
				}
			} catch (SQLException e) {
				System.err.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			} 
			
			return Optional.of(stateId);
		}
	}
	
	public class StateAdder {
		public static int addState(Connection connection, String state) {			
			int stateId = 0;
			String query = "insert into State(stateName) values (?);";
				
			try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
				statement.setString(1, state);
				statement.executeUpdate();
				ResultSet key = statement.getGeneratedKeys();
				if (key.next()) {
					stateId = key.getInt(1);
				} else {
					throw new RuntimeException("State: \"" + state + "\" could not be inserted into the database.");
				}
			} catch (SQLException e) {
				System.err.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			} 
			
			return stateId;
		}
	}
}
