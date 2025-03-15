package com.vgb;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// A representation of a person in the invoice subsystem. 
public class Person extends Identifiable {
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private ArrayList<String> emailAddresses;

	// Creates and returns a Person from the given information. 
	public Person(UUID uuid, String firstName, String lastName, String phoneNumber, List<String> emailAddresses) {
		super(uuid);
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.emailAddresses = new ArrayList<>(emailAddresses);
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public ArrayList<String> getEmailAddresses() {
		return emailAddresses;
	}

	@Override
	public String toString() {
		return firstName + " " + lastName + " (" + this.getUuid() + ") | " + "Phone: " + phoneNumber + ", Email(s): " + emailAddresses;
	}
}
