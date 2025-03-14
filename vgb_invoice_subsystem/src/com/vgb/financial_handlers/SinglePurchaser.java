package com.vgb.financial_handlers;


public class SinglePurchaser {
	private Purchaser purchaser;
	
	public SinglePurchaser(double taxRate) {
		purchaser = new Purchaser(taxRate);
	}
	
	public Total total(double price) {
		return purchaser.total(price, 1);
	}
}