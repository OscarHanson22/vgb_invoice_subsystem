package com.vgb;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	public static List<Person> parsePeople(String fromFile) {
		String personsCsvString = readFileToString(fromFile);
				
		String[] lines = personsCsvString.split("\\R+"); // splits on new-line combinations (\n, \n\r, \r\n, \r etc.)
		
		ArrayList<Person> persons = new ArrayList<>(); 
		
		for (int i = 1; i < lines.length; i++) {
			String[] splitLine = lines[i].split(",");
			
			String uuid = splitLine[0];
			String firstName = splitLine[1];
			String lastName = splitLine[2];
			String phoneNumber = splitLine[3];
			List<String> emailAddresses = Arrays.asList(Arrays.copyOfRange(splitLine, 4, splitLine.length));

			Person person = new Person(uuid, firstName, lastName, phoneNumber, emailAddresses);
						
			persons.add(person);
		}
		
		return persons;
	}
	
	// Parses a list of Company objects from a given file `fromFile`. 
	// `fromFile` is expected to be a .csv file that is properly formatted.
	public static List<Company> parseCompanies(String fromFile) {
		String companiesCsvString = readFileToString(fromFile);
				
		String[] lines = companiesCsvString.split("\\R+"); // splits on new-line combinations (\n, \n\r, \r\n, \r etc.)
		
		ArrayList<Company> companies = new ArrayList<>(); 
		
		for (int i = 1; i < lines.length; i++) {
			String[] splitLine = lines[i].split(",");
						
			String uuid = splitLine[0];
			String contactUuid = splitLine[1];
			String name = splitLine[2];
			String street = splitLine[3];
			String city = splitLine[4];
			String state = splitLine[5];
			String zip = splitLine[6];
			
			Address company_address = new Address(street, city, state, zip);
			Company company = new Company(uuid, contactUuid, name, company_address);
						
			companies.add(company);
		}
		
		return companies;
	}
	
	// Parses a list of Item objects from a given file `fromFile`. 
	// `fromFile` is expected to be a .csv file that is properly formatted.
	public static List<Item> parseItems(String fromFile) {
		String itemsCsvString = readFileToString(fromFile);
				
		String[] lines = itemsCsvString.split("\\R+"); // splits on new-line combinations (\n, \n\r, \r\n, \r etc.)
		
		ArrayList<Item> items = new ArrayList<>(); 
		
		for (int i = 1; i < lines.length; i++) {
			String[] splitLine = lines[i].split(",");
			
			String uuid = splitLine[0];
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
				String companyUuid = splitLine[3];
				item = new Contract(uuid, name, companyUuid);
			} else {
				System.err.println("Error: Unknown item type \"" + type + "\" found on line " + (i + 1) + " of \"data/Items.csv\".");
				System.exit(1);
			}
						
			items.add(item);
		}
		
		return items;
	}
}
