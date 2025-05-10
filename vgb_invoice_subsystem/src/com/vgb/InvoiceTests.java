/**
 * Authors: Oscar Hanson and Ermias Wolde
 * Date: 5/9/2025
 * Purpose: Unit tests that ensure invoices hold the necessary information. 
 */

package com.vgb;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
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
		UUID uuid = UUID.randomUUID();
		Company customer = new Company(UUID.randomUUID(), null, "Company A", null);
		Person salesperson = new Person(UUID.randomUUID(), "Oscar", "Hanson", null, new ArrayList<>());
		LocalDate invoiceDate = LocalDate.parse("2024-07-23");
		
		LocalDate startDate = LocalDate.parse("2024-01-01");
		LocalDate endDate = LocalDate.parse("2026-06-01");
		double hours = 25.0;
		
		Invoice invoice = new Invoice(uuid, customer, salesperson, invoiceDate);
		
		Equipment equipment = new Equipment(UUID.randomUUID(), "Backhoe 3000", "BH30X2", 95125);
		EquipmentLease equipmentLease = new EquipmentLease(equipment, startDate, endDate);
		EquipmentRental equipmentRental = new EquipmentRental(equipment, hours);
		
		invoice.addItem(equipment);
		invoice.addItem(equipmentLease);
		invoice.addItem(equipmentRental);
					
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
		
		assertTrue(s.contains(uuid.toString()));
		assertTrue(s.contains(customer.getName()));
		assertTrue(s.contains(customer.getUuid().toString()));		
		assertTrue(s.contains(salesperson.getLastName()));
		assertTrue(s.contains(salesperson.getUuid().toString()));
		assertTrue(s.contains(invoiceDate.toString()));
		assertTrue(s.contains(equipment.getUuid().toString()));
		assertTrue(s.contains(equipmentLease.getUuid().toString()));
		assertTrue(s.contains(equipmentRental.getUuid().toString()));
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
		UUID uuid = UUID.randomUUID();
		Company customer = new Company(UUID.randomUUID(), null, "Company A", null);
		Person salesperson = new Person(UUID.randomUUID(), "Oscar", "Hanson", null, new ArrayList<>());
		LocalDate invoiceDate = LocalDate.parse("2024-07-23");
		
		Invoice invoice = new Invoice(uuid, customer, salesperson, invoiceDate);
		
		Material baseMaterial = new Material(UUID.randomUUID(), "Nails", "Box", 9.99);
		Material material = new Material(baseMaterial, 31);
		
		Contract baseContract = new Contract(UUID.randomUUID(), "Contract A", customer);
		Contract contract = new Contract(baseContract, 10500.00);
		
		invoice.addItem(material);
		invoice.addItem(contract);
		
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
		
		assertTrue(s.contains(uuid.toString()));
		assertTrue(s.contains(customer.getName()));
		assertTrue(s.contains(customer.getUuid().toString()));		
		assertTrue(s.contains(salesperson.getLastName()));
		assertTrue(s.contains(salesperson.getUuid().toString()));
		assertTrue(s.contains(invoiceDate.toString()));
		assertTrue(s.contains(material.getUuid().toString()));
		assertTrue(s.contains(contract.getUuid().toString()));
	}



}