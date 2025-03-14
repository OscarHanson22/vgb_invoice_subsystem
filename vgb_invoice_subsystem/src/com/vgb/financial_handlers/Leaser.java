package com.vgb.financial_handlers;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/*
 * A class that handles leasing "something."
 */
public class Leaser {
	private double markup;
	
	// Creates a leaser with a given markup. 
	// A markup value of `0.5` would correspond to a 50% markup.
	public Leaser(double markup) {
		this.markup = markup;
	}
	
	// Returns the raw cost (without tax) of leasing something with the specified information. 
	public double cost(double price, LocalDate startDate, LocalDate endDate) {		
		int leaseDurationInDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1; // the `endDate` is treated as exclusive
		double leaseDurationInYears = (double) leaseDurationInDays / 365;
		double fiveYearAmortization = leaseDurationInYears / 5.0;
				
		return Round.toCents(fiveYearAmortization * price * (1.0 + markup));
	}
	
	// Returns the tax cost of leasing something with the specified information. 
	public double tax(double price, LocalDate startDate, LocalDate endDate) {
		double leaseAmount = cost(price, startDate, endDate);
		
		if (leaseAmount < 5000.00) {
			return 0.0;
		} else if (leaseAmount < 12500.00) {
			return 500.0;
		} else {
			return 1500.0;
		}	
	}
	
	// Returns the total cost of leasing something. 
	public double total(double price, LocalDate startDate, LocalDate endDate) {
		return cost(price, startDate, endDate) + tax(price, startDate, endDate);
	}
}
