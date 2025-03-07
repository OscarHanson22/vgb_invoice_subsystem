package com.vgb;

// Represents the basic information of an item in the invoice subsystem. 
public abstract class Item {
	private String uuid;
	private String name;
	
	// Creates and returns an Item from the specified information. 
	public Item(String uuid, String name) {
		this.uuid = uuid;
		this.name = name;
	}

	@Override
	public String toString() {
		return "uuid=" + uuid + ", name=" + name;
	}
}