package com.vgb.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.vgb.Contract;
import com.vgb.Equipment;
import com.vgb.Material;

/**
 * JUnit test suite for VGB invoice system.
 */
public class EntityTests {

	public static final double TOLERANCE = 0.001;

	/**
	 * Creates an instance of a piece of equipment and tests if
	 * its cost and tax calculations are correct.
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

		double actualCost = equipment.purchase_cost();
		double actualTax = equipment.purchase_tax();

		assertEquals(expectedCost, actualCost, TOLERANCE);
		assertEquals(expectedTax, actualTax, TOLERANCE);
		
		String s = equipment.toString();
		
		// ensure that the string representation contains necessary elements
		assertTrue(s.contains(name));
		assertTrue(s.contains(model));
		assertTrue(s.contains(Double.toString(cost)));
	}

	@Test
	public void testLease() {
		//data values
		UUID uuid = UUID.randomUUID();
		String name = "Backhoe 3000";
		String model = "BH30X2";
		double cost = 95125.00;
		
		Equipment equipment = new Equipment(uuid.toString(), name, model, (int) cost);

		//2. Establish the expected cost and tax (rounded to nearest cent)
		double expectedCost = 70537.29;
		
		LocalDate startDate = LocalDate.parse("2024-01-01");
		LocalDate endDate = LocalDate.parse("2026-06-01");

		double actualCost = equipment.lease(startDate, endDate);

		assertEquals(expectedCost, actualCost, TOLERANCE);
	}

	@Test
	public void testRental() {
		UUID uuid = UUID.randomUUID();
		String name = "Backhoe 3000";
		String model = "BH30X2";
		double cost = 95125.00;
		
		Equipment equipment = new Equipment(uuid.toString(), name, model, (int) cost);

		double expectedCost = 2482.29;
		double actualCost = equipment.rent(25);

		assertEquals(expectedCost, actualCost, TOLERANCE);
	}

	@Test
	public void testMaterial() {
		UUID uuid = UUID.randomUUID();
		String name = "Nails";
		String unit = "Box";
		double cost = 9.99;
		
		Material material = new Material(uuid.toString(), name, unit, cost);

		double actualCost = material.purchase_cost(31);
		double actualTax = material.purchase_tax(31);
		
		double expectedCost = 309.69;
		double expectedTax = 22.14;

		assertEquals(expectedCost, actualCost, TOLERANCE);
		assertEquals(expectedTax, actualTax, TOLERANCE);

		
		String s = material.toString();
		
		// ensure that the string representation contains necessary elements
		assertTrue(s.contains(name));
		assertTrue(s.contains(unit));
		assertTrue(s.contains(Double.toString(cost)));
	}


	@Test
	public void testContract() {
		UUID uuid = UUID.randomUUID();
		UUID contactUuid = UUID.randomUUID();
		String name = "Box Office Co.";
		
		Contract contract = new Contract(uuid.toString(), name, contactUuid.toString());
		
		String s = contract.toString();
		
		// ensure that the string representation contains necessary elements
		assertTrue(s.contains(name));
		assertTrue(s.contains(uuid.toString()));
		assertTrue(s.contains(contactUuid.toString()));
	}




}