package com.vgb;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// A representation of an invoice in the invoicing subsystem.
public class Invoice extends Identifiable {
	private Company customer;
	private Person salesperson;
	private LocalDate date;
	private Total total;
	private ArrayList<Item> items;
	
	// Creates a new invoice with the specified information.
	public Invoice(UUID uuid, Company customer, Person salesperson, LocalDate date) {
		super(uuid);
		this.customer = customer;
		this.salesperson = salesperson;
		this.date = date;
		this.total = Total.empty();
		this.items = new ArrayList<>();
	}
	
	public Company getCustomer() {
		return customer;
	}

	public Person getSalesperson() {
		return salesperson;
	}

	public LocalDate getDate() {
		return date;
	}

	public List<Item> getItems() {
		return items;
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
	
	public Total getTotal() {
		return total;
	}

	@Override
	public String toString() {
		StringBuilder invoiceSB = new StringBuilder();
		invoiceSB.append("Invoice UUID: " + this.getUuid() + " Date: " + date + "\nCustomer: " + customer + "\nSalesperson: " + salesperson + "\nItems (" + items.size() + "):\n");
		for (Item item : items) {
			invoiceSB.append(" - " + item + "\n");
		}
		return invoiceSB.toString();
	}
}
