package com.vgb;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

// A file-system interface that provides methods for writing to and from files. 
public abstract class Parser {
	// Reads the contents of `fileName` into a string and returns it. 
	public static String readFileToString(String fileName) {
		String data = "";

		try {
			data = new String(Files.readAllBytes(Paths.get(fileName)));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return data;
	}
	
	// Splits a string up into lines (\n, \n\r, \r\n, \r etc.)
	public static String[] linesOf(String string) {
		return string.split("\\R+"); 
	}
	
	// Writes `string` to the file at `fileName`.
	public static void writeStringToFile(String string, String fileName) {
		try {
			Files.write(Paths.get(fileName), string.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	// Parses a list of Person objects from a given file `fromFile`. 
	// `fromFile` is expected to be a .csv file that is properly formatted.
	public static HashMap<UUID, Person> parsePeople(String fromFile) {
		String personsCsvString = readFileToString(fromFile);
						
		HashMap<UUID, Person> persons = new HashMap<>(); 
		
		for (String line : linesOf(personsCsvString)) {
			String[] splitLine = line.split(",");
			
			UUID uuid = UUID.fromString(splitLine[0]);
			String firstName = splitLine[1];
			String lastName = splitLine[2];
			String phoneNumber = splitLine[3];
			List<String> emailAddresses = Arrays.asList(Arrays.copyOfRange(splitLine, 4, splitLine.length));

			Person person = new Person(uuid, firstName, lastName, phoneNumber, emailAddresses);
						
			persons.put(uuid, person);
		}
		
		return persons;
	}
	
	// Parses a list of Company objects from a given file `fromFile`. 
	// `fromFile` is expected to be a .csv file that is properly formatted.
	public static HashMap<UUID, Company> parseCompanies(String fromFile, HashMap<UUID, Person> withPeople) {
		String companiesCsvString = readFileToString(fromFile);
						
		HashMap<UUID, Company> companies = new HashMap<>(); 
		
		for (String line : linesOf(companiesCsvString)) {
			String[] splitLine = line.split(",");
						
			UUID uuid = UUID.fromString(splitLine[0]);
			UUID contactUuid = UUID.fromString(splitLine[1]);
			String name = splitLine[2];
			String street = splitLine[3];
			String city = splitLine[4];
			String state = splitLine[5];
			String zip = splitLine[6];
			
			Person contact = withPeople.get(contactUuid);
			if (contact == null) {
				System.err.println("Contact with UUID: \"" + contact + "\" not found in `withPeople`.");
				System.exit(1);
			}
			
			Address companyAddress = new Address(street, city, state, zip);
			Company company = new Company(uuid, contact, name, companyAddress);
						
			companies.put(uuid, company);
		}
		
		return companies;
	}
	
	// Parses a list of Item objects from a given file `fromFile`. 
	// `fromFile` is expected to be a .csv file that is properly formatted.
	public static HashMap<UUID, Item> parseItems(String fromFile, HashMap<UUID, Company> withCompanies) {
		String itemsCsvString = readFileToString(fromFile);
						
		HashMap<UUID, Item> items = new HashMap<>(); 
		
		for (String line : linesOf(itemsCsvString)) {
			String[] splitLine = line.split(",");
			
			UUID uuid = UUID.fromString(splitLine[0]);
			String type = splitLine[1];
			String name = splitLine[2];
			
			Item item = null;
			
			if (type.equals("M")) {
				String unit = splitLine[3];
				int costPerUnit = Integer.parseInt(splitLine[4]);
				item = new Material(uuid, name, unit, costPerUnit);
			} else if (type.equals("E")) {
				String modelNumber = splitLine[3];
				int retailPrice = Integer.parseInt(splitLine[4]);
				item = new Equipment(uuid, name, modelNumber, retailPrice);
			} else if (type.equals("C")) {
				UUID subcontractorUuid = UUID.fromString(splitLine[0]);
				Company subcontractor = withCompanies.get(subcontractorUuid);
				if (subcontractor == null) {
					System.err.println("Subcontractor with UUID: \"" + subcontractorUuid + "\" not found in `withCompanies`.");
					System.exit(1);
				}
				item = new Contract(uuid, name, subcontractor);
			} else {
				System.err.println("Error: Unknown item type \"" + type + "\" found in \"data/Items.csv\".");
				System.exit(1);
			}
						
			items.put(uuid, item);
		}
		
		return items;
	}
	
	public static HashMap<UUID, Invoice> parseInvoices(String fromFile, HashMap<UUID, Person> withPeople) {
		String InvoicesCsvString = readFileToString(fromFile);
				
		HashMap<UUID, Invoice> invoices = new HashMap<>(); 
		
		for (String line : linesOf(InvoicesCsvString)) {
			String[] splitLine = line.split(",");
						
			UUID uuid = UUID.fromString(splitLine[0]);
			UUID customerUuid = UUID.fromString(splitLine[1]);
			UUID salespersonUuid = UUID.fromString(splitLine[2]);
			String date = splitLine[3];
			
			Person customer = withPeople.get(customerUuid);
			if (customer == null) {
				System.err.println("Customer with UUID: \"" + customerUuid + "\" not found in `withPeople`.");
				System.exit(1);
			}
			
			Person salesperson = withPeople.get(salespersonUuid);
			if (salesperson == null) {
				System.err.println("Salesperson with UUID: \"" + salespersonUuid + "\" not found in `withPeople`.");
				System.exit(1);
			}

			Invoice invoice = new Invoice(uuid, customer, salesperson, date);
						
			invoices.put(uuid, invoice);
		}
		
		return invoices;
	}
	
	public static List<Invoice> parseInvoiceItems(String fromFile, HashMap<UUID, Invoice> addToInvoices, HashMap<UUID, Item> withItems) {
		String invoiceItemsCsvString = readFileToString(fromFile);
						
		for (String line : linesOf(invoiceItemsCsvString)) {
			String[] splitLine = line.split(",");
						
			UUID invoiceUuid = UUID.fromString(splitLine[0]);
			UUID itemUuid = UUID.fromString(splitLine[1]);
						
			Item item = withItems.get(itemUuid);
						
			if (item.getClass() == Equipment.class) {
				Equipment equipment = (Equipment) item;
				String parseIdentifier = splitLine[2];
				
				switch (parseIdentifier) {
					case "P":
						item = equipment;
						break;
					case "R":
						int hours = Integer.parseInt(splitLine[3]);
						item = new EquipmentRental(equipment, hours);
						break;
					case "L":
						LocalDate startDate = LocalDate.parse(splitLine[3]);
						LocalDate endDate = LocalDate.parse(splitLine[3]);
						item = new EquipmentLease(equipment, startDate, endDate);
						break;
					default: 
						System.err.println("Found unknown parse identifier: \"" + parseIdentifier + "\".");
						System.exit(1);
				}
			} else if (item.getClass() == Material.class) {
				Material material = (Material) item;
				int amount = Integer.parseInt(splitLine[2]);
				item = new Material(material, amount);				
			} else if (item.getClass() == Contract.class) {
				Contract contract = (Contract) item;
				int contractAmount = Integer.parseInt(splitLine[2]);
				item = new Contract(contract, contractAmount);
			}
			
			Invoice invoice = addToInvoices.get(invoiceUuid);
			invoice.addItem(item);
		}
		
		
		ArrayList<Invoice> invoices = new ArrayList<>(); 

		for (Invoice invoice : addToInvoices.values()) {
			invoices.add(invoice);
		}
		
		return invoices;
	}
	
//	public static void main(String[] args) {
//		
//	}
}
