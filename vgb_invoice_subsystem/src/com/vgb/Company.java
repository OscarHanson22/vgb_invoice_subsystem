package com.vgb;

import java.util.UUID;

// A representation of a company in the invoice subsystem. 
public class Company {
	private UUID uuid;
	private String contactUuid;
	private String name;
	private Address address;
	
	// Creates and returns a Company from the given information. 
	public Company(UUID uuid, String contactUuid, String name, Address address) {
		this.uuid = uuid;
		this.contactUuid = contactUuid;
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
