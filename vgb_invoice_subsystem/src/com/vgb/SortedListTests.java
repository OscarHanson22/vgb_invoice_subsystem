/**
 * Authors: Oscar Hanson and Ermias Wolde
 * Date: 5/9/2025
 * Purpose: Unit tests that ensure that the sorted list is always sorted, even during many different operations. 
 */

package com.vgb;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Comparator;

import org.junit.jupiter.api.Test;

public class SortedListTests {
	private static class IntComp implements Comparator<Integer> {
		@Override
		public int compare(Integer i1, Integer i2) {
			return Integer.compare(i1, i2);
		}
	}
	
	private <T> boolean isSorted(SortedList<T> list, Comparator<T> comp) {
		T previous = null;
		
		for (T element : list) {	
			if (previous != null && comp.compare(element, previous) < 0) {
				return false;
			}
			previous = element;
		}
		
		return true;
	}
	
	@Test
	public void sortedListTest001() {
		SortedList<Integer> list = new SortedList<>(new IntComp());
		
		list.add(0);
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
	
		assert(isSorted(list, new IntComp()));
	}
	
	@Test
	public void sortedListTest002() {
		SortedList<Integer> list = new SortedList<>(new IntComp());
		
		list.add(4);
		list.add(3);
		list.add(2);
		list.add(1);
		list.add(0);
	
		assert(isSorted(list, new IntComp()));
	}
	
	@Test
	public void sortedListTest003() {
		SortedList<Integer> list = new SortedList<>(new IntComp());
		
		list.add(10000);
		list.add(4);
		list.add(2);
		list.add(55);
		list.add(100);
		list.add(10000);
		list.add(-1);
		list.add(100);
		list.add(-2);
		list.add(50);
				
		assertEquals(list.get(0), -2);
		assertEquals(list.get(9), 10000);
	
		assert(isSorted(list, new IntComp()));
	}
	
	@Test
	public void sortedListTest004() {
		SortedList<Integer> list = new SortedList<>(new IntComp());
		
		list.add(10000);
		list.add(4);
		list.add(2);
		list.add(55);
		list.add(100);
		list.add(10000);
		list.add(-1);
		list.add(100);
		list.add(-2);
		list.add(50);
		
		assertEquals(list.remove(0), -2);
		assertEquals(list.remove(2), 4);
	
		assert(isSorted(list, new IntComp()));
	}
}
