package com.vgb;

// A representation of an address. 
public class Address {
	private String street;
	private String city;
	private String state;
	private String zip;
	
	// Creates and returns an Address from the given information. 
	public Address(String street, String city, String state, String zip) {
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}

	@Override
	public String toString() {
		return "Address [street=" + street + ", city=" + city + ", state=" + state + ", zip=" + zip + "]";
	}
}
