/**
 * Authors: Oscar Hanson and Ermias Wolde
 * Date: 5/9/2025
 * Purpose: Class that reads Address objects from the database.
 */

package com.vgb.database_factories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vgb.Address;

/**
 * A class that loads Address information from the invoicing database.
 */
public class AddressFactory {
    private static final Logger logger = LogManager.getLogger(AddressFactory.class);

    /**
     * Loads an Address object from the Address table of the database.
     * 
     * @param connection The connection to the database.
     * @param addressId The addressId of the specified address in the database.
     */
	public static Address loadAddress(Connection connection, int addressId) {
		Address address = null;
		
		String query = 
			"select street, city, stateName as state, zipcode from Address address "
			+ "join Zipcode zip on address.zipcodeId = zip.zipcodeId "
			+ "join State state on zip.stateId = state.stateId "
			+ "where address.addressId = ?;";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, addressId);
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				String street = results.getString("street");
				String city = results.getString("city");
				String state = results.getString("state");
				String zip = results.getString("zipcode");
				address = new Address(street, city, state, zip);				
			} else {
				logger.error("Address with addressId: " + addressId + " not found in the database.");
				throw new IllegalStateException("Address with addressId: " + addressId + " not found in the database.");
			}
		} catch (SQLException e) {
			logger.error("SQLException encountered while loading address with with addressId: " + addressId + " from the database.");
			throw new RuntimeException(e);
		}
		
		return address;
	}
}
