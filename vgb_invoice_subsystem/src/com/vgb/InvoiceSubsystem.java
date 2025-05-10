/**
 * Authors: Oscar Hanson and Ermias Wolde
 * Date: 5/9/2025
 * Purpose: Class that gathers together all of the subsystem elements to assist in reporting.
 * Note: Only for reading from files, no database interface.
 */

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
	
//	private <T extends Identifiable> HashMap<UUID, Identifiable> listToHashMap(List<T> items) {
//		HashMap<UUID, Identifiable> hashmap = new HashMap<>();
//		
//		for (T item : items) {
//			hashmap.put(item.getUuid(), item);
//		}
//		
//		return hashmap;
//	}
}
