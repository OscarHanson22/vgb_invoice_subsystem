package com.vgb;

import java.util.UUID;

import com.vgb.financial_handlers.Total;

// A representation of an invoice in the invoicing subsystem.
public class Invoice {
	private UUID uuid;
	private UUID customerUuid;
	private UUID salespersonUuid;
	private String date;
	private Total total;
	
	// Creates a new invoice with the specified information.
	public Invoice(UUID uuid, UUID customerUuid, UUID salespersonUuid, String date) {
		this.uuid = uuid;
		this.customerUuid = customerUuid;
		this.salespersonUuid = salespersonUuid;
		this.date = date;
		this.total = Total.empty();
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	// Adds an item's total to the invoice. 
	public void addItem(Total total) {
		this.total.add(total);
	}
	
	// Returns the subtotal of the invoice. 
	public double subtotal() {
		return total.getCost();
	}
	
	// Returns the total amount of tax of the invoice. 
	public double taxTotal() {
		return total.getTax();
	}
	
	// Returns the grand total (subtotal and total tax) of the invoice. 
	public double grandTotal() {
		return total.getTotal();
	}

	@Override
	public String toString() {
		return "Invoice [uuid=" + uuid + ", customerUuid=" + customerUuid + ", salespersonUuid=" + salespersonUuid + ", date=" + date + "]";
	}
}
