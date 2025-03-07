package com.vgb;

// A representation of a material in the invoice subsystem. 
public class Material extends Item {	
	private String unit;
	private int costPerUnit;
	
	// Creates and returns a Material from the given information. 
	public Material(String uuid, String name, String unit, int costPerUnit) {
		super(uuid, name);
		this.unit = unit;
		this.costPerUnit = costPerUnit;
	}
	
	public double purchase(int amount) {
		double PURCHASE_TAX = 0.0715;

		double roundedPreTaxCost = Round.toCents((double) costPerUnit * amount);
		return roundedPreTaxCost + Round.toCents(roundedPreTaxCost * PURCHASE_TAX);
	}

	@Override
	public String toString() {
		return "Material [unit=" + unit + ", costPerUnit=" + costPerUnit + ", " + super.toString() + "]";
	}
}
