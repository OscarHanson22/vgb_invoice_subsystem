package com.vgb.database_factories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.vgb.Company;
import com.vgb.Invoice;
import com.vgb.Item;
import com.vgb.Person;

public class InvoiceFactory {
	// TODO! need to populate items using ItemFactory
	public static Invoice loadInvoice(Connection connection, int invoiceId) {
		Invoice invoice = null;
		String uuidString = null; // used for error messaging
		String dateString = null; // used for error messaging
		
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
			System.err.println("UUID for invoice with invoiceId: " + invoiceId + " is formatted incorrectly: \"" + uuidString + "\".");
			e.printStackTrace();
			throw new RuntimeException(e);
		// Only occurs when the `LocalDate.parse(...)` above fails
		} catch (DateTimeParseException e) {
			System.err.println("LocalDate for invoice with invoiceId: " + invoiceId + " is formatted incorrectly: \"" + dateString + "\".");
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (SQLException e) {
			System.err.println("SQLException: ");
			e.printStackTrace();
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
				Item item = ItemFactory.loadItem(connection, itemId);
				invoice.addItem(item);
			}
		} catch (SQLException e) {
			System.err.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
			
		return invoice;
	}
	
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
			System.err.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
			
		return invoices;
	}
}