package com.vgb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vgb.database_factories.CompanyFactory;
import com.vgb.database_factories.ConnectionFactory;
import com.vgb.database_factories.InvoiceFactory;
import com.vgb.database_factories.ItemFactory;
import com.vgb.database_factories.PersonFactory;

/**
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 *
 */
public class InvoiceData {
    private static final Logger logger = LogManager.getLogger(InvoiceData.class);

	/**
	 * Removes all records from all tables in the database.
	 */
	public static void clearDatabase() {
		String[] tables = {"InvoiceItem", "Invoice", "Item", "Company", "Email", "Person", "Address", "Zipcode", "State"};
		Connection connection = ConnectionFactory.getConnection();

		try (Statement statement = connection.createStatement()) {
			for (String table : tables) {
				int rowsAffected = statement.executeUpdate("delete from " + table);
				logger.info(rowsAffected + " rows cleared from table: " + table);
			}
		} catch (SQLException e) {
			logger.error("SQLException encountered while clearing the database.");
			throw new RuntimeException(e);
		}
		logger.info("Database successfully cleared.");
		logger.warn("Database cleared.");
	}
	
//	drop table if exists InvoiceItem;
//	drop table if exists Invoice;
//	drop table if exists Item;
//	drop table if exists Company;
//	drop table if exists Email;
//	drop table if exists Person;
//	drop table if exists Address;
//	drop table if exists Zipcode;
//	drop table if exists State;

	/**
	 * Method to add a person record to the database with the provided data.
	 *
	 * @param personUuid
	 * @param firstName
	 * @param lastName
	 * @param phone
	 )*/
	public static void addPerson(UUID personUuid, String firstName, String lastName, String phone) {
		logger.warn("Adding person: " + firstName + " " + lastName + " with UUID: \"" + personUuid + "\".");

		Connection connection = ConnectionFactory.getConnection();
		String query = "insert into Person(uuid, firstName, lastName, phoneNumber) values (?, ?, ?, ?);";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, personUuid.toString());
			statement.setString(2, firstName);
			statement.setString(3, lastName);
			statement.setString(4, phone);
			int rowsAffected = statement.executeUpdate();
			
