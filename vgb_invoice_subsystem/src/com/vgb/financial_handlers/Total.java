package com.vgb.financial_handlers;

public class Total {
	private double cost;
	private double tax;
	
	public Total(double cost, double tax) {
		this.cost = cost;
		this.tax = tax;
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
