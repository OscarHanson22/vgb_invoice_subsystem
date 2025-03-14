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
	}
}
