package com.vgb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

import com.vgb.database_factories.ConnectionFactory;
import com.vgb.database_factories.PersonFactory;

/**
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 *
 */
public class InvoiceData {

	/**
	 * Removes all records from all tables in the database.
	 */
	public static void clearDatabase() {
		//TODO: implement
	}

	/**
	 * Method to add a person record to the database with the provided data.
	 *
	 * @param personUuid
	 * @param firstName
	 * @param lastName
	 * @param phone
	 */
	public static void addPerson(UUID personUuid, String firstName, String lastName, String phone) {
		String query = "insert into Person(uuid, firstName, lastName, phoneNumber) values (?, ?, ?, ?);";
		
		try (
			Connection connection = ConnectionFactory.connect();
			PreparedStatement statement = connection.prepareStatement(query);
		) {
			statement.setString(1, personUuid.toString());
			statement.setString(2, firstName);
			statement.setString(3, lastName);
			statement.setString(4, phone);
			int rowsAffected = statement.executeUpdate();
			
			if (rowsAffected != 1) {
				throw new RuntimeException("Person with uuid: \"" + personUuid + "\" was not inserted into the database. " + rowsAffected + " rows were affected.");
			}
		} catch (SQLException e) {
			System.err.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
	}

	/**
	 * Adds an email record corresponding person record corresponding to the
	 * provided <code>personUuid</code>
	 *
	 * @param personUuid
	 * @param email
	 */
	public static void addEmail(UUID personUuid, String email) {		
		String query = "insert into Email(personId, email) values (?, ?);";
				
		try (
			Connection connection = ConnectionFactory.connect();
			PreparedStatement statement = connection.prepareStatement(query);
		) {
			int personId = PersonFactory.getId(connection, personUuid);
			statement.setInt(1, personId);
			statement.setString(2, email);
			int rowsAffected = statement.executeUpdate();
			
			if (rowsAffected != 1) {
				throw new RuntimeException("Email: \"" + email + "\" was not inserted into the database. " + rowsAffected + " rows were affected.");
			}
		} catch (SQLException e) {
			System.err.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
	}

	/**
	 * Adds a company record to the database with the primary contact person identified by the
	 * given code.
	 *
	 * @param companyUuid
	 * @param name
	 * @param contactUuid
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 */
	public static void addCompany(UUID companyUuid, UUID contactUuid, String name, String street, String city, String state,
			String zip) {
		String query = "insert into Company(uuid, contactId, name, addressId) values (?, ?, ?, ?);";
		
		try (
			Connection connection = ConnectionFactory.connect();
			PreparedStatement statement = connection.prepareStatement(query);
		) {
			statement.setString(1, companyUuid.toString());
			int personId = PersonFactory.getId(connection, contactUuid);
			statement.setInt(2, personId);
			statement.setString(3, name);
			int addressId = AddressAdder.addAddress(connection, street, city, state, zip);
			statement.setInt(4, addressId);
			int rowsAffected = statement.executeUpdate();
			
			if (rowsAffected != 1) {
				throw new RuntimeException("Company with uuid: \"" + companyUuid + "\" was not inserted into the database. " + rowsAffected + " rows were affected.");
			}
		} catch (SQLException e) {
			System.err.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
	}

	/**
	 * Adds an equipment record to the database of the given values.
	 *
	 * @param equipmentUuid
	 * @param name
	 * @param modelNumber
	 * @param retailPrice
	 */
	public static void addEquipment(UUID equipmentUuid, String name, String modelNumber, double retailPrice) {
		//TODO: implement

	}

	/**
	 * Adds an material record to the database of the given values.
	 *
	 * @param materialUuid
	 * @param name
	 * @param unit
	 * @param pricePerUnit
	 */
	public static void addMaterial(UUID materialUuid, String name, String unit, double pricePerUnit) {
		//TODO: implement

	}

	/**
	 * Adds an contract record to the database of the given values.
	 *
	 * @param contractUuid
	 * @param name
	 * @param unit
	 * @param pricePerUnit
	 */
	public static void addContract(UUID contractUuid, String name, UUID servicerUuid) {
		//TODO: implement

	}

	/**
	 * Adds an Invoice record to the database with the given data.
	 *
	 * @param invoiceUuid
	 * @param customerUuid
	 * @param salesPersonUuid
	 * @param date
	 */
	public static void addInvoice(UUID invoiceUuid, UUID customerUuid, UUID salesPersonUuid, LocalDate date) {
		//TODO: implement

	}


	/**
	 * Adds an Equipment purchase record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 */
	public static void addEquipmentPurchaseToInvoice(UUID invoiceUuid, UUID itemUuid) {
		//TODO: implement

	}

	/**
	 * Adds an Equipment lease record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param start
	 * @param end
	 */
	public static void addEquipmentLeaseToInvoice(UUID invoiceUuid, UUID itemUuid, LocalDate start, LocalDate end) {
		//TODO: implement

	}

	/**
	 * Adds an Equipment rental record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param numberOfHours
	 */
	public static void addEquipmentRentalToInvoice(UUID invoiceUuid, UUID itemUuid, double numberOfHours) {
		//TODO: implement

	}

	/**
	 * Adds a material record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param numberOfUnits
	 */
	public static void addMaterialToInvoice(UUID invoiceUuid, UUID itemUuid, int numberOfUnits) {
		//TODO: implement

	}

	/**
	 * Adds a contract record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param amount
	 */
	public static void addContractToInvoice(UUID invoiceUuid, UUID itemUuid, double amount) {
		//TODO: implement

	}


}