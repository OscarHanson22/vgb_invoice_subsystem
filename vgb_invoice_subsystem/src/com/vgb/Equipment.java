package com.vgb;

import java.time.LocalDate;
import java.time.Period;

// Represents a piece of equipment in the invoice subsystem. 
public class Equipment extends Item {
	private String modelNumber;
	private int retailPrice;
	
	// Creates and returns an Equipment with the given information. 
	public Equipment(String uuid, String name, String modelNumber, int retailPrice) {
		super(uuid, name);
		this.modelNumber = modelNumber;
		this.retailPrice = retailPrice;
	}
	
	public double purchase() {
		double PURCHASE_TAX_RATE = 0.0715;
		
		return (double) retailPrice + Round.toCents(retailPrice * PURCHASE_TAX_RATE);
	}
	
	public double rent(int hours) {
		double RENTAL_TAX_RATE = 0.0438;
		double RENTAL_CHARGE_RATE = 0.001;
		
		double roundedPreTaxCost = Round.toCents((double) retailPrice * RENTAL_CHARGE_RATE * hours);
		return roundedPreTaxCost + Round.toCents(roundedPreTaxCost * RENTAL_TAX_RATE);
	}
	
	private static double LEASE_TAX(double lease_amount) {
		if (lease_amount < 5000.00) {
			return 0.0;
		} else if (lease_amount < 12500.00) {
			return 500.0;
		} else {
			return 1500.0;
		}
	}
	
	public double lease(LocalDate start_date, LocalDate end_date) {
		double LEASE_MARKUP = 0.5;
		
		int leaseDurationInDays = Period.between(start_date, end_date).getDays() + 1; // the `end_date` is treated as exclusive
		double leaseDurationInYears = (double) leaseDurationInDays / 365;
		double fiveYearAmortization = leaseDurationInYears / 5.0;
		
		double totalCost = Round.toCents(fiveYearAmortization * retailPrice * (1.0 + LEASE_MARKUP));
		double totalCostWithTax = totalCost + LEASE_TAX(totalCost);
		
		return totalCostWithTax;
	}
	
	@Override
	public String toString() {
		return "Equipment [modelNumber=" + modelNumber + ", retailPrice=" + retailPrice + ", " + super.toString() + "]";
	}
}
