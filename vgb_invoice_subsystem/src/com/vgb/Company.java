/**
 * Authors: Oscar Hanson and Ermias Wolde
 * Date: 5/9/2025
 * Purpose: Object representation of a company.
 * 
 */

package com.vgb;

import java.util.UUID;

/**
 * A representation of a company in the invoice subsystem. 
 */
public class Company extends Identifiable {
	private Person contact;
	private String name;
	private Address address;
	
	/**
	 * Creates and returns a Company from the given information. 
	 * 
	 * @param uuid
	 * @param contact
	 * @param name
	 * @param address
	 */
	public Company(UUID uuid, Person contact, String name, Address address) {
		super(uuid);
		this.contact = contact;
		this.name = name;
		this.address = address;
	}

	/**
	 * Returns the person to contact to reach this company.
	 * 
	 * @return
	 */
	public Person getContact() {
		return contact;
	}

	/**
	 * Returns the name of the company.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the address the company is located at.
	 * 
	 * @return
	 */
	public Address getAddress() {
		return address;
	}

	@Override
	public String toString() {
		return name + " (" + this.getUuid() + ") | Contact: " + contact + " | Address: " + address;
	}
}
