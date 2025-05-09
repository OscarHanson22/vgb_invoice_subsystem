package com.vgb;

import com.thoughtworks.xstream.XStream;

// Provides a formatter to serialize objects into XML. 
public class ToXML {
	private XStream configured_xstream;
	
	// Instantiates an XML formatter that can call `toXML`. 
	public ToXML() {
		configured_xstream = new XStream();
		configured_xstream.alias("person", Person.class);
		configured_xstream.alias("company", Company.class);
		configured_xstream.alias("address", Address.class);
		configured_xstream.alias("material", Material.class);
		configured_xstream.alias("equipment", Equipment.class);
		configured_xstream.alias("contract", Contract.class);
	}
	
	// Returns an XML string from the passed `object`. 
	public <T> String toXML(T object) {
		return configured_xstream.toXML(object);
	}
}
