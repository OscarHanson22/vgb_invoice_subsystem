package com.vgb;

import java.util.UUID;

// A representation of a material in the invoice subsystem. 
public class Material extends Item {	
	private String unit;
	private double costPerUnit;
	
	// Creates and returns a Material from the given information. 
	public Material(UUID uuid, String name, String unit, double costPerUnit) {
		super(uuid, name);
		this.unit = unit;
		this.costPerUnit = costPerUnit;
	}
	
	public double getCostPerUnit() {
		return costPerUnit;
	}
	
	@Override
	public String toString() {
		return "Material [unit=" + unit + ", costPerUnit=" + costPerUnit + ", " + super.toString() + "]";
	}
}
