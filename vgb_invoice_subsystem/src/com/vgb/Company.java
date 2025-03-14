package com.vgb;

import java.util.UUID;

// A representation of a company in the invoice subsystem. 
public class Company extends Identifiable {
	private Person contact;
	private String name;
	private Address address;
	
	// Creates and returns a Company from the given information. 
	public Company(UUID uuid, Person contact, String name, Address address) {
		super(uuid);
		this.contact = contact;
		this.name = name;
		this.address = address;
	}

	public Person getContact() {
		return contact;
	}

	public String getName() {
		return name;
	}

	public Address getAddress() {
		return address;
	}

	@Override
	public String toString() {
		return "Company [uuid=" + this.getUuid() + ", contact=" + contact.toString() + "name=" + name + ", address=" + address + "]";
	}
}
