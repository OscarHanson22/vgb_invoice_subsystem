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
}
