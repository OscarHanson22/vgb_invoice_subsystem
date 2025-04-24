package com.vgb;

import java.util.List;

public class RowFormatter {
	private int[] widths;
	private String[] items;
	
	public RowFormatter(int[] widths) {	
		this.widths = widths;
		this.items = null;
	}
	
	public RowFormatter(RowFormatter formatter, String[] items) {
		if (formatter.widths.length != items.length) {
			throw new IllegalArgumentException("The lengths of `formatter.widths` and `items` must be equal: `formatter.widths` length: " + widths.length + ", `items` length: " + items.length + ".");
		}
		
		this.widths = formatter.widths;
		this.items = items;
	}
	
	private static String formatAndTruncate(String string, int width) {	
		if (width < 1) {
			throw new IllegalArgumentException("Argument `width` must be greater than or equal to 1.");
		}
		
		if (width <= 10) {
			int min = Math.min(string.length(), width);
			string = string.substring(0, min);
		} else if (string.length() > width) {
			string = string.substring(0, width - 3);
			string = string.concat("...");
		}
		
		return String.format("%-" + width + "s", string);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		int widthIndex = 0;
		for (String item : items) {
			sb.append(formatAndTruncate(item, widths[widthIndex]));
			sb.append(" | ");
			widthIndex++;
		}
		
		return sb.toString();
	}
	
	public static void main(String[] args) {
		System.out.println("Here in RowFormatter.");
		
//		for (int i = 1; i < 30; i++) {
//			System.out.println("|" + formatAndTruncate("1", i) + "|");
//		}
		int[] widths = {7, 10, 7, 20};
		RowFormatter format = new RowFormatter(widths);
		
		String[] items = {"Something", "Another", "So help us", "God i guess"};
		System.out.println(new RowFormatter(format, items));
		String[] items2 = {"else", "maybe this", "is what i", "was looking for"};
		System.out.println(new RowFormatter(format, items2));

	}
}
