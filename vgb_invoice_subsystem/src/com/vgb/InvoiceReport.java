package com.vgb;

import java.util.List;

public class InvoiceReport {
	public static void main(String[] args) {
		InvoiceSubsystem invoiceSubsystem = new InvoiceSubsystem(null, null, null, null);
		
		List<Invoice> invoices = invoiceSubsystem.addInvoiceItems(null);
		
		for (Invoice invoice : invoices) {
			System.out.println("Subtotal: $" + invoice.subtotal());
			System.out.println("Tax total: $" + invoice.taxTotal());
			System.out.println("Grand total: $" + invoice.grandTotal());
		}
		
//		The first report will give a summary of all invoices along with a few totals.

//		The second report will give a similar summary but for each customer.

//		The final report will give details for each individual invoice.
	}
}
