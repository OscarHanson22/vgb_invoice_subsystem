/**
 * Authors: Oscar Hanson and Ermias Wolde
 * Date: 5/9/2025
 * Purpose: Provides tests for item subclasses.
 * 
 */

package com.vgb;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
 * JUnit test suite for VGB invoice system.
 */
public class EntityTests {
	public static final double TOLERANCE = 0.001;

	/**
	 * Creates an instance of a piece of equipment and tests if
	 * its cost and tax calculations are correct.
	 * 
	 * Also checks the equipment's toString() method. 
	 */
	@Test
	public void testEquipment() {
		UUID uuid = UUID.randomUUID();
		String name = "Backhoe 3000";
		String model = "BH30X2";
		double cost = 95125.00;
		
		Equipment equipment = new Equipment(uuid, name, model, cost);

		double expectedCost = 95125.00;
		double expectedTax = 4994.06;

		double actualCost = equipment.getTotal().getCost();
		double actualTax = equipment.getTotal().getTax();

		assertEquals(expectedCost, actualCost, TOLERANCE);
		assertEquals(expectedTax, actualTax, TOLERANCE);
		
		String s = equipment.toString();
		
		assertTrue(s.contains(name));
		assertTrue(s.contains(model));
		assertTrue(s.contains(Double.toString(cost)));
	}

	/**
	 * Creates an instance of a piece of equipment and tests if
	 * its leasing cost and tax calculations are correct.
	 */
	@Test
	public void testLease() {
		UUID uuid = UUID.randomUUID();
		String name = "Backhoe 3000";
		String model = "BH30X2";
		double cost = 95125.00;
		LocalDate startDate = LocalDate.parse("2024-01-01");
		LocalDate endDate = LocalDate.parse("2026-06-01");
		
		Equipment equipment = new Equipment(uuid, name, model, cost);
		EquipmentLease equipmentLease = new EquipmentLease(equipment, startDate, endDate);

		double expectedCost = 69037.29;
		double expectedTax = 1500.00;

		double actualCost = equipmentLease.getTotal().getCost();
		double actualTax = equipmentLease.getTotal().getTax();

		assertEquals(expectedCost, actualCost, TOLERANCE);
		assertEquals(expectedTax, actualTax, TOLERANCE);
	}

	/**
	 * Creates an instance of a piece of equipment and tests if
	 * its rental costs and tax calculations are correct.
	 */
	@Test
	public void testRental() {
		UUID uuid = UUID.randomUUID();
		String name = "Backhoe 3000";
		String model = "BH30X2";
		double cost = 95125.00;
		double hours = 25.0;
		
		Equipment equipment = new Equipment(uuid, name, model, cost);
		EquipmentRental equipmentRental = new EquipmentRental(equipment, hours);

		double expectedCost = 2378.13;
		double expectedTax = 104.16;
		
		double actualCost = equipmentRental.getTotal().getCost();
		double actualTax = equipmentRental.getTotal().getTax();

		assertEquals(expectedCost, actualCost, TOLERANCE);
		assertEquals(expectedTax, actualTax, TOLERANCE);
	}

	/**
	 * Creates an instance of a material and tests if
	 * its purchasing cost and tax calculations are correct.
	 * 
	 * Also checks the material's toString() method.
	 */
	@Test
	public void testMaterial() {
		UUID uuid = UUID.randomUUID();
		String name = "Nails";
		String unit = "Box";
		double cost = 9.99;
		int quantity = 31;
		
		Material baseMaterial = new Material(uuid, name, unit, cost);
		Material material = new Material(baseMaterial, quantity);

		double actualCost = material.getTotal().getCost();
		double actualTax = material.getTotal().getTax();
		
		double expectedCost = 309.69;
		double expectedTax = 22.14;

		assertEquals(expectedCost, actualCost, TOLERANCE);
		assertEquals(expectedTax, actualTax, TOLERANCE);
		
		String s = material.toString();
		
		assertTrue(s.contains(name));
		assertTrue(s.contains(unit));
		assertTrue(s.contains(Double.toString(cost)));
	}

	/**
	 * Creates an instance of a contact and tests if
	 * its toString() method works properly.
	 */
	@Test
	public void testContract() {
		UUID uuid = UUID.randomUUID();
		Company subcontractor = new Company(null, null, "Company A", null);
		String name = "Box Office Co.";
		double amount = 10000.0;
		
		Contract baseContract = new Contract(uuid, name, subcontractor);
		Contract contract = new Contract(baseContract, amount);
		
		String s = contract.toString();
		
		assertTrue(s.contains(name));
		assertTrue(s.contains(uuid.toString()));
	}
}