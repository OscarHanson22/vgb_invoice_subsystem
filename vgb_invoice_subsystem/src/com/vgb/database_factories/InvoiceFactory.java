/**
 * Authors: Oscar Hanson and Ermias Wolde
 * Date: 5/9/2025
 * Purpose: Class that reads Invoice objects from the database.
 */

package com.vgb.database_factories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vgb.Company;
import com.vgb.Invoice;
import com.vgb.Item;
import com.vgb.Person;

/**
 * A class that loads Invoice objects and related information from the Invoice table in the database.
 */
public class InvoiceFactory {
    private static final Logger logger = LogManager.getLogger(InvoiceFactory.class);

    /**
     * Loads a single Invoice object from the database's Invoice table with the specified invoiceId.
     * 
     * @param connection The connection to the database.
     * @param invoiceId The invoiceId of the specified invoice in the database.
     */
	public static Invoice loadInvoice(Connection connection, int invoiceId) {
		Invoice invoice = null;
		String uuidString = null; 
		String dateString = null; 
		
		// first create the invoice
		String query = "select uuid, customerId, salespersonId, date from Invoice where invoiceId = ?;";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, invoiceId);
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				uuidString = results.getString("uuid");
				UUID uuid = UUID.fromString(uuidString);
				int customerId = results.getInt("customerId");
				Company customer = CompanyFactory.loadCompany(connection, customerId);
				int salespersonId = results.getInt("salespersonId");
				Person salesperson = PersonFactory.loadPerson(connection, salespersonId);
				dateString = results.getString("date");
				LocalDate date = LocalDate.parse(dateString);
				invoice = new Invoice(uuid, customer, salesperson, date);
			} else {
				throw new IllegalStateException("Invoice with invoiceId: " + invoiceId + " not found in the database.");
			}
		// Only occurs when the `UUID.fromString(...)` above fails
		} catch (IllegalArgumentException e) {
			logger.error("UUID for invoice with invoiceId: " + invoiceId + " is formatted incorrectly: \"" + uuidString + "\".");
			throw new RuntimeException(e);
		// Only occurs when the `LocalDate.parse(...)` above fails
		} catch (DateTimeParseException e) {
			logger.error("LocalDate for invoice with invoiceId: " + invoiceId + " is formatted incorrectly: \"" + dateString + "\".");
			throw new RuntimeException(e);
		} catch (SQLException e) {
			logger.error("SQLException encountered while loading Invoice with invoiceId: " + invoiceId + " from the database.");
			throw new RuntimeException(e);
		} 
		
		// then populate its items 
		query = 
			"select itemId from Invoice invoice "
			+ "join InvoiceItem invoiceItem on invoice.invoiceId = invoiceItem.invoiceId "
			+ "where invoice.invoiceId = ?;";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, invoiceId);
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				int itemId = results.getInt("itemId");
				Item item = InvoiceItemFactory.loadInvoiceItem(connection, itemId);
				invoice.addItem(item);
			}
		} catch (SQLException e) {
			logger.error("SQLException encountered while populating items of Invoice with invoiceId: " + invoiceId);
			throw new RuntimeException(e);
		} 
			
		return invoice;
	}
	
	/**
	 * Returns the invoiceId (if it exists) of the invoice with the specified UUID.
	 * 
	 * @param connection The connection to the database.
	 * @param uuid The UUID of the desired invoice. 
	 */
	public static Optional<Integer> getId(Connection connection, UUID uuid) {
		Optional<Integer> invoiceId = Optional.empty();
		String query = "select invoiceId from Invoice where uuid = ?;";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, uuid.toString());
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				invoiceId = Optional.of(results.getInt("invoiceId"));
			} else {
				logger.info("Invoice with UUID: \"" + uuid + "\" not found in the database.");
			}
		} catch (SQLException e) {
			logger.error("SQLException encountered while getting invoiceId for Invoice with UUID: \"" + uuid + "\" from the database.");
			throw new RuntimeException(e);
		} 
			
		return invoiceId;
	}
	
	/**
	 * Loads all Invoice objects from the Invoice table in the database.
	 * 
	 * @param connection The connection to the database.
	 */
	public static List<Invoice> loadAllInvoices(Connection connection) {
		List<Invoice> invoices = new ArrayList<>();
		
		String query = "select invoiceId from Invoice";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				int invoiceId = results.getInt("invoiceId");
				Invoice invoice = loadInvoice(connection, invoiceId);
				invoices.add(invoice);
			}
		} catch (SQLException e) {
			logger.error("SQLException encountered while loading all invoices from the database.");
			throw new RuntimeException(e);
		}
		
		if (invoices.size() == 0) {
			logger.warn("No invoices loaded from the database.");
		}
			
		return invoices;
	}
}