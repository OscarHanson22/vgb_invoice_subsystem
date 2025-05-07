package com.vgb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A class that handles adding address information to the database. This class handles the Address, Zipcode, and State tables of the database.
 */
public class AddressAdder {
    private static final Logger logger = LogManager.getLogger(AddressAdder.class);
	
    /**
     * Adds address information to the database, accounting for normalization. 
     * 
     * @param connection The connection to the database.
     * @param street The street of the address to be added to the database.
     * @param city The city of the address to be added to the database.
     * @param state The state of the address to be added to the database.
     * @param zip The zipcode of the address to be added to the database.
     */
	public static int addAddress(Connection connection, String street, String city, String state, String zip) {
		int addressId = 0;
		
		// find or add the state information to the database
		int stateId = 0;
		Optional<Integer> foundStateId = StateFinder.findState(connection, state);
		if (foundStateId.isEmpty()) {
			stateId = StateAdder.addState(connection, state);
		} else {
			stateId = foundStateId.get();
		}
		
		// find or add the zipcode information to the database
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
				logger.error("Address: \"" + street + ", " + city + ", " + state + ", " + zip + "\" was not inserted into the database.");
				throw new RuntimeException("Address: \"" + street + ", " + city + ", " + state + ", " + zip + "\" was not inserted into the database.");
			}
		} catch (SQLException e) {
			logger.error("SQLException while adding address: \"" + street + ", " + city + ", " + state + ", " + zip + "\" to the database.");
			throw new RuntimeException(e);
		} 
		
		return addressId;
	}
	
	/**
	 * A class that gets the zipcodeId for a specified zipcode string.
	 */
	private class ZipcodeFinder {
		/**
		 * Finds and returns (if it exists) the zipcodeId of the zipcode with the specified zipcode string.
		 * 
		 * @param connection The connection to the database.
		 * @param zipcode The zipcode of the entry in the Zipcode table with the desired zipcodeId.
		 */
		public static Optional<Integer> findZipcode(Connection connection, String zipcode) {
			int zipcodeId = 0;
			String query = "select zipcodeId from Zipcode where zipcode = ?;";
			
			try (PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setString(1, zipcode);
				ResultSet results = statement.executeQuery();
				if (results.next()) {
					zipcodeId = results.getInt("zipcodeId");
				} else {
					logger.info("Zipcode with string: " + zipcode + " was not found in the database.");
					return Optional.empty();
				}
				// if there is another result, it is a duplicate Zipcode entry that indicates a data abnormality
				if (results.next()) {
					int duplicateZipcodeId = results.getInt("zipcodeId");
					logger.error("Zipcode: \"" + zipcode + "\" with id: " + zipcodeId + " has duplicate with id: " + duplicateZipcodeId + " in the database.");
					throw new IllegalStateException("Zipcode: \"" + zipcode + "\" with id: " + zipcodeId + " has duplicate with id: " + duplicateZipcodeId + " in the database.");
				}
			} catch (SQLException e) {
				logger.error("SQLException encountered while attempting to find zipcodeId of zipcode with string: \"" + zipcode + "\".");
				throw new RuntimeException(e);
			} 
			
			return Optional.of(zipcodeId);
		}
	}
	
	/**
	 * A class that adds zipcode information to the Zipcode table in the database.
	 */
	private class ZipcodeAdder {
		/**
		 * Adds zipcode information to the Zipcode table in the database.
		 * 
		 * @param connection The connection to the database.
		 * @param zipcode The zipcode string to be added to the database.
		 * @param stateId The stateId that corresponds to the zipcode.
		 */
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
					logger.error("Zipcode: \"" + zipcode + "\" was not inserted into the database.");
					throw new IllegalStateException("Zipcode: \"" + zipcode + "\" was not inserted into the database.");
				}
			} catch (SQLException e) {
				logger.error("SQLException encountered while attempting to add zipcode with zipcode string: \"" + zipcode + "\" and stateId: " + stateId + ".");
				throw new RuntimeException(e);
			} 
			
			return zipcodeId;
		}
	}
	
	/**
	 * A class that gets the stateId for a state string in the database.
	 */
	private class StateFinder {
		/**
		 * Finds and returns (if it exists) the stateId of the specified state string.
		 * 
		 * @param connection The connection to the database.
		 * @param state The state string of the desired stateId.
		 */
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
					int duplicateStateId = results.getInt("zipcodeId");
					logger.error("State: \"" + state + "\" with stateId: " + stateId + " has a duplicate with stateId: " + duplicateStateId + " in the database.");
					throw new IllegalStateException("State: \"" + state + "\" with stateId: " + stateId + " has a duplicate with stateId: " + duplicateStateId + " in the database.");
				}
			} catch (SQLException e) {
				logger.error("SQLException encountered while attempting to find stateId of state: \"" + state + "\".");
				throw new RuntimeException(e);
			} 
			
			return Optional.of(stateId);
		}
	}
	
	/**
	 * A class that adds state information to the State table in the database.
	 */
	private class StateAdder {
		/**
		 * Adds state information to the State table in the database.
		 * 
		 * @param connection The connection to the database.
		 * @param state The state string to be added to the database.
		 */
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
					logger.error("State: \"" + state + "\" was not inserted into the database.");
					throw new IllegalStateException("State: \"" + state + "\" was not inserted into the database.");
				}
			} catch (SQLException e) {
				logger.error("SQLException encountered while attempting to add a state with string: \"" + state + "\" to the database.");
				throw new RuntimeException(e);
			} 
			
			return stateId;
		}
	}
}
