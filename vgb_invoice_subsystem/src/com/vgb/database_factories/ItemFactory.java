package com.vgb.database_factories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

//create table Item (
//		itemId int not null primary key auto_increment,
//		uuid varchar(50) not null, 
//		name varchar(200) not null, 
//		discriminator varchar(100) not null, 
//		equipmentModelNumber varchar(100), 
//		equipmentRetailPrice double, 
//		materialUnit varchar(100), 
//		materialPricePerUnit double
//	);

//-- Represents an item in an invoice (and other necessary information that changes from sales to sale)
//create table InvoiceItem (
//	invoiceItemId int not null primary key auto_increment, 
//	invoiceId int not null,
//	itemId int not null, 
//	equipmentDiscriminator varchar(50), 
//	leaseAmount double,
//	leaseStartDate varchar(50),
//	leaseEndDate varchar(50),
//	rentAmount double, 
//	rentedHours int,
//	contractAmount double,
//	contractSubcontractorId int,
//	materialQuantity int,
//	foreign key (invoiceId) references Invoice(invoiceId), 
//	foreign key (itemId) references Item(itemId),
//	foreign key (contractSubcontractorId) references Company(companyId)
//);


import java.util.List;
import java.util.UUID;

import com.vgb.Company;
import com.vgb.Contract;
import com.vgb.Equipment;
import com.vgb.EquipmentLease;
import com.vgb.EquipmentRental;
import com.vgb.Item;
import com.vgb.Material;

public class ItemFactory {
	public static Item loadItem(Connection connection, int itemId) {
		Item item = null;
		String uuidString = null; // used for error messaging
		String dateString = null; // used for error messaging
		
		String query = 
			"select uuid, name, discriminator, " // item info
			+ "equipmentModelNumber, equipmentRetailPrice, equipmentDiscriminator, " // equipment (and subclasses) info
			+ "leaseStartDate, leaseEndDate, "
			+ "rentedHours, "
			+ "materialUnit, materialPricePerUnit, materialQuantity, " // material info
			+ "contractAmount, contractSubcontractorId " // contract info
			+ "from Item item "
			+ "join InvoiceItem invoiceItem on item.itemId = invoiceItem.itemId "
			+ "where item.itemId = ?;";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, itemId);
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				uuidString = results.getString("uuid");
				UUID uuid = UUID.fromString(uuidString);
				String name = results.getString("name");
				String discriminator = results.getString("discriminator");
				
				// load an equipment, equipment rental, or equipment lease
				if (discriminator.equals("Equipment")) {
//					public Equipment(UUID uuid, String name, String modelNumber, double retailPrice) {

					String modelNumber = results.getString("equipmentModelNumber");
					double retailPrice = results.getDouble("equipmentRetailPrice");
					Equipment baseEquipment = new Equipment(uuid, name, modelNumber, retailPrice);
					String equipmentDiscriminator = results.getString("equipmentDiscriminator");
					// loads an equipment (purchase)
					if (equipmentDiscriminator.equals("Purchase")) {
						item = baseEquipment;
					// loads an equipment rental
					} else if (equipmentDiscriminator.equals("Rental")) {
						int hours = results.getInt("rentedHours");
						item = new EquipmentRental(baseEquipment, hours);
					// loads an equipment lease
					} else if (equipmentDiscriminator.equals("Lease")) {
						dateString = results.getString("leaseStartDate");
						LocalDate startDate = LocalDate.parse(dateString);
						dateString = results.getString("leaseEndDate");
						LocalDate endDate = LocalDate.parse(dateString);
						item = new EquipmentLease(baseEquipment, startDate, endDate);
					} else {
						throw new IllegalStateException("Equipment discriminator for item with itemId: " + itemId + " is formatted incorrectly: \"" + equipmentDiscriminator + "\" (must be \"Purchase\", \"Rental\", or \"Lease\".");
					}
				// load a material
				} else if (discriminator.equals("Material")) { 
					String unit = results.getString("materialUnit");
					double costPerUnit = results.getDouble("materialPricePerUnit");
					int quantity = results.getInt("materialQuantity");
					Material baseMaterial = new Material(uuid, name, unit, costPerUnit);
					item = new Material(baseMaterial, quantity);
				// load a contract
				} else if (discriminator.equals("Contract")) {
					int subcontractorId = results.getInt("contractSubcontractorId");
					Company subcontractor = CompanyFactory.loadCompany(connection, subcontractorId);
					double amount = results.getDouble("contractAmount");
					Contract baseContract = new Contract(uuid, name, subcontractor);
					item = new Contract(baseContract, amount);
					
				} else {
					throw new IllegalStateException("Discriminator for item with itemId: " + itemId + " is formatted incorrectly: \"" + discriminator + "\" (must be \"Equipment\", \"Material\", or \"Contract\".");
				}
			} else {
				throw new IllegalStateException("Item with itemId: " + itemId + " not found in the database.");
			}
		// Only occurs when the `UUID.fromString(...)` above fails
		} catch (IllegalArgumentException e) {
			System.err.println("UUID for item with itemId: " + itemId + " is formatted incorrectly: \"" + uuidString + "\".");
			e.printStackTrace();
			throw new RuntimeException(e);
		// Only occurs when the `LocalDate.parse(...)` above fails
		} catch (DateTimeParseException e) {
			System.err.println("LocalDate for equipment rental with itemId: " + itemId + " is formatted incorrectly: \"" + dateString + "\".");
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (SQLException e) {
			System.err.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
		
		return item;
	}
	
	public static List<Item> loadAllItems(Connection connection) {
		List<Item> items = new ArrayList<>();
		
		String query = "select itemId from Item";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				int itemId = results.getInt("itemId");
				Item item = loadItem(connection, itemId);
				items.add(item);
			}
		} catch (SQLException e) {
			System.err.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
			
		return items;
	}
}