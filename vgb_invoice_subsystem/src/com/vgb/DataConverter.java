package com.vgb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DataConverter {
	public static void main(String[] args) {
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
		
		Parser.writeStringToFile(JSONFormatter.toJSON(people), "data/PersonsTest.json");
		Parser.writeStringToFile(JSONFormatter.toJSON(companies), "data/CompaniesTest.json");
		Parser.writeStringToFile(JSONFormatter.toJSON(items), "data/ItemsTest.json");
		Parser.writeStringToFile(XMLFormatter.toXML(people), "data/PersonsTest.xml");
		Parser.writeStringToFile(XMLFormatter.toXML(companies), "data/CompaniesTest.xml");
		Parser.writeStringToFile(XMLFormatter.toXML(items), "data/ItemsTest.xml");
	}
}
