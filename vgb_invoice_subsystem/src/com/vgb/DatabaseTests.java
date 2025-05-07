package com.vgb;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.vgb.database_factories.ConnectionFactory;
import com.vgb.database_factories.EmailFactory;
import com.vgb.database_factories.PersonFactory;

public class DatabaseTests {
	public static final double TOLERANCE = 0.001;
	private static final Connection connection;
	
	static {
		InvoiceData.clearDatabase();
		connection = ConnectionFactory.getConnection();
	}
	
	@BeforeEach 
	public void resetDatabase() {
		InvoiceData.clearDatabase();
	}
	
	public void PersonShouldBeAddedTest(UUID uuid, String firstName, String lastName, String phoneNumber, List<String> emails) {
		Person expectedPerson = new Person(uuid, firstName, lastName, phoneNumber, emails);
		
		// add person info to the database
		InvoiceData.addPerson(uuid, firstName, lastName, phoneNumber);
		InvoiceData.addEmail(uuid, emails.get(0));
		InvoiceData.addEmail(uuid, emails.get(1));
		
		// ensure person can be found in the database
		Optional<Integer> emptyOptional = Optional.empty();
		assertTrue(PersonFactory.getId(connection, uuid) != emptyOptional);
		
		// load the person from the database
		Person actualPerson = PersonFactory.loadPerson(connection, uuid);
		
		assertTrue("The UUIDs of the expected and loaded person do not match.", expectedPerson.getUuid() == actualPerson.getUuid());
		assertTrue("The the first names of the expected and loaded person do not match.", expectedPerson.getFirstName().equals(actualPerson.getFirstName()));
		assertTrue("The the last names of the expected and loaded person do not match.", expectedPerson.getLastName().equals(actualPerson.getLastName()));
		assertTrue("The the phone numbers of the expected and loaded person do not match.", expectedPerson.getPhoneNumber().equals(actualPerson.getPhoneNumber()));
		for (int emailIndex = 0; emailIndex < emails.size(); emailIndex++) {
			String expectedEmail = expectedPerson.getEmailAddresses().get(emailIndex);
			String actualEmail = actualPerson.getEmailAddresses().get(emailIndex);
			assertTrue("The email of the expected and loaded person do not match.", expectedEmail.equals(actualEmail));
		}
	}
	
	public void PersonShouldNotBeAddedTest(UUID uuid, String firstName, String lastName, String phoneNumber) {		
		// add person info to the database
		InvoiceData.addPerson(uuid, firstName, lastName, phoneNumber);
		
		// ensure person cannot be found in the database
		Optional<Integer> emptyOptional = Optional.empty();
		assertTrue(PersonFactory.getId(connection, uuid) == emptyOptional);
	}
	
	public void EmailShouldNotBeAddedTest(UUID personUuid, String email) {
		// add email to the database
		InvoiceData.addEmail(personUuid, email);
		
		// ensure email cannot be found in the database
		assertTrue(EmailFactory.getIds(connection, personUuid).size() == 0);
	}

	/**
	 * Adds a person to the database, ensures they can be found, and loads it from the database to ensure data integrity. 
	 */
	@Test
	public void testAddPerson001() {
		UUID uuid = UUID.randomUUID();
		String firstName = "John";
		String lastName = "Doe";
		String phoneNumber = "209-877-9990";
		List<String> emails = new ArrayList<>();
		emails.add("johndow67@aol.com");
		emails.add("johndoebackup@aol.com");
		
		PersonShouldBeAddedTest(uuid, firstName, lastName, phoneNumber, emails);
	}
	
	@Test
	public void testAddPersonNullUuid() {
		UUID uuid = null;
		String firstName = "John";
		String lastName = "Doe";
		String phoneNumber = "209-877-9990";
		List<String> emails = new ArrayList<>();
		emails.add("johndow67@aol.com");
		emails.add("johndoebackup@aol.com");
		
		PersonShouldNotBeAddedTest(uuid, firstName, lastName, phoneNumber, emails);
	}
	
	@Test
	public void testAddPersonNullFirstName() {
		UUID uuid = UUID.randomUUID();
		String firstName = null;
		String lastName = "Doe";
		String phoneNumber = "209-877-9990";
		List<String> emails = new ArrayList<>();
		emails.add("johndow67@aol.com");
		emails.add("johndoebackup@aol.com");
		
		PersonShouldNotBeAddedTest(uuid, firstName, lastName, phoneNumber, emails);
	}
	
	@Test
	public void testAddPersonNullLastName() {
		UUID uuid = UUID.randomUUID();
		String firstName = "John";
		String lastName = null;
		String phoneNumber = "209-877-9990";
		List<String> emails = new ArrayList<>();
		emails.add("johndow67@aol.com");
		emails.add("johndoebackup@aol.com");
		
		PersonShouldNotBeAddedTest(uuid, firstName, lastName, phoneNumber, emails);
	}
	
