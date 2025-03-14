package com.vgb;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class EquipmentLease extends Equipment {
	private static final double MARKUP = 0.5;
	private LocalDate startDate;
	private LocalDate endDate;
	
	public EquipmentLease(Equipment fromEquipment, LocalDate startDate, LocalDate endDate) {
		super(fromEquipment.getUuid(), fromEquipment.getName(), fromEquipment.getModelNumber(), fromEquipment.getRetailPrice());
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	@Override
	public Total getTotal() {
		int leaseDurationInDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1; // the `endDate` is treated as exclusive
		double leaseDurationInYears = (double) leaseDurationInDays / 365;
		double fiveYearAmortization = leaseDurationInYears / 5.0;
		double cost = Round.toCents(fiveYearAmortization * this.getRetailPrice() * (1.0 + MARKUP));
		
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
