package com.vgb.financial_handlers;

/*
 * A class that handles purchasing "something."
 */
public class EquipmentPurchaser {
	private double taxRate;
	
	// Creates a purchaser with a given tax rate. 
	public Purchaser(double taxRate) {
		this.taxRate = taxRate;
	}

	// Returns a total (which includes raw cost and tax amounts) given the price and amount of something to be purchased.
	public Total total(double price, int amount) {
		double cost = Round.toCents(price * amount);
		double tax = Round.toCents(cost * taxRate);
		return new Total(cost, tax);
	}
}
