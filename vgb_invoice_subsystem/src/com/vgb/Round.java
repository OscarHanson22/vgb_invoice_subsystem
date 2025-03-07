package com.vgb;

public class Round {
	public static double toCents(double amount) {
		return Math.round(amount * 100.0) / 100.0;
	}
}