			if (rowsAffected != 1) {
				logger.warn("Person with UUID: \"" + personUuid + "\" was not inserted into the database. " + rowsAffected + " rows were affected.");
			}
		} catch (SQLException e) {
			logger.error("SQLException encountered while adding Person with UUID: \"" + personUuid + "\" to the database.");
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
		logger.warn("Adding email: \"" + email + "\" to person with UUID: \"" + personUuid + "\".");
		
		if (email == null) {
			logger.warn("Cannot add null email to person with UUID: \"" + personUuid + "\". No changes made to database.");
			return;
		}
		
		if (email.isEmpty()) {
			logger.warn("Cannot add empty email to person with UUID: \"" + personUuid + "\". No changes made to the database.");
			return;
		}
		
		Connection connection = ConnectionFactory.getConnection();
		
		int personId = 0;
		Optional<Integer> foundId = PersonFactory.getId(connection, personUuid);
		if (foundId.isPresent()) {
			personId = foundId.get();
		} else {
			logger.warn("Person with UUID: \"" + personUuid + "\" not found while adding email: \"" + email + "\". No changes made to the database.");
			return;
		}
		
		String query = "insert into Email(personId, email) values (?, ?);";
				
		try (PreparedStatement statement = connection.prepareStatement(query)) {			
			statement.setInt(1, personId);
			statement.setString(2, email);
			int rowsAffected = statement.executeUpdate();
			
			if (rowsAffected != 1) {
				logger.warn("Email: \"" + email + "\" was not inserted into the database. " + rowsAffected + " rows were affected.");
			}
		} catch (SQLException e) {
			logger.error("SQLException encountered while adding Email: \"" + email + "\" to Person with UUID: \"" + personUuid + "\" to the database");
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
	public static void addCompany(UUID companyUuid, UUID contactUuid, String name, String street, String city, String state, String zip) {
		Connection connection = ConnectionFactory.getConnection();
		
		int personId = 0;
		Optional<Integer> foundId = PersonFactory.getId(connection, contactUuid);
		if (foundId.isPresent()) {
			personId = foundId.get();
		} else {
			logger.warn("Person with UUID: \"" + contactUuid + "\" not found while adding company with UUID: \"" + companyUuid + "\". No changes made to the database.");
			return;
		}
		
		String query = "insert into Company(uuid, contactId, name, addressId) values (?, ?, ?, ?);";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, companyUuid.toString());
			statement.setInt(2, personId);
			statement.setString(3, name);
			int addressId = AddressAdder.addAddress(connection, street, city, state, zip);
			statement.setInt(4, addressId);
			int rowsAffected = statement.executeUpdate();
			
			if (rowsAffected != 1) {
				logger.warn("Company with UUID: \"" + companyUuid + "\" was not inserted into the database. " + rowsAffected + " rows were affected.");
			}
		} catch (SQLException e) {
			logger.error("SQLException encountered while adding Company with UUID: \"" + companyUuid + "\".");
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
		logger.warn("Adding Equipment with UUID: \"" + equipmentUuid + "\".");

		Connection connection = ConnectionFactory.getConnection();
		String query = "insert into Item(uuid, name, discriminator, equipmentModelNumber, equipmentRetailPrice) values (?, ?, ?, ?, ?);";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, equipmentUuid.toString());
			statement.setString(2, name);
			statement.setString(3, "Equipment");
			statement.setString(4, modelNumber);
			statement.setDouble(5, retailPrice);
			int rowsAffected = statement.executeUpdate();
			
			if (rowsAffected != 1) {
				logger.warn("Equipment with UUID: \"" + equipmentUuid + "\" was not inserted into the database. " + rowsAffected + " rows were affected.");
			}
		} catch (SQLException e) {
			logger.error("SQLException encountered while adding Equipment with UUID: \"" + equipmentUuid + "\" to the database.");
			throw new RuntimeException(e);
		}
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
		Connection connection = ConnectionFactory.getConnection();
		String query = "insert into Item(uuid, name, discriminator, materialUnit, materialPricePerUnit) values (?, ?, ?, ?, ?);";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, materialUuid.toString());
			statement.setString(2, name);
			statement.setString(3, "Material");
			statement.setString(4, unit);
			statement.setDouble(5, pricePerUnit);
			int rowsAffected = statement.executeUpdate();
			
			if (rowsAffected != 1) {
				logger.warn("Material with UUID: \"" + materialUuid + "\" was not inserted into the database. " + rowsAffected + " rows were affected.");
			}
		} catch (SQLException e) {
			logger.error("SQLException encountered while adding Material with UUID: \"" + materialUuid + "\" to the database.");
			throw new RuntimeException(e);
		}
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
		Connection connection = ConnectionFactory.getConnection();
		
		int subcontractorId = 0;
		Optional<Integer> foundId = CompanyFactory.getId(connection, servicerUuid);
		if (foundId.isPresent()) {
			subcontractorId = foundId.get();
		} else {
			logger.warn("Company with UUID: \"" + servicerUuid + "\" not found while adding contract with UUID: \"" + contractUuid + "\". No changes made to the database.");
			return;
		}

		String query = "insert into Item(uuid, name, discriminator, contractSubcontractorId) values (?, ?, ?, ?);";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, contractUuid.toString());
			statement.setString(2, name);
			statement.setString(3, "Contract");
			statement.setInt(4, subcontractorId);
			int rowsAffected = statement.executeUpdate();
			
			if (rowsAffected != 1) {
				logger.warn("Contract with UUID: \"" + contractUuid + "\" was not inserted into the database. " + rowsAffected + " rows were affected.");
			}
		} catch (SQLException e) {
			logger.error("SQLException encountered while adding Contract with UUID: \"" + contractUuid + "\" to the database.");
			throw new RuntimeException(e);
		}
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
		Connection connection = ConnectionFactory.getConnection();
		
		int customerId = 0;
		Optional<Integer> foundCustomerId = CompanyFactory.getId(connection, customerUuid);
		if (foundCustomerId.isPresent()) {
			customerId = foundCustomerId.get();
		} else {
			logger.warn("Company with UUID: \"" + customerUuid + "\" not found while adding invoice with UUID: \"" + invoiceUuid + "\". No changes made to the database.");
			return;
		}
		
		int salespersonId = 0;
		Optional<Integer> foundSalespersonId = PersonFactory.getId(connection, salesPersonUuid);
		if (foundSalespersonId.isPresent()) {
			salespersonId = foundSalespersonId.get();
		} else {
			logger.warn("Person with UUID: \"" + salesPersonUuid + "\" not found while adding invoice with UUID: \"" + invoiceUuid + "\". No changes made to the database.");
			return;
		}
		
		String query = "insert into Invoice(uuid, date, customerId, salespersonId) values (?, ?, ?, ?);";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, invoiceUuid.toString());
			statement.setString(2, date.toString());
			statement.setInt(3, customerId);
			statement.setInt(4, salespersonId);
			int rowsAffected = statement.executeUpdate();
			
			if (rowsAffected != 1) {
				logger.warn("Invoice with UUID: \"" + invoiceUuid + "\" was not inserted into the database. " + rowsAffected + " rows were affected.");
			}
		} catch (SQLException e) {
			logger.error("SQLException encountered while adding Invoice with UUID: \"" + invoiceUuid + "\" to the database.");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds an Equipment purchase record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 */
	public static void addEquipmentPurchaseToInvoice(UUID invoiceUuid, UUID itemUuid) {
		logger.warn("Adding Equipment purchase with UUID: \"" + itemUuid + "\" to invoice with UUID: \"" + invoiceUuid + "\".");

		Connection connection = ConnectionFactory.getConnection();
		
		int invoiceId = 0;
		Optional<Integer> foundInvoiceId = InvoiceFactory.getId(connection, invoiceUuid);
		if (foundInvoiceId.isPresent()) {
			invoiceId = foundInvoiceId.get();
		} else {
			logger.warn("Invoice with UUID: \"" + invoiceUuid + "\" not found while adding equipment purchase with UUID: \"" + itemUuid + "\". No changes made to the database.");
			return;
		}
		
		int itemId = 0;
		Optional<Integer> foundItemId = ItemFactory.getId(connection, itemUuid);
		if (foundItemId.isPresent()) {
			itemId = foundItemId.get();
		} else {
			logger.warn("Equipment with UUID: \"" + itemUuid + "\" not found while adding equipment purchase to invoice with UUID: \"" + invoiceUuid + "\". No changes made to the database.");
			return;
		}
		
		String query = "insert into InvoiceItem(invoiceId, itemId, equipmentDiscriminator) values (?, ?, ?);";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, invoiceId);
			statement.setInt(2, itemId);
			statement.setString(3, "Purchase");
			int rowsAffected = statement.executeUpdate();
			
			if (rowsAffected != 1) {
				logger.warn("Equipment purchase with UUID: \"" + itemUuid + "\" was not added to invoice with UUID: \"" + invoiceUuid + "\".");
			}
		} catch (SQLException e) {
			logger.error("SQLException encountered while adding equipment purchase with UUID: \"" + itemUuid + "\" to invoice with UUID: \"" + invoiceUuid + "\" in the database.");
			throw new RuntimeException(e);
		}
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
		logger.warn("Adding Equipment lease with UUID: \"" + itemUuid + "\" to invoice with UUID: \"" + invoiceUuid + "\".");

		Connection connection = ConnectionFactory.getConnection();
		
		int invoiceId = 0;
		Optional<Integer> foundInvoiceId = InvoiceFactory.getId(connection, invoiceUuid);
		if (foundInvoiceId.isPresent()) {
			invoiceId = foundInvoiceId.get();
		} else {
			logger.warn("Invoice with UUID: \"" + invoiceUuid + "\" not found while adding equipment lease with UUID: \"" + itemUuid + "\". No changes made to the database.");
			return;
		}
		
		int itemId = 0;
		Optional<Integer> foundItemId = ItemFactory.getId(connection, itemUuid);
		if (foundItemId.isPresent()) {
			itemId = foundItemId.get();
		} else {
			logger.warn("Equipment with UUID: \"" + itemUuid + "\" not found while adding equipment lease to invoice with UUID: \"" + invoiceUuid + "\". No changes made to the database.");
			return;
		}
		
		String query = "insert into InvoiceItem(invoiceId, itemId, equipmentDiscriminator, leaseStartDate, leaseEndDate, leaseAmount) values (?, ?, ?, ?, ?, ?);";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, invoiceId);
			statement.setInt(2, itemId);
			statement.setString(3, "Lease");
			statement.setString(4, start.toString());
			statement.setString(5, end.toString());
			// Must get the lease amount from the EquipmentLease for the database
			Equipment equipment = (Equipment) ItemFactory.loadItem(connection, itemId);
			EquipmentLease lease = new EquipmentLease(equipment, start, end);
			statement.setDouble(6, lease.getTotal().getTotal()); 
			int rowsAffected = statement.executeUpdate();
			
			if (rowsAffected != 1) {
				logger.warn("Equipment lease with UUID: \"" + itemUuid + "\" was not added to invoice with UUID: \"" + invoiceUuid + "\".");
			}
		} catch (SQLException e) {
			logger.error("SQLException encountered while adding equipment lease with UUID: \"" + itemUuid + "\" to invoice with UUID: \"" + invoiceUuid + "\" in the database.");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds an Equipment rental record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param numberOfHours
	 */
	public static void addEquipmentRentalToInvoice(UUID invoiceUuid, UUID itemUuid, double numberOfHours) {
		logger.warn("Adding Equipment rental with UUID: \"" + itemUuid + "\" to invoice with UUID: \"" + invoiceUuid + "\".");

		Connection connection = ConnectionFactory.getConnection();
		
		int invoiceId = 0;
		Optional<Integer> foundInvoiceId = InvoiceFactory.getId(connection, invoiceUuid);
		if (foundInvoiceId.isPresent()) {
			invoiceId = foundInvoiceId.get();
		} else {
			logger.warn("Invoice with UUID: \"" + invoiceUuid + "\" not found while adding equipment rental with UUID: \"" + itemUuid + "\". No changes made to the database.");
			return;
		}
		
		int itemId = 0;
		Optional<Integer> foundItemId = ItemFactory.getId(connection, itemUuid);
		if (foundItemId.isPresent()) {
			itemId = foundItemId.get();
		} else {
			logger.warn("Equipment with UUID: \"" + itemUuid + "\" not found while adding equipment rental to invoice with UUID: \"" + invoiceUuid + "\". No changes made to the database.");
			return;
		}
		
		
		logger.warn("itemId=" + itemId + " meaning it was found in the database lol.");	
		
		Equipment equipment = (Equipment) ItemFactory.loadItem(connection, itemId);
		
		String query = "insert into InvoiceItem(invoiceId, itemId, equipmentDiscriminator, rentedHours, rentAmount) values (?, ?, ?, ?, ?);";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, invoiceId);
			statement.setInt(2, itemId);
			statement.setString(3, "Rental");
			statement.setDouble(4, numberOfHours);
			// Must get the rent amount from the EquipmentRental for the database
			EquipmentRental rental = new EquipmentRental(equipment, numberOfHours);
			statement.setDouble(5, rental.getTotal().getTotal()); 
			int rowsAffected = statement.executeUpdate();
			
			if (rowsAffected != 1) {
				logger.error("Equipment lease with UUID: \"" + itemUuid + "\" was not added to invoice with UUID: \"" + invoiceUuid + "\".");
				throw new RuntimeException("Equipment lease with UUID: \"" + itemUuid + "\" was not added to invoice with UUID: \"" + invoiceUuid + "\".");
			}
		} catch (SQLException e) {
			logger.error("SQLException encountered while adding equipment lease with UUID: \"" + itemUuid + "\" to invoice with UUID: \"" + invoiceUuid + "\" in the database.");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a material record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param numberOfUnits
	 */
	public static void addMaterialToInvoice(UUID invoiceUuid, UUID itemUuid, int numberOfUnits) {
		logger.warn("Adding Material with UUID: \"" + itemUuid + "\" to invoice with UUID: \"" + invoiceUuid + "\".");

		Connection connection = ConnectionFactory.getConnection();
		
		int invoiceId = 0;
		Optional<Integer> foundInvoiceId = InvoiceFactory.getId(connection, invoiceUuid);
		if (foundInvoiceId.isPresent()) {
			invoiceId = foundInvoiceId.get();
		} else {
			logger.warn("Invoice with UUID: \"" + invoiceUuid + "\" not found while adding material with UUID: \"" + itemUuid + "\". No changes made to the database.");
			return;
		}
		
		int itemId = 0;
		Optional<Integer> foundItemId = ItemFactory.getId(connection, itemUuid);
		if (foundItemId.isPresent()) {
			itemId = foundItemId.get();
		} else {
			logger.warn("Material with UUID: \"" + itemUuid + "\" not found while adding material to invoice with UUID: \"" + invoiceUuid + "\". No changes made to the database.");
			return;
		}
		
		String query = "insert into InvoiceItem(invoiceId, itemId, materialQuantity) values (?, ?, ?);";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, invoiceId);
			statement.setInt(2, itemId);
			statement.setInt(3, numberOfUnits); 
			int rowsAffected = statement.executeUpdate();
			
			if (rowsAffected != 1) {
				logger.error("Material with UUID: \"" + itemUuid + "\" was not added to invoice with UUID: \"" + invoiceUuid + "\".");
				throw new RuntimeException("Material with UUID: \"" + itemUuid + "\" was not added to invoice with UUID: \"" + invoiceUuid + "\".");
			}
		} catch (SQLException e) {
			logger.error("SQLException encountered while adding Material with UUID: \"" + itemUuid + "\" to invoice with UUID: \"" + invoiceUuid + "\" in the database.");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a contract record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param amount
	 */
	public static void addContractToInvoice(UUID invoiceUuid, UUID itemUuid, double amount) {
		logger.warn("Adding Contract with UUID: \"" + itemUuid + "\" to invoice with UUID: \"" + invoiceUuid + "\".");

		Connection connection = ConnectionFactory.getConnection();
		
		int invoiceId = 0;
		Optional<Integer> foundInvoiceId = InvoiceFactory.getId(connection, invoiceUuid);
		if (foundInvoiceId.isPresent()) {
			invoiceId = foundInvoiceId.get();
		} else {
			logger.warn("Invoice with UUID: \"" + invoiceUuid + "\" not found while adding contract with UUID: \"" + itemUuid + "\". No changes made to the database.");
			return;
		}
		
		int itemId = 0;
		Optional<Integer> foundItemId = ItemFactory.getId(connection, itemUuid);
		if (foundItemId.isPresent()) {
			itemId = foundItemId.get();
		} else {
			logger.warn("Contract with UUID: \"" + itemUuid + "\" not found while adding contract to invoice with UUID: \"" + invoiceUuid + "\". No changes made to the database.");
			return;
		}
		
		String query = "insert into InvoiceItem(invoiceId, itemId, contractAmount) values (?, ?, ?);";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, invoiceId);
			statement.setInt(2, itemId);
			statement.setDouble(3, amount); 
			int rowsAffected = statement.executeUpdate();
			
			if (rowsAffected != 1) {
				logger.error("Contract with UUID: \"" + itemUuid + "\" was not added to invoice with UUID: \"" + invoiceUuid + "\".");
				throw new RuntimeException("Contract with UUID: \"" + itemUuid + "\" was not added to invoice with UUID: \"" + invoiceUuid + "\".");
			}
		} catch (SQLException e) {
			logger.error("SQLException encountered while adding Contract with UUID: \"" + itemUuid + "\" to invoice with UUID: \"" + invoiceUuid + "\" in the database.");
			throw new RuntimeException(e);
		}
	}
}