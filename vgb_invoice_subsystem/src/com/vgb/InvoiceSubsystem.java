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
		invoices = Parser.parseInvoices(invoicesFilePath, people);
	}
	
	public List<Invoice> addInvoiceItems(String invoiceItemsFilePath) {
		return Parser.parseInvoiceItems(invoiceItemsFilePath, invoices, items);
	}
}
