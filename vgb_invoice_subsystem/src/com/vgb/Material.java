package com.vgb;

// A representation of a material in the invoice subsystem. 
public class Material extends Item {	
	private String unit;
	private double costPerUnit;
	
	// Creates and returns a Material from the given information. 
	public Material(String uuid, String name, String unit, double costPerUnit) {
		super(uuid, name);
		this.unit = unit;
		this.costPerUnit = costPerUnit;
	}
	
	public double purchase_total(int amount) {
		return purchase_cost(amount) + purchase_tax(amount);
	}
	
	public double purchase_cost(int amount) {
		return Round.toCents((double) costPerUnit * amount);
	}
	
	public double purchase_tax(int amount) {
		double PURCHASE_TAX = 0.0715;

		return Round.toCents(purchase_cost(amount) * PURCHASE_TAX);
	}
	
	@Override
	public String toString() {
		return "Material [unit=" + unit + ", costPerUnit=" + costPerUnit + ", " + super.toString() + "]";
	}
}
