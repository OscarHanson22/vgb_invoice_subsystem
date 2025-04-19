package com.vgb;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vgb.database_factories.PersonFactory;

public class InvoiceSubsystem {
    private static final Logger logger = LogManager.getLogger(InvoiceSubsystem.class);

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
	
	private <T extends Identifiable> HashMap<UUID, Identifiable> listToHashMap(List<T> items) {
		HashMap<UUID, Identifiable> hashmap = new HashMap<>();
		
		for (T item : items) {
			hashmap.put(item.getUuid(), item);
		}
		
		return hashmap;
	}
	
//	public InvoiceSubsystem(Connection connection) {
//		people = listToHashMap(PersonFactory.loadAllPeople(connection));
//	}
	
//	public void debug() {
//		System.out.println(people);
//		System.out.println(companies);
//		System.out.println(items);
//		for (Invoice invoice : invoices.values()) {
//			System.out.println(invoice);		
//		}
//	}
		
//	public static void main(String[] args) {		
//		InvoiceSubsystem invoiceSubsystem = new InvoiceSubsystem("data/PersonsTest.csv", "data/CompaniesTest.csv", "data/ItemsTest.csv", "data/InvoicesTest.csv");
//		
//		List<Invoice> invoices = invoiceSubsystem.addInvoiceItems("data/InvoiceItemsTest.csv");
//		
//		
//		for (Invoice invoice : invoices) {
//			System.out.println(invoice.getUuid());
//			System.out.println("Subtotal: $" + invoice.subtotal());
//			System.out.println("Tax total: $" + invoice.taxTotal());
//			System.out.println("Grand total: $" + invoice.grandTotal());
//			System.out.println();
//		}
//		
//		invoiceSubsystem.debug();
//	}
}
