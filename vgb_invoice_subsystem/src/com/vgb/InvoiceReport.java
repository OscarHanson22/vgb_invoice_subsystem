package com.vgb;

import java.util.List;

public class InvoiceReport {
	public static void main(String[] args) {
		InvoiceSubsystem invoiceSubsystem = new InvoiceSubsystem("data/PersonsTest.csv", "data/CompaniesTest.csv", "data/ItemsTest.csv", "data/InvoicesTest.csv");
		List<Invoice> invoices = invoiceSubsystem.addInvoiceItems("data/InvoiceItemsTest.csv");
		
//		for (Invoice invoice : invoices) {
//			System.out.println("Subtotal: $" + invoice.subtotal());
//			System.out.println("Tax total: $" + invoice.taxTotal());
//			System.out.println("Grand total: $" + invoice.grandTotal());
//		}
//		
		invoicesSummary(invoices);
		
		
//		The first report will give a summary of all invoices along with a few totals.

//		The second report will give a similar summary but for each customer.

//		The final report will give details for each individual invoice.
	}
	
	public static void invoicesSummary(List<Invoice> invoices) {
		Total total = Total.empty();
		
		for (Invoice invoice : invoices) {
			System.out.println("Invoice UUID: " + invoice.getUuid());
			System.out.println("Customer: " + invoice.getCustomer().getFirstName() + " " + invoice.getCustomer().getLastName());
			System.out.println("Salesperson: " + invoice.getSalesperson().getFirstName() + " " + invoice.getSalesperson().getLastName());
			System.out.println("Number of items: " + invoice.getItems().size());
			System.out.println();
			total.add(invoice.getTotal());
		}
		
		System.out.println("Subtotal: $" + total.getCost());
		System.out.println("Tax Total: $" + Round.toCents(total.getTax()));
		System.out.println("Grand Total: $" + total.getTotal());

	}
	
	public void customerSummary(List<Invoice> invoices) {
		
	}
	
	public void invoiceDetailedSummary(List<Invoice> invoices) {
		
	}
}
