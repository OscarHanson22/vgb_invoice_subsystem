/**
 * Authors: Oscar Hanson and Ermias Wolde
 * Date: 5/9/2025
 * Purpose: Object that represents an invoice in the subsystem.
 */

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
	
	// Returns the Total (subtotal and tax) represented by this invoice. 
	public Total getTotal() {
		return total;
	}
	
	// Returns the value of the total (used for sorting).
	public double getTotalValue() {
		return total.getTotal();
	}
	
	public static String summaryHeader() {
		return String.format("%-36s | %-30s | %-20s\n", "UUID", "Customer", "Total");
	}
	
	/**
	 * Returns a summary of the invoice.
	 */
	public String summary() {
		return String.format("%-36s | %-30s | $%-20.2f\n", getUuid(), customer.getName(), getTotalValue());
	}

	@Override
	public String toString() {
		StringBuilder invoiceSB = new StringBuilder();
		Total total = Total.empty();
		
		invoiceSB.append(
			"Invoice UUID: " + this.getUuid() + " | Date: " + date + 
			"\nCustomer: " + customer + 
			"\nSalesperson: " + salesperson
		);
		
		if (items.size() > 0) {
			invoiceSB.append("\nItems (" + items.size() + "):\n");
			
			for (Item item : items) {
				invoiceSB.append(" - " + item + "\n");
				total.add(item.getTotal());
			}
		} else {
			invoiceSB.append("\nNo items on invoice.\n");
		}
		
		invoiceSB.append(total + "\n");
		
		return invoiceSB.toString();
	}
}
