package com.vgb.financial_handlers;

/*
 * A class that handles the renting of "something."
 */
public class Renter {
	private double taxRate;
	private double chargeRate;
	
	// Creates a renter with a given tax rate and charge rate.
	public Renter(double tax_rate, double charge_rate) {
		this.taxRate = tax_rate;
		this.chargeRate = charge_rate;
	}
	
	// Returns the raw cost (without tax) of renting something with the specified information. 
	public double cost(double price, int hours) {
		return Round.toCents(price * chargeRate * hours);
	}

	// Returns the tax cost of renting something with the specified information. 
	public double tax(double price, int hours) {
		return Round.toCents(cost(price, hours) * taxRate);
	}
	
	// Returns the total cost of renting something with the specified information. 
	public double total(double price, int hours) {
		return cost(price, hours) + tax(price, hours);
	}
}
