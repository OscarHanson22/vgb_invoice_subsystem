package com.vgb;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class InvoiceSubsystem {
	private HashMap<UUID, Person> people;
	private HashMap<UUID, Company> companies;
	private HashMap<UUID, Item> items;
	private HashMap<UUID, Invoice> invoices;
		
	public InvoiceSubsystem(List<Person> people, List<Company> companies, List<Item> items, List<Invoice> invoices) {
		this.people = new HashMap<>();
		for (Person person : people) {
			this.people.put(person.getUuid(), person);
		}
		
		this.companies = new HashMap<>();
		for (Company company : companies) {
			this.companies.put(company.getUuid(), company);
		}
		
		this.items = new HashMap<>();
		for (Item item : items) {
			this.items.put(item.getUuid(), item);
		}
		
		this.invoices = new HashMap<>();
		for (Invoice invoice : invoices) {
			this.invoices.put(invoice.getUuid(), invoice);
		}
	}
	
	
}
