package com.vgb;

// A representation of a material in the invoice subsystem. 
public class Material extends Item {
	private static final double PURCHASE_TAX = 0.0715;
	
	private String unit;
	private int costPerUnit;
	
	// Creates and returns a Material from the given information. 
	public Material(String uuid, String name, String unit, int costPerUnit) {
		super(uuid, name);
		this.unit = unit;
		this.costPerUnit = costPerUnit;
	}
	
	public double purchase(int amount) {
		return (double) costPerUnit * amount * PURCHASE_TAX;
	}

	@Override
	public String toString() {
		return "Material [unit=" + unit + ", costPerUnit=" + costPerUnit + ", " + super.toString() + "]";
	}
}
