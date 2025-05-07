package com.vgb;

public class RowFormat {
	private int[] widths;
	private String separator;
	
	public RowFormat(int[] widths, String separator) {
		this.widths = widths;
		this.separator = separator;
	}
	
	public int columns() {
		return widths.length;
	}

	public int width(int atIndex) {
		return widths[atIndex];
	}
	
	public String separator() {
		return separator;
	}
}