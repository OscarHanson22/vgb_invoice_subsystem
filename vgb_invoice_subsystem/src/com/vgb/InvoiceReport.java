package com.vgb;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InvoiceReport {
	public static void main(String[] args) {
		InvoiceSubsystem invoiceSubsystem = new InvoiceSubsystem("data/PersonsTest.csv", "data/CompaniesTest.csv", "data/ItemsTest.csv", "data/InvoicesTest.csv");
		List<Invoice> invoices = invoiceSubsystem.addInvoiceItems("data/InvoiceItemsTest.csv");
		
//		for (Invoice invoice : invoices) {
//			System.out.println("Subtotal: $" + invoice.subtotal());
//			System.out.println("Tax total: $" + invoice.taxTotal());
//			System.out.println("Grand total: $" + invoice.grandTotal());
//		}
		
		String outputFilePath = "data/output.txt";
//		
		System.out.println(customersSummary(invoices));
//		System.out.println("NEXT SUMMARYYYYYYYYYYYYYYYYY");
//		invoiceDetailedSummary(invoices);
//		System.out.println("NEXT SUMMARYYYYYYYYYYYYYYYYY");
//		customerSummary(invoices);
	}
		
		
//		The first report will give a summary of all invoices along with a few totals.

//		The second report will give a similar summary but for each customer.
	public static String customersSummary(List<Invoice> invoices) {
		Total total = Total.empty();
		int totalAmountOfInvoices = 0;
		
		StringBuilder companiesStringBuilder = new StringBuilder();
		
		companiesStringBuilder.append("Company Invoice Summary Report\n\n");
			
		Map<String, Total> customerTotals = new HashMap<>();
		Map<String, Integer> customerInvoiceAmount = new HashMap<>();
	        
		for (Invoice invoice : invoices) {
	    	String customerName = invoice.getCustomer().getName();
	    	customerTotals.putIfAbsent(customerName, Total.empty());
	    	Total customerTotal = customerTotals.get(customerName);
	    	customerTotal.add(invoice.getTotal());
	    	customerTotals.put(customerName, customerTotal);
	    	customerInvoiceAmount.putIfAbsent(customerName, 0);
	    	customerInvoiceAmount.put(customerName, customerInvoiceAmount.get(customerName) + 1);
	    }
		
		Iterator<Map.Entry<String, Total>> totals = customerTotals.entrySet().iterator();
		Iterator<Map.Entry<String, Integer>> amountOfInvoices = customerInvoiceAmount.entrySet().iterator();

		// Iterate over both `customerTotals` and `amountOfInvoices`
        while (totals.hasNext() && amountOfInvoices.hasNext()) {
        	String customerName = null;
        	Total customerTotal = Total.empty();
        	int customerAmountOfInvoices = 0;
        	
            if (totals.hasNext()) {
                Map.Entry<String, Total> nameAndTotal = totals.next();
                customerName = nameAndTotal.getKey();
            }

            if (amountOfInvoices.hasNext()) {
                Map.Entry<String, Integer> nameAndAmountOfInvoices = amountOfInvoices.next();
                customerAmountOfInvoices = nameAndAmountOfInvoices.getValue();
            }
            
            companiesStringBuilder.append("Customer: " + customerName + "\n");
	    	companiesStringBuilder.append("Number of Invoices: " + customerAmountOfInvoices + "\n");
	    	companiesStringBuilder.append("Grand Total: $" + String.format("%.2f", customerTotal.getTotal()) + "\n\n");
	    	
	    	totalAmountOfInvoices += customerAmountOfInvoices;
	    	total.add(customerTotal);
        }
	    
	    companiesStringBuilder.append("Total Number of Invoices: " + totalAmountOfInvoices + "\n");
		companiesStringBuilder.append("Grand Total: $" + String.format("%.2f", total.getTotal()) + "\n");
	    
		return companiesStringBuilder.toString();
    }
    

//		The final report will give details for each individual invoice.
	public static void invoiceDetailedSummary(List<Invoice> invoices) {
		
		
		System.out.println("Invoice Detailed Summary:");
		
        for (Invoice invoice : invoices) {
            System.out.println("Invoice UUID: " + invoice.getUuid());
            System.out.println("Customer: " + invoice.getCustomer().getFirstName() + " " + invoice.getCustomer().getLastName());
            System.out.println("Salesperson: " + invoice.getSalesperson().getFirstName() + " " + invoice.getSalesperson().getLastName());
            System.out.println("Items:");
            
        for (Item item : invoice.getItems()) {
            System.out.println("  - " + item.getName() + " | Price: $" + item.getTotal());
        }
            
        System.out.println("Subtotal: $" + invoice.subtotal());
        System.out.println("Tax Total: $" + invoice.taxTotal());
        System.out.println("Grand Total: $" + invoice.grandTotal());
        System.out.println("------------------------------");
        }
    }
	
	public static String invoicesSummary(List<Invoice> invoices) {
		Total total = Total.empty();
		int totalAmountOfItems = 0;
		
		StringBuilder invoicesStringBuilder = new StringBuilder();
		
		invoicesStringBuilder.append("Summary Report | By Total\n\n");
		
		for (Invoice invoice : invoices) {
			int amountOfItems = invoice.getItems().size();
			
			invoicesStringBuilder.append("Invoice UUID: " + invoice.getUuid() + "\n");
			invoicesStringBuilder.append("Customer: " + invoice.getCustomer().getName() + "\n");
			invoicesStringBuilder.append("Sold by: " + invoice.getSalesperson() + "\n");
			invoicesStringBuilder.append("Number of Items: " + amountOfItems + "\n");
			invoicesStringBuilder.append(invoice.getTotal() + "\n\n");
			
			total.add(invoice.getTotal());
			totalAmountOfItems += amountOfItems;
		}
		
		invoicesStringBuilder.append("Total Number of Items: " + totalAmountOfItems + "\n");
		invoicesStringBuilder.append("Subtotal: $" + total.getCost() + "\n");
		invoicesStringBuilder.append("Tax Total: $" + Round.toCents(total.getTax()) + "\n");
		invoicesStringBuilder.append("Grand Total: $" + total.getTotal() + "\n");
	
		return invoicesStringBuilder.toString();
	}
	
}
