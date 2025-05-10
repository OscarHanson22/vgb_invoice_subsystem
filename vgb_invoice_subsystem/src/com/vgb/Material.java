/**
 * Authors: Oscar Hanson and Ermias Wolde
 * Date: 5/9/2025
 * Purpose: Object representation of a material in the subsystem. 
 */

package com.vgb;

import java.util.UUID;

/**
 * A representation of a material in the invoice subsystem. 
 */
public class Material extends Item {	
	public static final double TAX_RATE = 0.0715;
	private String unit;
	private double costPerUnit;
	private int quantity;
	
	/**
	 * Creates and returns a Material with the given information. 
	 * 
	 * @param uuid
	 * @param name
	 * @param unit
	 * @param costPerUnit
	 */
	public Material(UUID uuid, String name, String unit, double costPerUnit) {
		super(uuid, name);
		this.unit = unit;
		this.costPerUnit = costPerUnit;
		this.quantity = 0;
	}
	
	/**
	 * Copy constructor that gives the material a quantity.
	 * 
	 * @param fromMaterial
	 * @param quantity
	 */
	public Material(Material fromMaterial, int quantity) {
		super(fromMaterial.getUuid(), fromMaterial.getName());
		this.unit = fromMaterial.getUnit();
		this.costPerUnit = fromMaterial.getCostPerUnit();
		this.quantity = quantity;
	}
	
	/**
	 * Returns the unit string of the material.
	 * @return
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * Returns the cost per unit of the material.
	 * 
	 * @return
	 */
	public double getCostPerUnit() {
		return costPerUnit;
	}

	/**
	 * Returns the quantity of the material.
	 * 
	 * @return
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * Implementation of the Item class' getTotal, returning the total of the material.
	 */
	@Override
	public Total getTotal() {
		double cost = Round.toCents(costPerUnit * quantity);
		double tax = Round.toCents(cost * TAX_RATE);
		return new Total(cost, tax);
	}

	@Override
	public String toString() {
		return super.toString() + " | Unit: " + unit + ", Cost Per Unit: $" + String.format("%.2f", costPerUnit) + ", Quantity: " + quantity;
	}
}
