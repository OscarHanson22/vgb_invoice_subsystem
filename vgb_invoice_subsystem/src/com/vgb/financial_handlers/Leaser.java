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

	// Returns a total (which includes raw cost and tax amounts) given the price and amount of hours of renting something.
	public Total total(double price, LocalDate startDate, LocalDate endDate) {
		int leaseDurationInDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1; // the `endDate` is treated as exclusive
		double leaseDurationInYears = (double) leaseDurationInDays / 365;
		double fiveYearAmortization = leaseDurationInYears / 5.0;
		double cost = Round.toCents(fiveYearAmortization * price * (1.0 + markup));
		
		double tax = 0.0;
		if (cost < 5000.00) {
			tax = 0.0;
		} else if (cost < 12500.00) {
			tax = 500.0;
		} else {
			tax = 1500.0;
		}	
		
		return new Total(cost, tax);
	}
}
