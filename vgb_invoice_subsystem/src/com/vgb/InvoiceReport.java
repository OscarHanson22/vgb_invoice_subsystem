package com.vgb;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vgb.database_factories.CompanyFactory;
import com.vgb.database_factories.ConnectionFactory;
import com.vgb.database_factories.InvoiceFactory;
import com.vgb.lists.SortedList;

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
		
		SortedList<Invoice> invoicesSortedByTotal = new SortedList<>(new InvoiceTotalDescending(), invoices);
		SortedList<Invoice> invoicesSortedAlphabetically= new SortedList<>(new InvoiceCustomerNameAlphabetical(), invoices);
		SortedList<Company> customersSortedByInvoiceTotal = new SortedList<>(new CompanyInvoiceTotalAscending(invoices), companies);
		
		System.out.println("Invoice Report | By Total");
		System.out.println(invoicesSummary(invoicesSortedByTotal) + "\n\n");
		System.out.println("Invoice Report | By Name");
		System.out.println(invoicesSummary(invoicesSortedAlphabetically) + "\n\n");
		System.out.println("Customer Report | By Invoice Total");
		System.out.println(customersSummary(customersSortedByInvoiceTotal, invoices) + "\n\n");

		ConnectionFactory.closeConnection();
	}
	
	/**
	 * Generates and returns a summary report of all invoices in the invoicing subsystem.
	 * 
	 * @param invoices The list of invoices the report should cover.
	 */
	public static String invoicesSummary(SortedList<Invoice> invoices) {
		Total total = Total.empty();
		int totalAmountOfItems = 0;
		StringBuilder invoicesSB = new StringBuilder();
		
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

	/**
	 * Generates and returns a report about the customers registered in the invoicing subsystem.
	 * 
	 * @param invoices The list of invoices the report should cover.
	 */
	public static String customersSummary(SortedList<Company> customers, List<Invoice> invoices) {
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
	public static String detailedInvoicesSummary(SortedList<Invoice> invoices) {		
		Total total = Total.empty();
		int totalAmountOfItems = 0;
		StringBuilder invoicesSB = new StringBuilder();
				
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
	 * A comparator that orders invoices by their total value (i.e. the sum of all of their items).
	 */
	private static class InvoiceTotalDescending implements Comparator<Invoice> {
	    @Override
	    public int compare(Invoice i1, Invoice i2) {
	        int comparisonValue = Double.compare(
	        	i2.getTotalValue(), 
	        	i1.getTotalValue()
	        );
			
			if (comparisonValue == 0) {
				return i1.getUuid().compareTo(i2.getUuid());
			}
			
			return comparisonValue;
	    }
	}
	
	/**
	 * A comparator that orders invoices alphabetically by the name of their customer.
	 */
	private static class InvoiceCustomerNameAlphabetical implements Comparator<Invoice> {
	    @Override
	    public int compare(Invoice i1, Invoice i2) {
	        int comparisonValue = i1.getCustomer().getName().compareTo(i2.getCustomer().getName());
				
			if (comparisonValue == 0) {
				return i2.getUuid().compareTo(i1.getUuid());
			}
			
			return comparisonValue;
	    }
	}
	
	/**
	 * A comparator that orders companies by the total of all of their invoices.
	 */
	private static class CompanyInvoiceTotalAscending implements Comparator<Company> {
		/**
		 * Contains all of the invoice totals for each company.
		 */
	    private final Map<String, Double> companyToTotal;

	    /**
	     * Creates and populates a comparator to compare companies by the total of their invoices. 
	     * 
	     * @param invoices The invoices to populate the comparator with. 
	     */
		public CompanyInvoiceTotalAscending(List<Invoice> invoices) {
			companyToTotal = new HashMap<>();
			
			for (Invoice invoice : invoices) {
				Company customer = invoice.getCustomer();
				double total = invoice.getTotalValue();
				if (!companyToTotal.containsKey(customer.getName())) {
					companyToTotal.put(customer.getName(), total);
				} else {
					companyToTotal.put(customer.getName(), companyToTotal.get(customer.getName()) + total);
				}
			}
		}

		@Override
		public int compare(Company c1, Company c2) {
			int comparisonValue = Double.compare(
				companyToTotal.getOrDefault(c1.getName(), 0.0), 
				companyToTotal.getOrDefault(c2.getName(), 0.0)
			);
			
			if (comparisonValue == 0) {
				return c1.getUuid().compareTo(c2.getUuid());
			}
			
			return comparisonValue;
		}
	}
}
