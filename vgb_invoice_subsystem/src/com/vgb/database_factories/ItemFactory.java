package com.vgb.database_factories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vgb.Company;
import com.vgb.Contract;
import com.vgb.Equipment;
import com.vgb.Item;
import com.vgb.Material;

/**
 * A class that loads Item objects from the Item and InvoiceItem tables in the database.
 */
public class ItemFactory {
    private static final Logger logger = LogManager.getLogger(ItemFactory.class);
	
	public static Item loadItem(Connection connection, int itemId) {
		logger.warn("Item with itemId " + itemId + " is about to be loaded.");
		Item item = null;
		String uuidString = null; // used for error messaging
		
		String query = 
			"select uuid, name, discriminator, " // item info
			+ "equipmentModelNumber, equipmentRetailPrice, " // equipment (and subclasses) info
			+ "materialUnit, materialPricePerUnit, " // material info
			+ "contractSubcontractorId " // contract info
			+ "from Item "
			+ "where itemId = ?;";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, itemId);
			ResultSet results = statement.executeQuery();
			
			if (results.next()) {
				uuidString = results.getString("uuid");
				UUID uuid = UUID.fromString(uuidString);
				String name = results.getString("name");
				String discriminator = results.getString("discriminator");
				
				// load an equipment
				if (discriminator.equals("Equipment")) {
					String modelNumber = results.getString("equipmentModelNumber");
					double retailPrice = results.getDouble("equipmentRetailPrice");
					item = new Equipment(uuid, name, modelNumber, retailPrice);
				// load a material
				} else if (discriminator.equals("Material")) { 
					String unit = results.getString("materialUnit");
					double costPerUnit = results.getDouble("materialPricePerUnit");
					item = new Material(uuid, name, unit, costPerUnit);
				// load a contract
				} else if (discriminator.equals("Contract")) {
					int subcontractorId = results.getInt("contractSubcontractorId");
					Company subcontractor = CompanyFactory.loadCompany(connection, subcontractorId);
					item = new Contract(uuid, name, subcontractor);
				// else the discriminator is not valid.
				} else {
					logger.error("Discriminator for item with itemId: " + itemId + " is formatted incorrectly: \"" + discriminator + "\" (must be \"Equipment\", \"Material\", or \"Contract\".");
					throw new IllegalStateException("Discriminator for item with itemId: " + itemId + " is formatted incorrectly: \"" + discriminator + "\" (must be \"Equipment\", \"Material\", or \"Contract\".");
				}
			} else {
				logger.error("Item with itemId: " + itemId + " not found in the database.");
				throw new IllegalStateException("Item with itemId: " + itemId + " not found in the database.");
			}
		// Only occurs when the `UUID.fromString(...)` above fails
		} catch (IllegalArgumentException e) {
			logger.error("UUID for item with itemId: " + itemId + " is formatted incorrectly: \"" + uuidString + "\".");
			throw new RuntimeException(e);
		} catch (SQLException e) {
			logger.error("SQLException encounted while loading Item with itemId " + itemId + " from the database.");
			throw new RuntimeException(e);
		} 
		
		return item;
	}
	
	/**
	 * Returns the itemId (if it exists) of the item with the specified UUID.
	 * 
	 * @param connection The connection to the database.
	 * @param uuid The UUID of the desired item. 
	 */
	public static Optional<Integer> getId(Connection connection, UUID uuid) {
		Optional<Integer> itemId = Optional.empty();
		String query = "select itemId, discriminator from Item where uuid = ?;";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, uuid.toString());
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				itemId = Optional.of(results.getInt("itemId"));
				String discriminator = results.getString("discriminator");
				logger.warn("Item discriminator: " + discriminator);
				logger.warn("itemId found: " + itemId);
				logger.warn(uuid);
			} else {
				logger.warn("Item with UUID: \"" + uuid + "\" not found in the database.");
			}
		} catch (SQLException e) {
			logger.error("SQLException encountered while getting itemId for Item with UUID: \"" + uuid + "\" from the database.");
			throw new RuntimeException(e);
		} 
			
		return itemId;
	}
	
	/**
	 * Loads all of the Item objects from the database.
	 * 
	 * @param connection The connection to the database.
	 */
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
			logger.error("SQLException encountered while loading all items from the database.");
			throw new RuntimeException(e);
		}
		
		if (items.size() == 0) {
			logger.warn("No items loaded from the database.");
		}
			
		return items;
	}
	
	public static void main(String[] args) {
		List<Item> items = loadAllItems(ConnectionFactory.getConnection());
		
		for (Item item : items) {
			System.out.println(item + "\n");
		}
		
		List<Item> invoiceItems = InvoiceItemFactory.loadAllInvoiceItems(ConnectionFactory.getConnection());
		
		for (Item item : invoiceItems) {
			System.out.println(item + "\n");
		}
	}
}