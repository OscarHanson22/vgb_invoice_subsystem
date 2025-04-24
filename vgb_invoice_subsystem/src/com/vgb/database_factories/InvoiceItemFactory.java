package com.vgb.database_factories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vgb.Company;
import com.vgb.Contract;
import com.vgb.Equipment;
import com.vgb.EquipmentLease;
import com.vgb.EquipmentRental;
import com.vgb.Item;
import com.vgb.Material;

public class InvoiceItemFactory {
    private static final Logger logger = LogManager.getLogger(InvoiceItemFactory.class);

	/**
     * Loads an Item object with the specified itemId, complete with information from the InvoiceItem table in the database.
     * 
     * @param connection The connection to the database.
     * @param itemId The itemId of the item in the database. 
     */
	public static Item loadInvoiceItem(Connection connection, int itemId) {
		Item item = null;
		Item baseItem = ItemFactory.loadItem(connection, itemId);		
		String dateString = null; // used for error messaging
		
		String query = 
			"select equipmentDiscriminator, " // equipment (and subclasses) info
			+ "leaseStartDate, leaseEndDate, "
			+ "rentedHours, "
			+ "materialQuantity, " // material info
			+ "contractAmount " // contract info
			+ "from InvoiceItem "
			+ "where itemId = ?;";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, itemId);
			ResultSet results = statement.executeQuery();
			
			if (results.next()) {
				// load an equipment, equipment rental, or equipment lease
				if (baseItem.getClass() == Equipment.class) {
					String equipmentDiscriminator = results.getString("equipmentDiscriminator");
					// loads an equipment (purchase)
					if (equipmentDiscriminator.equals("Purchase")) {
						item = baseItem;
					// loads an equipment rental
					} else if (equipmentDiscriminator.equals("Rental")) {
						double hours = results.getDouble("rentedHours");
						item = new EquipmentRental((Equipment) baseItem, hours);
					// loads an equipment lease
					} else if (equipmentDiscriminator.equals("Lease")) {
						dateString = results.getString("leaseStartDate");
						LocalDate startDate = LocalDate.parse(dateString);
						dateString = results.getString("leaseEndDate");
						LocalDate endDate = LocalDate.parse(dateString);
						item = new EquipmentLease((Equipment) baseItem, startDate, endDate);
					} else {
						logger.error("Equipment discriminator for item with itemId: " + itemId + " is formatted incorrectly: \"" + equipmentDiscriminator + "\" (must be \"Purchase\", \"Rental\", or \"Lease\".");
						throw new IllegalStateException("Equipment discriminator for item with itemId: " + itemId + " is formatted incorrectly: \"" + equipmentDiscriminator + "\" (must be \"Purchase\", \"Rental\", or \"Lease\".");
					}
				// load a material
				} else if (baseItem.getClass() == Material.class) { 
					int quantity = results.getInt("materialQuantity");
					item = new Material((Material) baseItem, quantity);
				// load a contract
				} else if (baseItem.getClass() == Contract.class) {
					double amount = results.getDouble("contractAmount");
					item = new Contract((Contract) baseItem, amount);
				// else the item is not a valid subclass.
				} else {
					logger.error("Item with itemId: " + itemId + " is not an Equipment, Material, or Contract.");
					throw new IllegalStateException("Item with itemId: " + itemId + " is not an Equipment, Material, or Contract.");
				}
			} else {
				logger.error("Invoice item with itemId: " + itemId + " not found in the database.");
				throw new IllegalStateException("Invoice item with itemId: " + itemId + " not found in the database.");
			}
		// Only occurs when the `LocalDate.parse(...)` above fails
		} catch (DateTimeParseException e) {
			logger.error("LocalDate for equipment lease with itemId: " + itemId + " is formatted incorrectly: \"" + dateString + "\".");
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
		String query = 
			"select temId from InvoiceItem where uuid = ?;";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, uuid.toString());
			ResultSet results = statement.executeQuery();
			if (results.next()) {
				itemId = Optional.of(results.getInt("itemId"));
			} else {
				logger.warn("Invoice item with UUID: \"" + uuid + "\" not found in the database.");
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
	public static List<Item> loadAllInvoiceItems(Connection connection) {
		List<Item> items = new ArrayList<>();
		
		String query = "select itemId from InvoiceItem";
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			ResultSet results = statement.executeQuery();
			while (results.next()) {
				int itemId = results.getInt("itemId");
				Item item = loadInvoiceItem(connection, itemId);
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
}
