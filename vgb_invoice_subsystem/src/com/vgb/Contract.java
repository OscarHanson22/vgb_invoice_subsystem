/**
 * Authors: Oscar Hanson and Ermias Wolde
 * Date: 5/9/2025
 * Purpose: Object representation of a contract.
 * 
 */

package com.vgb;

import java.util.UUID;

/**
 *  Represents a contract with another company. 
 */
public class Contract extends Item {
	private Company subcontractor;
	private double amount;

	/**
	 * Creates and returns a Contract with the specified information. 
	 * 
	 * @param uuid
	 * @param name
	 * @param subcontractor
	 */
	public Contract(UUID uuid, String name, Company subcontractor) {
		super(uuid, name);
		this.subcontractor = subcontractor;
		this.amount = 0.0;
	}
	
	/**
	 * Creates a contract with the specified amount.
	 * 
	 * @param fromContract
	 * @param amount
	 */
	public Contract(Contract fromContract, double amount) {
		super(fromContract.getUuid(), fromContract.getName());
		this.subcontractor = fromContract.getSubcontractor();
		this.amount = amount;
	}
	
	/**
	 * Returns the subcontractor of this contract.
	 * 
	 * @return
	 */
	public Company getSubcontractor() {
		return subcontractor;
	}

	/**
	 * Returns the amount this contract is worth.
	 * 
	 * @return
	 */
	public double getAmount() {
		return amount;
	}

	@Override 
	public Total getTotal() {
		return new Total(amount, 0.0);
	}

	@Override
	public String toString() {
		return super.toString() + " | Amount: $" + String.format("%.2f", amount);
	}
}
