/**
 * Authors: Oscar Hanson and Ermias Wolde
 * Date: 5/9/2025
 * Purpose: Simple class to handle rounding dollar amounts. 
 */

package com.vgb;

/**
 * Class that handles rounding.
 */
public abstract class Round {
	/**
	 * Rounds the amount to the nearest amount of cents.
	 * 
	 * @param amount
	 * @return
	 */
	public static double toCents(double amount) {
		return Math.round(amount * 100.0) / 100.0;
	}
}
