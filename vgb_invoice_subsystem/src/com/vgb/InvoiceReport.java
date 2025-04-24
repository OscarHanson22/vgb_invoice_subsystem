package com.vgb;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.vgb.database_factories.CompanyFactory;
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
		Connection connection = ConnectionFactory.getConnection();		
		List<Invoice> invoices = InvoiceFactory.loadAllInvoices(connection);
		List<Company> companies = CompanyFactory.loadAllCompanies(connection);
		System.out.println(customersSummary(companies, invoices) + "\n\n");
		System.out.println(invoicesSummary(invoices) + "\n\n");
		System.out.println(detailedInvoicesSummary(invoices) + "\n\n");
		ConnectionFactory.closeConnection();
	}

	/**
	 * Generates and returns a report about the customers registered in the invoicing subsystem.
	 * 
	 * @param invoices The list of invoices the report should cover.
	 */
	public static String customersSummary(List<Company> customers, List<Invoice> invoices) {
		Total total = Total.empty();
		int totalAmountOfInvoices = 0;
				
		Map<String, List<Invoice>> customersAndInvoices = new HashMap<>();
		
		for (Company customer : customers) {
			customersAndInvoices.put(customer.getName(), new ArrayList<Invoice>());
		}
		
		
		for (Invoice invoice : invoices) {
			List<Invoice> customerInvoices = customersAndInvoices.get(invoice.getCustomer().getName());
			if (customerInvoices == null) {
				System.err.println("Company: \"" + invoice.getCustomer() + "\" not found in companies.");
				throw new IllegalStateException("Unknown company found in invoice.");
			}
			customerInvoices.add(invoice);
		}
				
		StringBuilder companiesSB = new StringBuilder();
		companiesSB.append("Company Invoice Summary Report\n\n");
		
		customers.sort(Comparator.comparing(Company::getName));
		
		for (Company customer : customers) {
			List<Invoice> customerInvoices = customersAndInvoices.get(customer.getName());
			int amountOfInvoices = customerInvoices.size();
			Total customerTotal = Total.empty();
			for (Invoice invoice : customerInvoices) {
				customerTotal.add(invoice.getTotal());
			}
			total.add(customerTotal);
			totalAmountOfInvoices += amountOfInvoices;
			
			companiesSB.append("Customer: " + customer.getName() + "\n");
			companiesSB.append("Amount of Invoices: " + amountOfInvoices + "\n");
			companiesSB.append(customerTotal + "\n\n");
		}
	    
		companiesSB.append("Total Number of Invoices: " + totalAmountOfInvoices + "\n");
	    companiesSB.append("Grand Total: $" + String.format("%.2f", total.getTotal()) + "\n");
	    
		return companiesSB.toString();
    }
    
	/**
	 * Generates and returns a detailed report of all invoices in the invoicing subsystem.
	 * 
	 * @param invoices The list of invoices the report should cover.
	 */
	public static String detailedInvoicesSummary(List<Invoice> invoices) {
		invoices.sort(Comparator.comparing(Invoice::getTotalValue).reversed());
		
		Total total = Total.empty();
		int totalAmountOfItems = 0;
		StringBuilder invoicesSB = new StringBuilder();
		invoicesSB.append("Summary Report | By Total\n\n");
		
		invoicesSB.append("Detailed Summary Report \n\n");
		
        for (Invoice invoice : invoices) {
        	invoicesSB.append(invoice);
        	invoicesSB.append("\n");
        	total.add(invoice.getTotal());
        	totalAmountOfItems += invoice.getItems().size();
        }
        
        invoicesSB.append("Total Number of Items: " + totalAmountOfItems + "\n");
        invoicesSB.append("Subtotal: $" + String.format("%.2f", total.getCost()) + "\n");
        invoicesSB.append("Tax Total: $" + String.format("%.2f", total.getTax()) + "\n");
        invoicesSB.append("Grand Total: $" + String.format("%.2f", total.getTotal()) + "\n");
        
        return invoicesSB.toString();
    }
	
	/**
	 * Generates and returns a summary report of all invoices in the invoicing subsystem.
	 * 
	 * @param invoices The list of invoices the report should cover.
	 */
	public static String invoicesSummary(List<Invoice> invoices) {
		invoices.sort(Comparator.comparing(Invoice::getTotalValue).reversed());

		Total total = Total.empty();
		int totalAmountOfItems = 0;
		StringBuilder invoicesSB = new StringBuilder();
		invoicesSB.append("Summary Report | By Total\n\n");
		
		for (Invoice invoice : invoices) {
			invoicesSB.append(invoice.summary());
			total.add(invoice.getTotal());
			totalAmountOfItems += invoice.getItems().size();
		}
		
		invoicesSB.append("Total Number of Items: " + totalAmountOfItems + "\n");
        invoicesSB.append("Subtotal: $" + String.format("%.2f", total.getCost()) + "\n");
        invoicesSB.append("Tax Total: $" + String.format("%.2f", total.getTax()) + "\n");
        invoicesSB.append("Grand Total: $" + String.format("%.2f", total.getTotal()) + "\n");
	
		return invoicesSB.toString();
	}
}
