package com.vgb.financial_handlers;


public class SinglePurchaser {
	private Purchaser purchaser;
	
	public SinglePurchaser(double taxRate) {
		purchaser = new Purchaser(taxRate);
	}
	
	public double cost(double price) {		
		return purchaser.cost(price, 1);
	}
	
	public double tax(double price) {
		return purchaser.tax(price, 1);
	}
	
	public double total(double price) {
		return cost(price) + tax(price);
	}
}