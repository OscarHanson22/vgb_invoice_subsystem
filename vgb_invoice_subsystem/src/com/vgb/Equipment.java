package com.vgb;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

// Represents a piece of equipment in the invoice subsystem. 
public class Equipment extends Item {
	private String modelNumber;
	private double retailPrice;
	
	// Creates and returns an Equipment with the given information. 
	public Equipment(String uuid, String name, String modelNumber, int retailPrice) {
		super(uuid, name);
		this.modelNumber = modelNumber;
		this.retailPrice = (double) retailPrice;
	}
	
	public double purchase_cost() {
		return (double) retailPrice;
	}
	
	public double purchase_tax() {
		double PURCHASE_TAX_RATE = 0.0525;

		return Round.toCents(retailPrice * PURCHASE_TAX_RATE);
	}
	
	public double purchase() {
		double PURCHASE_TAX_RATE = 0.0715;
		
		return (double) retailPrice + Round.toCents(retailPrice * PURCHASE_TAX_RATE);
	}
	
	public double rent_cost(int hours) {
		double RENTAL_CHARGE_RATE = 0.001;
		
		return Round.toCents((double) retailPrice * RENTAL_CHARGE_RATE * hours);
	}
	
	public double rent_tax(int hours) {
		double RENTAL_TAX_RATE = 0.0438;
		
		return Round.toCents(rent_cost(hours) * RENTAL_TAX_RATE);
	}
	
	public double rent(int hours) {
		return rent_cost(hours) + rent_tax(hours);
	}
	
	public double lease_tax(LocalDate startDate, LocalDate endDate) {
		double lease_amount = lease_cost(startDate, endDate);
		
		if (lease_amount < 5000.00) {
			return 0.0;
		} else if (lease_amount < 12500.00) {
			return 500.0;
		} else {
			return 1500.0;
		}
	}
	
	public double lease_cost(LocalDate startDate, LocalDate endDate) {
		double LEASE_MARKUP = 0.5;
		
		int leaseDurationInDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1; // the `endDate` is treated as exclusive
		double leaseDurationInYears = (double) leaseDurationInDays / 365;
		double fiveYearAmortization = leaseDurationInYears / 5.0;
				
		double cost = Round.toCents(fiveYearAmortization * retailPrice * (1.0 + LEASE_MARKUP));
				
		return cost;
	}
	
	public double lease(LocalDate startDate, LocalDate endDate) {
		return lease_cost(startDate, endDate) + lease_tax(startDate, endDate);
	}
	
	@Override
	public String toString() {
		return "Equipment [modelNumber=" + modelNumber + ", retailPrice=" +  String.format("%.2f", retailPrice) + ", " + super.toString() + "]";
	}
}
