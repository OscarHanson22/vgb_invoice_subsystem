package com.vgb;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
 * JUnit test suite for VGB invoice system.
 */
public class InvoiceTests {

	public static final double TOLERANCE = 0.001;

	/**
	 * Tests the subtotal, tax total and grand total values of an invoice in
	 * the VGB system.
	 * 
	 * This test specifically covers equipment purchasing, leasing, and renting. 
	 * 
	 * Also checks the invoice's toString() method.
	 */
	@Test
	public void testInvoice01() {
		String uuid = UUID.randomUUID().toString();
		String customerUuid = UUID.randomUUID().toString();
		String salespersonUuid = UUID.randomUUID().toString();
		String date = "2024-07-23";
		
		Invoice invoice = new Invoice(uuid, customerUuid, salespersonUuid, date);
		
		Equipment equipment = new Equipment(UUID.randomUUID().toString(), "Backhoe 3000", "BH30X2", 95125);
		
		LocalDate startDate = LocalDate.parse("2024-01-01");
		LocalDate endDate = LocalDate.parse("2026-06-01");
		
		invoice.addItem(equipment.purchaseCost(), equipment.purchaseTax());
		invoice.addItem(equipment.leaseCost(startDate, endDate), equipment.leaseTax(startDate, endDate));
		invoice.addItem(equipment.rentCost(25), equipment.rentTax(25));
					
		double expectedSubtotal = 95125.00 + 69037.29 + 2378.13;
		double expectedTaxTotal = 4994.06 + 1500.00 + 104.16;
		double expectedGrandTotal = 173138.64;

		double actualSubtotal = invoice.subtotal();
		double actualTaxTotal = invoice.taxTotal();
		double actualGrandTotal = invoice.grandTotal();

		assertEquals(expectedSubtotal, actualSubtotal, TOLERANCE);
		assertEquals(expectedTaxTotal, actualTaxTotal, TOLERANCE);
		assertEquals(expectedGrandTotal, actualGrandTotal, TOLERANCE);
		
		String s = invoice.toString();
		
		assertTrue(s.contains(uuid));
		assertTrue(s.contains(customerUuid));
		assertTrue(s.contains(salespersonUuid));
		assertTrue(s.contains(date));
	}

	/**
	 * Tests the subtotal, tax total and grand total values of an invoice in
	 * the VGB system.
	 * 
	 * This specific test covers material and contract purchasing.
	 * 
	 * Also checks the invoice's toString() method.
	 */
	@Test
	public void testInvoice02() {
		String uuid = UUID.randomUUID().toString();
		String customerUuid = UUID.randomUUID().toString();
		String salespersonUuid = UUID.randomUUID().toString();
		String date = "2024-07-23";
		
		Invoice invoice = new Invoice(uuid, customerUuid, salespersonUuid, date);
		
		Material material = new Material(UUID.randomUUID(), "Nails", "Box", 9.99);
		Contract contract = new Contract(UUID.randomUUID(), "Contract A", UUID.randomUUID().toString());
		
		invoice.addItem(material.purchaseCost(31), material.purchaseTax(31));
		invoice.addItem(10500.00, 0.00); // the contract
		
		double expectedSubtotal = 309.69 + 10500.00;
		double expectedTaxTotal = 22.14;
		double expectedGrandTotal = 10831.83;

		double actualSubtotal = invoice.subtotal();
		double actualTaxTotal = invoice.taxTotal();
		double actualGrandTotal = invoice.grandTotal();
		
		assertEquals(expectedSubtotal, actualSubtotal, TOLERANCE);
		assertEquals(expectedTaxTotal, actualTaxTotal, TOLERANCE);
		assertEquals(expectedGrandTotal, actualGrandTotal, TOLERANCE);
		
		String s = invoice.toString();
		
		assertTrue(s.contains(uuid));
		assertTrue(s.contains(customerUuid));
		assertTrue(s.contains(salespersonUuid));
		assertTrue(s.contains(date));
	}



}