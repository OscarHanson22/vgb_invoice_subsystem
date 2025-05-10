/**
 * Authors: Oscar Hanson and Ermias Wolde
 * Date: 5/9/2025
 * Purpose: Unit tests that ensure that the sorted list is always sorted, even during many different operations. 
 */

package com.vgb;

import java.util.UUID;

/**
 * Represents the basic information of an item in the invoice subsystem. 
 */
public abstract class Item extends Identifiable {
	private String name;
	
	/**
	 * Creates and returns an Item from the specified information. 
	 * 
	 * @param uuid
	 * @param name
	 */
	public Item(UUID uuid, String name) {
		super(uuid);
		this.name = name;
	}
	
	/**
	 * Returns the name of the item.
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * A method to be implemented by item's subclasses.
	 * 
	 * @return
	 */
	public abstract Total getTotal();

	@Override
	public String toString() {
		return name + " (" + getUuid() + ")";
	}
}