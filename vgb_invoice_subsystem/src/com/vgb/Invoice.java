package com.vgb;

import java.util.ArrayList;
import java.util.UUID;

import com.vgb.financial_handlers.Total;

// A representation of an invoice in the invoicing subsystem.
public class Invoice {
	private UUID uuid;
	private Person customer;
	private Person salesperson;
	private String date;
	private Total total;
	private ArrayList<Item> items;
	
	// Creates a new invoice with the specified information.
	public Invoice(UUID uuid, Person customer, Person salesperson, String date) {
		this.uuid = uuid;
		this.customer = customer;
		this.salesperson = salesperson;
		this.date = date;
		this.total = Total.empty();
		this.items = new ArrayList<>();
		
	}
	
	// Adds an item's total to the invoice. 
	public void addItem(Item item) {
		items.add(item);
		total.add(item.getTotal());
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
		return "Invoice [uuid=" + uuid + ", customer=" + customer + ", salesperson=" + salesperson + ", date=" + date + "]";
	}
}
