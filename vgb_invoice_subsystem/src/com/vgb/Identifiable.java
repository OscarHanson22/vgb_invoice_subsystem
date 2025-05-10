/**
 * Authors: Oscar Hanson and Ermias Wolde
 * Date: 5/9/2025
 * Purpose: Object that represents something in the subsystem (i.e. this something has a UUID).
 */

package com.vgb;

import java.util.UUID;

/**
 * A class to represent something in the subsystem.
 */
public class Identifiable {
	private UUID uuid;
	
	/**
	 * Returns a new identifiable with the specified UUID.
	 * 
	 * @param uuid
	 */
	public Identifiable(UUID uuid) {
		this.uuid = uuid;
	}
	
	/**
	 * Returns this object's UUID.
	 * 
	 * @return
	 */
	public UUID getUuid() {
		return uuid;
	}
}