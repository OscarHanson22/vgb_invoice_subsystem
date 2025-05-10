/**
 * Authors: Oscar Hanson and Ermias Wolde
 * Date: 5/9/2025
 * Purpose: Class that reads Company objects from the database.
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

import com.vgb.Address;
import com.vgb.Company;
import com.vgb.Person;

/**
 * A class that loads Company objects (and related information) from the invoicing database.
 */
public class CompanyFactory {
    private static final Logger logger = LogManager.getLogger(CompanyFactory.class);

    /**
     * Loads a Company object from the Company table in the database with the specified companyId.
     * 
     * @param connection The connection to the database.
     * @param companyId The companyId of the company in the database.
     */
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
				logger.error("Company with companyId: " + companyId + " not found in the database.");
				throw new IllegalStateException("Company with companyId: " + companyId + " not found in the database.");
			}
		// Only occurs when the `UUID.fromString(...)` above fails
		} catch (IllegalArgumentException e) {
			logger.error("UUID for company with companyId: " + companyId + " is formatted incorrectly: \"" + uuidString + "\".");
			throw new RuntimeException(e);
		} catch (SQLException e) {
			logger.error("SQLException encounted while loading company with companyId: " + companyId + " from the database.");
			throw new RuntimeException(e);
		} 
			
		return company;
	}
	
	/**
     * Gets the companyId of a company with the specified UUID.
     * 
     * @param connection The connection to the database. 
     * @param uuid The uuid column value of the Company table in the database.
     */
	public static Optional<Integer> getId(Connection connection, UUID uuid) {		
		Optional<Integer> companyId = Optional.empty();
		String query = "select companyId from Company where uuid = ?;";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, uuid.toString());
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				companyId = Optional.of(results.getInt("companyId"));
			} else {
				logger.info("Company with UUID: \"" + uuid + "\" not found in the database.");
			}
		} catch (SQLException e) {
			logger.error("SQLException encountered while getting companyId for company with UUID: \"" + uuid + "\".");
			throw new RuntimeException(e);
		} 
			
		return companyId;
	}
	
	/**
	 * Loads all Company objects from the database.
	 * 
	 * @param connection The connection to the database.
	 */
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
			logger.error("SQLException encountered while loading all companies from database.");
			throw new RuntimeException(e);
		}
		
		if (companies.size() == 0) {
			logger.warn("No companies loaded from database.");
		}
			
		return companies;
	}
}
