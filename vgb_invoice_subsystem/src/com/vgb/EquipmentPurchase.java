package com.vgb;

public class EquipmentPurchase extends Equipment {
	private static final double TAX_RATE = 0.0525;
	
	public EquipmentPurchase(Equipment fromEquipment) {
		super(fromEquipment.getUuid(), fromEquipment.getName(), fromEquipment.getModelNumber(), fromEquipment.getRetailPrice());
	}
	
	@Override
	public Total getTotal() {
		double cost = Round.toCents(this.getRetailPrice());
		double tax = Round.toCents(cost * TAX_RATE);
		return new Total(cost, tax);
	}
}
