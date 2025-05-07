package com.vgb;

public class RowFormatter {
	private RowFormat format;
	private String[] items;
	
	public RowFormatter(RowFormat format, String[] items) {	
		if (format.columns() != items.length) {
			throw new IllegalArgumentException("The amount of columns must match the amount of items. Amount of format columns: " + format.columns() + ", Amount of items: " + items.length + ".");
		}
		
		this.format = format;
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
			sb.append(formatAndTruncate(item, format.width(widthIndex)));
			sb.append(format.separator());
			widthIndex++;
		}
		
		return sb.toString();
	}
	
	public static void main(String[] args) {
		System.out.println("Here in RowFormatter.");
		
//		for (int i = 1; i < 30; i++) {
//			System.out.println("|" + formatAndTruncate("1", i) + "|");
//		}
		int[] widths = {14, 15, 7, 20};
		RowFormat format = new RowFormat(widths, " ");
		
		String[] items = {"Something", "Another", "So help us", "God i guess"};
		System.out.println(new RowFormatter(format, items));
		String[] items2 = {"else", "maybe this isn't what i was looking for", "is what i", "was looking for"};
		System.out.println(new RowFormatter(format, items2));

	}
}
