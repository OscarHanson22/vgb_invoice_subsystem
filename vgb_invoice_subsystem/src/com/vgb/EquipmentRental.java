package com.vgb;

public class EquipmentRental extends Equipment {
	private static final double CHARGE_RATE = 0.001;
	private static final double TAX_RATE = 0.0438;

	private int hours;
	
	public EquipmentRental(Equipment fromEquipment, int hours) {
		super(fromEquipment.getUuid(), fromEquipment.getName(), fromEquipment.getModelNumber(), fromEquipment.getRetailPrice());
		this.hours = hours;
	}
	
	@Override
	public Total getTotal() {
		double cost = Round.toCents(this.getRetailPrice() * CHARGE_RATE * hours);
		double tax = Round.toCents(cost * TAX_RATE);	
		return new Total(cost, tax);
	}
}
