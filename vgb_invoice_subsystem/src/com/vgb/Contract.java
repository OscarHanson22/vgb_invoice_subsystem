package com.vgb;

import java.util.UUID;

import com.vgb.financial_handlers.Total;

// Represents a contract with another company. 
public class Contract extends Item {
	private Company subcontractor;
	private double amount;

	// Creates and returns a Contract with the specified information. 
	public Contract(UUID uuid, String name, Company subcontractor) {
		super(uuid, name);
		this.subcontractor = subcontractor;
		this.amount = 0.0;
	}
	
	public Contract(Contract fromContract, double amount) {
		super(fromContract.getUuid(), fromContract.getName());
		this.subcontractor = fromContract.getSubcontractor();
		this.amount = amount;
	}
	
	public Company getSubcontractor() {
		return subcontractor;
	}

	public double getAmount() {
		return amount;
	}

	@Override 
	public Total getTotal() {
		return new Total(amount, 0.0);
	}

	@Override
	public String toString() {
		return "Contract [" + super.toString() + "subcontractor=" + subcontractor.toString() + "]";
	}
}
