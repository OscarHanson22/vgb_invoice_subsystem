package com.vgb.database_factories;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import com.vgb.InvoiceData;
import com.vgb.Person;

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
	
	public void PersonShouldBeAddedTest(UUID uuid, String firstName, String lastName, String phoneNumber) {
		Person expectedPerson = new Person(uuid, firstName, lastName, phoneNumber, new ArrayList<>());
		
		// add person info to the database
		InvoiceData.addPerson(uuid, firstName, lastName, phoneNumber);
		
		// ensure person can be found in the database
		Optional<Integer> emptyOptional = Optional.empty();
		Optional<Integer> personId = PersonFactory.getId(connection, uuid);
		assertTrue(personId != emptyOptional);
		
		// load the person from the database
		Person actualPerson = PersonFactory.loadPerson(connection, personId.get());
		
		assertTrue(expectedPerson.getUuid().equals(actualPerson.getUuid()), "The UUIDs of the expected and loaded person do not match.");
		assertTrue(expectedPerson.getFirstName().equals(actualPerson.getFirstName()), "The the first names of the expected and loaded person do not match.");
		assertTrue(expectedPerson.getLastName().equals(actualPerson.getLastName()), "The the last names of the expected and loaded person do not match.");
		assertTrue(expectedPerson.getPhoneNumber().equals(actualPerson.getPhoneNumber()), "The the phone numbers of the expected and loaded person do not match.");
	}
	
	public void PersonShouldNotBeAddedTest(UUID uuid, String firstName, String lastName, String phoneNumber) {		
		// add person info to the database
		InvoiceData.addPerson(uuid, firstName, lastName, phoneNumber);
		
		// ensure person cannot be found in the database
		Optional<Integer> emptyOptional = Optional.empty();
		assertTrue(PersonFactory.getId(connection, uuid) == emptyOptional);
	}
	
//	public void EmailShouldNotBeAddedTest(UUID personUuid, String email) {
//		// add email to the database
//		InvoiceData.addEmail(personUuid, email);
//		
//		// ensure email cannot be found in the database
//		assertTrue(EmailFactory.getIds(connection, personUuid).size() == 0);
//	}

	/**
	 * Adds a person to the database, ensures they can be found, and loads it from the database to ensure data integrity. 
	 */
	@Test
	public void testAddPerson001() {
		UUID uuid = UUID.randomUUID();
		String firstName = "John";
		String lastName = "Doe";
		String phoneNumber = "209-877-9990";
		
		PersonShouldBeAddedTest(uuid, firstName, lastName, phoneNumber);
	}
	
	@Test
	public void testAddPersonNullUuid() {
		UUID uuid = null;
		String firstName = "John";
		String lastName = "Doe";
		String phoneNumber = "209-877-9990";
		
		PersonShouldNotBeAddedTest(uuid, firstName, lastName, phoneNumber);
	}
	
	@Test
	public void testAddPersonNullFirstName() {
		UUID uuid = UUID.randomUUID();
		String firstName = null;
		String lastName = "Doe";
		String phoneNumber = "209-877-9990";
		
		PersonShouldNotBeAddedTest(uuid, firstName, lastName, phoneNumber);
	}
	
	@Test
	public void testAddPersonNullLastName() {
		UUID uuid = UUID.randomUUID();
		String firstName = "John";
		String lastName = null;
		String phoneNumber = "209-877-9990";
		
		PersonShouldNotBeAddedTest(uuid, firstName, lastName, phoneNumber);
	}
	
	@Test
	public void testAddPersonNullPhoneNumber() {
		UUID uuid = UUID.randomUUID();
		String firstName = "John";
		String lastName = "Doe";
		String phoneNumber = null;
		
		PersonShouldNotBeAddedTest(uuid, firstName, lastName, phoneNumber);
	}
}