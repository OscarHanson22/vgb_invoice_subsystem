package com.vgb.financial_handlers;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import com.vgb.Equipment;

/*
 * A class that handles leasing "something."
 */
public class EquipmentLeaser extends Equipment {
	private static final double MARKUP = 0.5;
	
	// Creates a leaser. 
	public EquipmentLeaser(Equipment equipment) {
		super(equipment.getUuid(), equipment.getName(), equipment.getModelNumber(), equipment.getRetailPrice());
	}

	// Returns a total (which includes raw cost and tax amounts) given the price and amount of hours of renting something.
	public Total total(double price, LocalDate startDate, LocalDate endDate) {
		int leaseDurationInDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1; // the `endDate` is treated as exclusive
		double leaseDurationInYears = (double) leaseDurationInDays / 365;
		double fiveYearAmortization = leaseDurationInYears / 5.0;
		double cost = Round.toCents(fiveYearAmortization * price * (1.0 + MARKUP));
		
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
