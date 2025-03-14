package com.vgb;

import java.util.UUID;

// Represents a contract with another company. 
public class Contract extends Item {
	private UUID companyUuid;

	// Creates and returns a Contract with the specified information. 
	public Contract(UUID uuid, String name, UUID companyUuid) {
		super(uuid, name);
		this.companyUuid = companyUuid;
	}

	@Override
	public String toString() {
		return "Contract [companyUuid=" + companyUuid + ", " + super.toString() + "]";
	}
}
