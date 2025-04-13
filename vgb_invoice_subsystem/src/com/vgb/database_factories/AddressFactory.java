package com.vgb.database_factories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.vgb.Address;

public class AddressFactory {
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
				throw new IllegalStateException("Address with addressId: " + addressId + " not found in the database.");
			}
		} catch (SQLException e) {
			System.err.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return address;
	}
}
