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
import com.vgb.Invoice;
import com.vgb.Item;
import com.vgb.Person;

public class PersonFactory {
	public static Person loadPerson(Connection connection, int personId) {
		Person person = null;
		String uuidString = null;
		
		String query = "select uuid, firstName, lastName, phoneNumber from Person where personId = ?;";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, personId);
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				uuidString = results.getString("uuid");
				UUID uuid = UUID.fromString(uuidString);
				String firstName = results.getString("firstName");
				String lastName = results.getString("lastName");
				String phoneNumber = results.getString("phoneNumber");
				List<String> emailAddresses = EmailFactory.loadEmails(connection, personId);
				person = new Person(uuid, firstName, lastName, phoneNumber, emailAddresses);
			} else {
				throw new IllegalStateException("Person with personId: " + personId + " not found in the database.");
			}
			
		// Only occurs when the `UUID.fromString(...)` above fails
		} catch (IllegalArgumentException e) {
			System.err.println("UUID for person with personId: " + personId + " is formatted incorrectly: \"" + uuidString + "\".");
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (SQLException e) {
			System.err.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
			
		return person;
	}
	
	public static List<Person> loadAllPeople(Connection connection) {
		List<Person> people = new ArrayList<>();
		
		String query = "select personId from Person";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				int personId = results.getInt("personId");
				Person person = loadPerson(connection, personId);
				people.add(person);
			}
		} catch (SQLException e) {
			System.err.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
			
		return people;
	}
	
	public static void main(String[] args) {
		Connection connection = null;

		try {
			connection = ConnectionFactory.connect();
		} catch (SQLException e) {
			System.err.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		System.out.println("Hello world!");
		
		List<Person> people = PersonFactory.loadAllPeople(connection);

		System.out.println(people);
		
		
		List<Company> companies = CompanyFactory.loadAllCompanies(connection);
		
		System.out.println(companies);
		
		Company company = CompanyFactory.loadCompany(connection, 1);
		System.out.println(company);
		
		
		List<Invoice> invoices = InvoiceFactory.loadAllInvoices(connection);
		System.out.println(invoices);
		
		Invoice invoice = InvoiceFactory.loadInvoice(connection, 5);
		System.out.println(invoice);
		
		Address address = AddressFactory.loadAddress(connection, 1);
		System.out.println(address);
		
		Item item = ItemFactory.loadItem(connection, 2);
		System.out.println(item);
	}
}