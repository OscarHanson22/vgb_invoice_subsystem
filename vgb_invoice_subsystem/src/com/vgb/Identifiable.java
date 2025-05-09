package com.vgb;

import java.util.UUID;

public class Identifiable {
	private UUID uuid;
	
	public Identifiable(UUID uuid) {
		this.uuid = uuid;
	}
	
	public UUID getUuid() {
		return uuid;
	}
}