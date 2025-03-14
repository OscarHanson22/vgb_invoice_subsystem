package com.vgb;

import java.util.UUID;

import com.vgb.financial_handlers.Total;

// Represents the basic information of an item in the invoice subsystem. 
public abstract class Item extends Identifiable {
	private String name;
	
	// Creates and returns an Item from the specified information. 
	public Item(UUID uuid, String name) {
		super(uuid);
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract Total getTotal();

	@Override
	public String toString() {
		return "uuid=" + this.getUuid() + ", name=" + name;
	}
}