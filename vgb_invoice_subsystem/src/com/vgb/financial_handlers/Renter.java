package com.vgb.financial_handlers;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
	
	// Returns a total (which includes raw cost and tax amounts) given the price and amount of hours of renting something.
	public Total total(double price, int hours) {
		double cost = Round.toCents(price * chargeRate * hours);
		double tax = Round.toCents(cost * taxRate);	
		return new Total(cost, tax);
	}
}
