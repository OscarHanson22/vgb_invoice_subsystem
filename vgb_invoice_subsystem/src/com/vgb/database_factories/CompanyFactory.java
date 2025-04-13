package com.vgb.database_factories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.vgb.Address;
import com.vgb.Company;
import com.vgb.Person;

public class CompanyFactory {
	public static Company loadCompany(Connection connection, int companyId) {
		Company company = null;
		String uuidString = null;
		
		String query = "select contactId, addressId, uuid, name from Company where companyId = ?";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, companyId);
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				uuidString = results.getString("uuid");
				UUID uuid = UUID.fromString(uuidString);
				int contactId = results.getInt("contactId");
				Person contact = PersonFactory.loadPerson(connection, contactId);
				String name = results.getString("name");
				int addressId = results.getInt("addressId");
				Address address = AddressFactory.loadAddress(connection, addressId);
				company = new Company(uuid, contact, name, address);
			} else {
				throw new IllegalStateException("Company with companyId: " + companyId + " not found in the database.");
			}
		// Only occurs when the `UUID.fromString(...)` above fails
		} catch (IllegalArgumentException e) {
			System.err.println("UUID for company with companyId: " + companyId + " is formatted incorrectly: \"" + uuidString + "\".");
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (SQLException e) {
			System.err.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
			
		return company;
	}
	
	public static List<Company> loadAllCompanies(Connection connection) {
		List<Company> companies = new ArrayList<>();
		
		String query = "select companyId from Company";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				int companyId = results.getInt("companyId");
				Company company = loadCompany(connection, companyId);
				companies.add(company);
			}
		} catch (SQLException e) {
			System.err.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
			
		return companies;
	}
}
