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
		
		Equipment equipment = new Equipment(uuid.toString(), name, model, (int) cost);

		double expectedCost = 95125.00;
		double expectedTax = 4994.06;

		double actualCost = equipment.purchaseCost();
		double actualTax = equipment.purchaseTax();

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
		
		Equipment equipment = new Equipment(uuid.toString(), name, model, (int) cost);

		double expectedCost = 69037.29;
		double expectedTax = 1500.00;
		
		LocalDate startDate = LocalDate.parse("2024-01-01");
		LocalDate endDate = LocalDate.parse("2026-06-01");

		double actualCost = equipment.leaseCost(startDate, endDate);
		double actualTax = equipment.leaseTax(startDate, endDate);

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
		
		Equipment equipment = new Equipment(uuid.toString(), name, model, (int) cost);

		double expectedCost = 2378.13;
		double expectedTax = 104.16;
		
		double actualCost = equipment.rentCost(25);
		double actualTax = equipment.rentTax(25);

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
		
		Material material = new Material(uuid.toString(), name, unit, cost);

		double actualCost = material.purchaseCost(31);
		double actualTax = material.purchaseTax(31);
		
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
		UUID contactUuid = UUID.randomUUID();
		String name = "Box Office Co.";
		
		Contract contract = new Contract(uuid.toString(), name, contactUuid.toString());
		
		String s = contract.toString();
		
		assertTrue(s.contains(name));
		assertTrue(s.contains(uuid.toString()));
		assertTrue(s.contains(contactUuid.toString()));
	}




}