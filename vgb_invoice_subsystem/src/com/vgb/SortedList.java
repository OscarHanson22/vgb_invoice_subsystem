package com.vgb;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;

/**
 * A list that is always sorted in ascending order with default comparators. 
 * 
 * Uses a linked list internally. 
 * 
 * @param <T> The type of the item of the list.
 */
public class SortedList<T> implements Iterable<T> {
	private LinkedList<T> list;
	final private Comparator<T> comparator;

	/**
	 * Creates a new sorted list with the specified comparator.
	 * 
	 * @param comparator 
	 */
	public SortedList(Comparator<T> comparator) {
		this.list = new LinkedList<T>(); 
		this.comparator = comparator;
	}
	
	/**
	 * Adds an element to the sorted list without reordering it.
	 * 
	 * @param element
	 */
	public void add(T element) {		
		Iterator<T> listIter = list.iterator();
		int index = 0;
		
		while (listIter.hasNext()) {
			T nextElement = listIter.next();
			
			if (comparator.compare(element, nextElement) < 0) {
				list.insert(element, index);
				return;
			}
			
			index++;
		}
		
		list.insert(element, index);
	}
	
	/**
	 * Finds and returns the element at the specified index (if it exists).
	 * 
	 * @param fromIndex
	 * @return
	 */
	public Optional<T> get(int fromIndex) {
		return list.get(fromIndex);
	}
	
	/**
	 * Finds, removes, and returns the element at the specified index (if it exists).
	 * 
	 * @param atIndex
	 * @return
	 */
	public Optional<T> remove(int atIndex) {
		return list.remove(atIndex);
	}
	
	/**
	 * Returns an iterator over the values of the sorted list.
	 */
	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}
	
	public static void main(String[] args) {
		SortedList<Integer> sl = new SortedList<>(new IntComp());
		System.out.println(sl.list);
		sl.add(5);
		System.out.println(sl.list);
		sl.add(10);
		System.out.println(sl.list);
		sl.add(100);
		System.out.println(sl.list);
		sl.add(-3);
		System.out.println(sl.list);
		sl.add(20);
		System.out.println(sl.list);
		sl.add(21);
		System.out.println(sl.list);
		sl.remove(2);
		System.out.println(sl.list);
		
		for (int i : sl) {
			System.out.println(i);
		}
		
	}
	
	static class IntComp implements Comparator<Integer> {
	    @Override
	    public int compare(Integer i1, Integer i2) {
	        return Integer.compare(i2, i1);
	    }
	}
}
