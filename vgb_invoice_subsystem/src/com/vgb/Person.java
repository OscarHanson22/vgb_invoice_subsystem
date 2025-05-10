/**
 * Authors: Oscar Hanson and Ermias Wolde
 * Date: 5/9/2025
 * Purpose: Object representation of a person in the subsystem. 
 */

package com.vgb;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A representation of a person in the invoice subsystem. 
 */
public class Person extends Identifiable {
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private ArrayList<String> emailAddresses;

	/**
	 * Creates and returns a Person from the given information. 
	 * 
	 * @param uuid
	 * @param firstName
	 * @param lastName
	 * @param phoneNumber
	 * @param emailAddresses
	 */
	public Person(UUID uuid, String firstName, String lastName, String phoneNumber, List<String> emailAddresses) {
		super(uuid);
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.emailAddresses = new ArrayList<>(emailAddresses);
	}

	/**
	 * Returns the first name of the person.
	 * 
	 * @return
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Returns the last name of the person.
	 * 
	 * @return
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Returns the full name of the person.
	 * 
	 * @return
	 */
	public String getFullName() {
		return firstName + " " + lastName;
	}

	/**
	 * Returns the phone number of the person.
	 * 
	 * @return
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * Returns the email addresses of the person.
	 * 
	 * @return
	 */
	public ArrayList<String> getEmailAddresses() {
		return emailAddresses;
	}

	@Override
	public String toString() {
		return firstName + " " + lastName + " (" + this.getUuid() + ") | " + "Phone: " + phoneNumber + ", Email(s): " + emailAddresses;
	}
}
