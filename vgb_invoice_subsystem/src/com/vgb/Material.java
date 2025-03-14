package com.vgb;

import java.util.UUID;

// A representation of a material in the invoice subsystem. 
public class Material extends Item {	
	public static final double TAX_RATE = 0.0715;
	private String unit;
	private double costPerUnit;
	private int quantity;
	
	// Creates and returns a Material from the given information. 
	public Material(UUID uuid, String name, String unit, double costPerUnit) {
		super(uuid, name);
		this.unit = unit;
		this.costPerUnit = costPerUnit;
		this.quantity = 0;
	}
	
	public Material(Material fromMaterial, int quantity) {
		super(fromMaterial.getUuid(), fromMaterial.getName());
		this.unit = fromMaterial.getUnit();
		this.costPerUnit = fromMaterial.getCostPerUnit();
		this.quantity = quantity;
	}
	
	public String getUnit() {
		return unit;
	}

	public double getCostPerUnit() {
		return costPerUnit;
	}

	public int getQuantity() {
		return quantity;
	}

	@Override
	public Total getTotal() {
		double cost = Round.toCents(costPerUnit * quantity);
		double tax = Round.toCents(cost * TAX_RATE);
		return new Total(cost, tax);
	}

	@Override
	public String toString() {
		return "Material [unit=" + unit + ", costPerUnit=" + costPerUnit + ", " + super.toString() + "]";
	}
}
