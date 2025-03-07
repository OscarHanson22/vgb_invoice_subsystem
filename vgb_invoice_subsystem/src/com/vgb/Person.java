package com.vgb;

import java.util.ArrayList;
import java.util.List;

// A representation of a person in the invoice subsystem. 
public class Person {
	private String uuid;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private ArrayList<String> emailAddresses;

	// Creates and returns a Person from the given information. 
	public Person(String uuid, String firstName, String lastName, String phoneNumber, List<String> emailAddresses) {
		this.uuid = uuid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.emailAddresses = new ArrayList<>(emailAddresses);
	}

	@Override
	public String toString() {
		return "Person [uuid=" + uuid + ", firstName=" + firstName + ", lastName=" + lastName + ", phoneNumber=" + phoneNumber + ", emailAddresses=" + emailAddresses + "]";
	}
}
