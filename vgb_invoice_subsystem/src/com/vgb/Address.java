/**
 * Authors: Oscar Hanson and Ermias Wolde
 * Date: 5/9/2025
 * Purpose: Object representation of an address.
 * 
 */

package com.vgb;

/**
 * A representation of an address. 
 */
public class Address {
	private String street;
	private String city;
	private String state;
	private String zip;
	
	/**
	 * Creates and returns an Address from the given information. 
	 * 
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 */
	public Address(String street, String city, String state, String zip) {
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}

	@Override
	public String toString() {
		return street + ", " + city + ", " + state + ", " + zip;
	}
}
