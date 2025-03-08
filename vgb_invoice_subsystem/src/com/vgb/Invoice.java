package com.vgb;

public class Invoice {
	private String uuid;
	private String customerUuid;
	private String salespersonUuid;
	private String date;
	private double taxTotal;
	private double subtotal;
	
	public Invoice(String uuid, String customerUuid, String salespersonUuid, String date) {
		this.uuid = uuid;
		this.customerUuid = customerUuid;
		this.salespersonUuid = salespersonUuid;
		this.date = date;
		this.taxTotal = 0.0;
		this.subtotal = 0.0;
	}
	
	public void addItem(double total, double tax) {
		subtotal += total;
		taxTotal += tax;
	}
	
	public double subtotal() {
		return subtotal;
	}
	
	public double taxTotal() {
		return taxTotal;
	}
	
	public double grandTotal() {
		return subtotal + taxTotal;
	}

	@Override
	public String toString() {
		return "Invoice [uuid=" + uuid + ", customerUuid=" + customerUuid + ", salespersonUuid=" + salespersonUuid + ", date=" + date + "]";
	}
}
