package com.vgb;

import java.util.UUID;

// A representation of a company in the invoice subsystem. 
public class Company extends Identifiable {
	private UUID uuid;
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
	
	
	public UUID getUuid() {
		return uuid;
	}

	@Override
	public String toString() {
		return "Company [uuid=" + uuid + ", contactUuid=" + contactUuid + "name=" + name + ", address=" + address + "]";
	}
}
