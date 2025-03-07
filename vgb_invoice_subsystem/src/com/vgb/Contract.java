package com.vgb;

// Represents a contract with another company. 
public class Contract extends Item {
	private String companyUuid;

	// Creates and returns a Contract with the specified information. 
	public Contract(String uuid, String name, String companyUuid) {
		super(uuid, name);
		this.companyUuid = companyUuid;
	}

	@Override
	public String toString() {
		return "Contract [companyUuid=" + companyUuid + ", " + super.toString() + "]";
	}
}