	@Test
	public void testAddPersonNullPhoneNumber() {
		UUID uuid = UUID.randomUUID();
		String firstName = "John";
		String lastName = "Doe";
		String phoneNumber = null;
		List<String> emails = new ArrayList<>();
		emails.add("johndow67@aol.com");
		emails.add("johndoebackup@aol.com");
		
		PersonShouldNotBeAddedTest(uuid, firstName, lastName, phoneNumber, emails);
	}
	
	@Test
	public void testAddPersonNullEmail001() {
		UUID uuid = UUID.randomUUID();
		String firstName = "John";
		String lastName = "Doe";
		String phoneNumber = "209-877-9990";
		List<String> emails = new ArrayList<>();
		emails.add(null);
		emails.add("johndoebackup@aol.com");
		
		PersonShouldNotBeAddedTest(uuid, firstName, lastName, phoneNumber, emails);
	}
	
	@Test
	public void testAddPersonNullEmail002() {
		UUID uuid = UUID.randomUUID();
		String firstName = "John";
		String lastName = "Doe";
		String phoneNumber = "209-877-9990";
		List<String> emails = new ArrayList<>();
		emails.add("johndow67@aol.com");
		emails.add(null);
		
		PersonShouldNotBeAddedTest(uuid, firstName, lastName, phoneNumber, emails);
	}
	

//	/**
//	 * Creates an instance of a piece of equipment and tests if
//	 * its leasing cost and tax calculations are correct.
//	 */
//	@Test
//	public void testLease() {
//		UUID uuid = UUID.randomUUID();
//		String name = "Backhoe 3000";
//		String model = "BH30X2";
//		double cost = 95125.00;
//		
//		Equipment equipment = new Equipment(uuid.toString(), name, model, (int) cost);
//
//		double expectedCost = 69037.29;
//		double expectedTax = 1500.00;
//		
//		LocalDate startDate = LocalDate.parse("2024-01-01");
//		LocalDate endDate = LocalDate.parse("2026-06-01");
//
//		double actualCost = equipment.leaseCost(startDate, endDate);
//		double actualTax = equipment.leaseTax(startDate, endDate);
//
//		assertEquals(expectedCost, actualCost, TOLERANCE);
//		assertEquals(expectedTax, actualTax, TOLERANCE);
//	}
//
//	/**
//	 * Creates an instance of a piece of equipment and tests if
//	 * its rental costs and tax calculations are correct.
//	 */
//	@Test
//	public void testRental() {
//		UUID uuid = UUID.randomUUID();
//		String name = "Backhoe 3000";
//		String model = "BH30X2";
//		double cost = 95125.00;
//		
//		Equipment equipment = new Equipment(uuid.toString(), name, model, (int) cost);
//
//		double expectedCost = 2378.13;
//		double expectedTax = 104.16;
//		
//		double actualCost = equipment.rentCost(25);
//		double actualTax = equipment.rentTax(25);
//
//		assertEquals(expectedCost, actualCost, TOLERANCE);
//		assertEquals(expectedTax, actualTax, TOLERANCE);
//	}
//
//	/**
//	 * Creates an instance of a material and tests if
//	 * its purchasing cost and tax calculations are correct.
//	 * 
//	 * Also checks the material's toString() method.
//	 */
//	@Test
//	public void testMaterial() {
//		UUID uuid = UUID.randomUUID();
//		String name = "Nails";
//		String unit = "Box";
//		double cost = 9.99;
//		
//		Material material = new Material(uuid.toString(), name, unit, cost);
//
//		double actualCost = material.purchaseCost(31);
//		double actualTax = material.purchaseTax(31);
//		
//		double expectedCost = 309.69;
//		double expectedTax = 22.14;
//
//		assertEquals(expectedCost, actualCost, TOLERANCE);
//		assertEquals(expectedTax, actualTax, TOLERANCE);
//		
//		String s = material.toString();
//		
//		assertTrue(s.contains(name));
//		assertTrue(s.contains(unit));
//		assertTrue(s.contains(Double.toString(cost)));
//	}
//
//	/**
//	 * Creates an instance of a contact and tests if
//	 * its toString() method works properly.
//	 */
//	@Test
//	public void testContract() {
//		UUID uuid = UUID.randomUUID();
//		UUID contactUuid = UUID.randomUUID();
//		String name = "Box Office Co.";
//		
//		Contract contract = new Contract(uuid.toString(), name, contactUuid.toString());
//		
//		String s = contract.toString();
//		
//		assertTrue(s.contains(name));
//		assertTrue(s.contains(uuid.toString()));
//		assertTrue(s.contains(contactUuid.toString()));
//	}
}