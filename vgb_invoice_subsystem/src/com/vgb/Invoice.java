package com.vgb;

import java.util.UUID;

// A representation of an invoice in the invoicing subsystem.
public class Invoice {
	private UUID uuid;
	private UUID customerUuid;
	private UUID salespersonUuid;
	private String date;
	private double taxTotal;
	private double subtotal;
	
	// Creates a new invoice with the specified information.
	public Invoice(UUID uuid, UUID customerUuid, UUID salespersonUuid, String date) {
		this.uuid = uuid;
		this.customerUuid = customerUuid;
		this.salespersonUuid = salespersonUuid;
		this.date = date;
		this.taxTotal = 0.0;
		this.subtotal = 0.0;
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	// Adds an item to the invoice. 
	public void addItem(double total, double tax) {
		subtotal += total;
		taxTotal += tax;
	}
	
	// Returns the subtotal of the invoice. 
	public double subtotal() {
		return subtotal;
	}
	
	// Returns the total amount of tax of the invoice. 
	public double taxTotal() {
		return taxTotal;
	}
	
	// Returns the grand total (subtotal and total tax) of the invoice. 
	public double grandTotal() {
		return subtotal + taxTotal;
	}

	@Override
	public String toString() {
		return "Invoice [uuid=" + uuid + ", customerUuid=" + customerUuid + ", salespersonUuid=" + salespersonUuid + ", date=" + date + "]";
	}
}
