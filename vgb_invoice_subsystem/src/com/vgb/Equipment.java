package com.vgb;

import java.util.UUID;

// Represents a piece of equipment in the invoice subsystem. 
public class Equipment extends Item {
	private static final double TAX_RATE = 0.0525;
	private String modelNumber;
	private double retailPrice;
	
	// Creates and returns an Equipment with the given information. 
	public Equipment(UUID uuid, String name, String modelNumber, double retailPrice) {
		super(uuid, name);
		this.modelNumber = modelNumber;
		this.retailPrice = retailPrice;
	}
	
	public String getModelNumber() {
		return modelNumber;
	}
	
	public double getRetailPrice() {
		return retailPrice;
	}
	
	@Override
	public Total getTotal() {
		double cost = Round.toCents(retailPrice);
		double tax = Round.toCents(cost * TAX_RATE);
		return new Total(cost, tax);
	}
	
	@Override
	public String toString() {
		return "Equipment [modelNumber=" + modelNumber + ", retailPrice=" +  String.format("%.2f", retailPrice) + ", " + super.toString() + "]";
	}
}


