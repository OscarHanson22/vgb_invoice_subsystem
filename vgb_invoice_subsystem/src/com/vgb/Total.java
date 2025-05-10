/**
 * Authors: Oscar Hanson and Ermias Wolde
 * Date: 5/9/2025
 * Purpose: Object that holds the cost, tax, and total of something that can be assigned a price.
 */

package com.vgb;

public class Total {
	private double cost;
	private double tax;
	
	public Total(double cost, double tax) {
		this.cost = cost;
		this.tax = tax;
	}
	
	public static Total empty() {
		return new Total(0.0, 0.0);
	}
	
	public void add(Total other) {
		this.cost += other.cost;
		this.tax += other.tax;
	}

	public double getCost() {
		return cost;
	}

	public double getTax() {
		return tax;
	}
	
	public double getTotal() {
		return cost + tax;
	}
	
	@Override
	public String toString() {
		return "Cost: $" + String.format("%.2f", cost) + ", Tax: $" + String.format("%.2f", tax) + ", Total: $" + String.format("%.2f", getTotal());
	}
}
