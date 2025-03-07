package com.vgb;

// A representation of a company in the invoice subsystem. 
public class Company {
	private String uuid;
	private String contactUuid;
	private String name;
	private Address address;
	
	// Creates and returns a Company from the given information. 
	public Company(String uuid, String contactUuid, String name, Address address) {
		this.uuid = uuid;
		this.contactUuid = contactUuid;
		this.name = name;
		this.address = address;
	}

	@Override
	public String toString() {
		return "Company [uuid=" + uuid + ", contactUuid=" + contactUuid + "name=" + name + ", address=" + address + "]";
	}
}
