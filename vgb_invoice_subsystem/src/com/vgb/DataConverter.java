package com.vgb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DataConverter {
	public static void main(String[] args) {
		private static final String INPUT_DIR = "data/";
	    private static final String OUTPUT_DIR = "output/";
	    
	    private static final String PERSONS_CSV = INPUT_DIR + "Persons.csv";
	    private static final String COMPANIES_CSV = INPUT_DIR + "Companies.csv";
	    private static final String ITEMS_CSV = INPUT_DIR + "Items.csv";

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
