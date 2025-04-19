package com.vgb;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.vgb.database_factories.ConnectionFactory;
import com.vgb.database_factories.InvoiceFactory;

/**
 * Generates and prints various types of reports for the invoicing subsystem.
 */
public class InvoiceReport {
	/**
	 * Prints reports about the invoicing subsystem.
	 */
	public static void main(String[] args) {
		List<Invoice> invoices = InvoiceFactory.loadAllInvoices(ConnectionFactory.getConnection());
		System.out.println(customersSummary(invoices) + "\n\n");
		System.out.println(customersSummary(invoices) + "\n\n");
		System.out.println(detailedInvoicesSummary(invoices) + "\n\n");
	}

	/**
	 * Generates and returns a report about the customers registered in the invoicing subsystem.
	 * 
	 * @param invoices The list of invoices the report should cover.
	 */
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
                customerTotal = nameAndTotal.getValue();
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
    
	/**
	 * Generates and returns a detailed report of all invoices in the invoicing subsystem.
	 * 
	 * @param invoices The list of invoices the report should cover.
	 */
	public static String detailedInvoicesSummary(List<Invoice> invoices) {
		StringBuilder invoicesStringBuilder = new StringBuilder();
		
		invoicesStringBuilder.append("Detailed Summary Report \n\n");
		
        for (Invoice invoice : invoices) {
        	invoicesStringBuilder.append(invoice);
        	invoicesStringBuilder.append(invoice.getTotal() + "\n\n");
        }
        
        
        return invoicesStringBuilder.toString();
    }
	
	/**
	 * Generates and returns a summary report of all invoices in the invoicing subsystem.
	 * 
	 * @param invoices The list of invoices the report should cover.
	 */
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
