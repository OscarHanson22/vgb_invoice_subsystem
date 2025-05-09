package com.vgb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DataConverter {
	public static void main(String[] args) {
		System.out.println("Parsing objects...");
        // Parse people (returns Map, casted to HashMap)
        HashMap<UUID, Person> personMap = new HashMap<>(Parser.parsePeople("data/Persons.csv"));

        // Parse companies using the person map
        HashMap<UUID, Company> companyMap = new HashMap<>(Parser.parseCompanies("data/Companies.csv", personMap));

        HashMap<UUID, Item> itemMap = new HashMap<>(Parser.parseItems("data/Items.csv", companyMap));
        List<Item> items = new ArrayList<>(itemMap.values());

        // Convert maps to lists
        List<Person> people = new ArrayList<>(personMap.values());
        List<Company> companies = new ArrayList<>(companyMap.values());

		ToJSON JSONFormatter = new ToJSON();
		ToXML XMLFormatter = new ToXML();
		
		String personsCsvString = Parser.readFileToString("data/Persons.csv");
		String companiesCsvString = Parser.readFileToString("data/Companies.csv");
		String itemsCsvString = Parser.readFileToString("data/Items.csv");
		
		System.out.println("Persons.csv");
		System.out.println(personsCsvString);
		System.out.println();
		
		System.out.println("Companies.csv");
		System.out.println(companiesCsvString);
		System.out.println();

		System.out.println("Items.csv");
		System.out.println(itemsCsvString);
		System.out.println();
		
		String personsXmlString = XMLFormatter.toXML(people);
		String companiesXmlString = XMLFormatter.toXML(companies);
		String itemsXmlString = XMLFormatter.toXML(items);
		
		System.out.println("Persons.xml");
		System.out.println(personsXmlString);
		System.out.println();
		
		System.out.println("Companies.xml");
		System.out.println(companiesXmlString);
		System.out.println();
		
		System.out.println("Items.xml");
		System.out.println(itemsXmlString);
		System.out.println();
				
		String personsJsonString = JSONFormatter.toJSON(people);
		String companiesJsonString = JSONFormatter.toJSON(companies);
		String itemsJsonString = JSONFormatter.toJSON(items);
		
		System.out.println("Persons.json");
		System.out.println(personsJsonString);
		System.out.println();
		
		System.out.println("Companies.json");
		System.out.println(companiesJsonString);
		System.out.println();
		
		System.out.println("Items.json");
		System.out.println(itemsJsonString);
		System.out.println();
		
		System.out.println("Writing to files...");
		
		Parser.writeStringToFile(personsXmlString, "data/Persons.xml");
		Parser.writeStringToFile(companiesXmlString, "data/Companies.xml");
		Parser.writeStringToFile(itemsXmlString, "data/Items.xml");
		
		Parser.writeStringToFile(personsJsonString, "data/Persons.json");
		Parser.writeStringToFile(companiesJsonString, "data/Companies.json");
		Parser.writeStringToFile(itemsJsonString, "data/Items.json");
		
		System.out.println("Done!");
	}
}
