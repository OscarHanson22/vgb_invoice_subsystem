package com.vgb;

import java.util.List;

public class DataConverter {
	public static void main(String[] args) {
		List<Person> people = Parser.parsePeople("data/Persons.csv");
		List<Company> companies = Parser.parseCompanies("data/Companies.csv");
		List<Item> items = Parser.parseItems("data/Items.csv");

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
