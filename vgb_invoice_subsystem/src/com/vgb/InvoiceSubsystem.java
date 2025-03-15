package com.vgb;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class InvoiceSubsystem {
	private HashMap<UUID, Person> people;
	private HashMap<UUID, Company> companies;
	private HashMap<UUID, Item> items;
	private HashMap<UUID, Invoice> invoices;
		
	public InvoiceSubsystem(String peopleFilePath, String companiesFilePath, String itemsFilePath, String invoicesFilePath) {
		people = Parser.parsePeople(peopleFilePath);
		companies = Parser.parseCompanies(companiesFilePath, people);
		items = Parser.parseItems(itemsFilePath, companies);
		invoices = Parser.parseInvoices(invoicesFilePath, people, companies);
	}
	
	public List<Invoice> addInvoiceItems(String invoiceItemsFilePath) {
		List<Invoice> updated_invoices = Parser.parseInvoiceItems(invoiceItemsFilePath, invoices, items);
		updated_invoices.sort((i1, i2) -> ((Double) i2.getTotal().getTotal()).compareTo(i1.getTotal().getTotal()));
		return updated_invoices;
	}
	
//	public void debug() {
//		System.out.println(people);
//		System.out.println(companies);
//		System.out.println(items);
//		for (Invoice invoice : invoices.values()) {
//			System.out.println(invoice);		
//		}
//	}
		
	public static void main(String[] args) {		
		InvoiceSubsystem invoiceSubsystem = new InvoiceSubsystem("data/PersonsTest.csv", "data/CompaniesTest.csv", "data/ItemsTest.csv", "data/InvoicesTest.csv");
		
		List<Invoice> invoices = invoiceSubsystem.addInvoiceItems("data/InvoiceItemsTest.csv");
		
		
		for (Invoice invoice : invoices) {
			System.out.println(invoice.getUuid());
			System.out.println("Subtotal: $" + invoice.subtotal());
			System.out.println("Tax total: $" + invoice.taxTotal());
//			System.out.println("Grand total: $" + invoice.grandTotal());
			System.out.println();
		}
		
//		invoiceSubsystem.debug();
	}
}
