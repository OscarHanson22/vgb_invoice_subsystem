package com.vgb;

import java.util.UUID;

// Represents the basic information of an item in the invoice subsystem. 
public abstract class Item {
	private UUID uuid;
	private String name;
	
	// Creates and returns an Item from the specified information. 
	public Item(UUID uuid, String name) {
		this.uuid = uuid;
		this.name = name;
	}
	
	public UUID getUuid() {
		return uuid;
	}

	@Override
	public String toString() {
		return "uuid=" + uuid + ", name=" + name;
	}
}