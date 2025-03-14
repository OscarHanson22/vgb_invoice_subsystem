package com.vgb;

import java.util.UUID;

// Represents a piece of equipment in the invoice subsystem. 
public class Equipment extends Item {
	private String modelNumber;
	private double retailPrice;
	
	// Creates and returns an Equipment with the given information. 
	public Equipment(UUID uuid, String name, String modelNumber, int retailPrice) {
		super(uuid, name);
		this.modelNumber = modelNumber;
		this.retailPrice = (double) retailPrice;
	}
	
	public double getRetailPrice() {
		return retailPrice;
	}
	
	@Override
	public String toString() {
		return "Equipment [modelNumber=" + modelNumber + ", retailPrice=" +  String.format("%.2f", retailPrice) + ", " + super.toString() + "]";
	}
}


