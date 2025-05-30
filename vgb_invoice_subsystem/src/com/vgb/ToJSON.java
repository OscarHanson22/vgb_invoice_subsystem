/**
 * Authors: Oscar Hanson and Ermias Wolde
 * Date: 5/9/2025
 * Purpose: Class that generates JSON strings of objects in the subsystem. 
 */

package com.vgb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Provides a formatter to serialize objects into JSON. 
 */
public class ToJSON {
	private Gson configured_gson;
	
	/**
	 * Instantiates a JSON formatter that can call `toJSON`. 
	 */
	public ToJSON() {
		configured_gson = new GsonBuilder()
			.setPrettyPrinting()
			.create();
	}
	
	/**
	 * Returns a JSON string from the passed `object`. 
	 * 
	 * @param <T>
	 * @param object
	 * @return
	 */
	public <T> String toJSON(T object) {
		return configured_gson.toJson(object);
	}
}