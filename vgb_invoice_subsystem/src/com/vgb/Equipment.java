package com.vgb;

import java.time.LocalDate;

// Represents a piece of equipment in the invoice subsystem. 
public class Equipment extends Item {
	private static final double PURCHASE_TAX_RATE = 0.0715; 
	private static final double RENTAL_TAX_RATE = 0.0438;
	private static final double RENTAL_CHARGE_RATE = 0.001;
	
	private static double LEASE_TAXES(double lease_amount) {
		if (lease_amount < 5000.00) {
			return 0.0;
		} else if (lease_amount < 12500.00) {
			return 500.0;
		} else {
			return 1500.0;
		}
	}
	
	private String modelNumber;
	private int retailPrice;
	
	// Creates and returns an Equipment with the given information. 
	public Equipment(String uuid, String name, String modelNumber, int retailPrice) {
		super(uuid, name);
		this.modelNumber = modelNumber;
		this.retailPrice = retailPrice;
	}
	
	public double purchase() {
		return (double) retailPrice + Round.toCents(retailPrice * PURCHASE_TAX_RATE);
	}
	
	public double rent(int hours) {
		double roundedPreTaxCost = Round.toCents((double) retailPrice * RENTAL_CHARGE_RATE * hours);
		return roundedPreTaxCost + Round.toCents(roundedPreTaxCost * RENTAL_TAX_RATE);
	}
	
	public double lease(LocalDate start_date, LocalDate end_date) {
		return 0.0;
	}

	@Override
	public String toString() {
		return "Equipment [modelNumber=" + modelNumber + ", retailPrice=" + retailPrice + ", " + super.toString() + "]";
	}
}
