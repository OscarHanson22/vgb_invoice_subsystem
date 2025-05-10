/**
 * Authors: Oscar Hanson and Ermias Wolde
 * Date: 5/9/2025
 * Purpose: List data structure that is always sorted.
 */

package com.vgb;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A list that is always sorted in ascending order with default comparators. 
 * Sorts in descending order with reversed comparators.
 * 
 * Uses a linked list internally. 
 * 
 * @param <T> The type of the item of the list.
 */
public class SortedList<T> implements Iterable<T> {
	final private Comparator<T> comparator;
	private LinkedListNode<T> head;
	private int size;
	
	/**
	 * Creates a new sorted list with the specified comparator.
	 * 
	 * @param comparator The comparator used to sort the list.
	 * @throws IllegalArgumentException If the comparator is null.
	 */
	public SortedList(Comparator<T> comparator) {
		if (comparator == null) {
			throw new IllegalArgumentException("Comparator cannot be null");
		}
		this.comparator = comparator;
		this.head = null; 
		this.size = 0;
	}
	
	/**
	 * Creates and populates a sorted list with the given elements.
	 * 
	 * @param comparator The comparator used to sort the list.
	 * @param elements The elements used to populate the list.
	 * @throws IllegalArgumentException If the comparator is null.
	 */
	public SortedList(Comparator<T> comparator, List<T> elements) {
		this(comparator);
		for (T element : elements) {
			add(element);
		}
	}
	
	/**
	 * Adds all of the specified elements to the sorted list.
	 * 
	 * @param elements The elements to be added to the list.
	 * @throws IllegalArgumentException If any element in elements is null.
	 */
	public void addElements(List<T> elements) {
		for (T element : elements) {
			add(element);
		}
	}
	
	/**
	 * Adds an element to the sorted list without reordering it.
	 * 
	 * @param element The element to add.
	 * @throws IllegalArgumentException When element is null.
	 */
	public void add(T element) {
		if (element == null) {
			throw new IllegalArgumentException("Cannot add a null element to the sorted list.");
		}
		
		LinkedListNode<T> nodeToInsert = new LinkedListNode<T>(element);
		
		if (head == null) {
			head = nodeToInsert;
			size++;
			return;
		}
		
		LinkedListNode<T> previous = null;
		LinkedListNode<T> current = head;
		boolean firstIteration = true;
				
		while (current != null) {
			if (comparator.compare(element, current.getData()) < 0) {
				nodeToInsert.setNext(current);
				if (firstIteration) {
					head = nodeToInsert;
				}
				if (previous != null) {
					previous.setNext(nodeToInsert);
				}
				size++;
				return;
			}
			
			previous = current;
			current = current.getNext();
			firstIteration = false;
		}
		
		previous.setNext(nodeToInsert);
		size++;
	}
	
	/**
	 * Returns the size of the linked list.
	 * 
	 * @return
	 */
	public int size() {	
		return size;
	}
	
	/**
	 * Returns true if the linked list has no nodes and false otherwise.
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return head == null;
	}
	
	/**
	 * Returns the value of the node at index <code>fromIndex</code>.
	 * Does not mutate the list. 
	 * 
	 * @param fromIndex The index from which to retrieve the data.
	 * @throws IndexOutOfBoundsException If the index is out of bounds.
	 * @return
	 */
	public T get(int fromIndex) {
		if (fromIndex < 0 || fromIndex >= size) {
			throw new IndexOutOfBoundsException("Index: " + fromIndex + " is out of bounds 0 to " + size);
		} 
		
		LinkedListNode<T> current = head;
		int index = 0;
		
		while (current != null) {
			if (index == fromIndex) {
				return current.getData();
			}	
			current = current.getNext();
			index++;
		}
		
		return null;
	}
	
	/**
	 * Removes and returns the element at the index <code>atIndex</code>.
	 * 
	 * @param atIndex The index of the element to remove.
 	 * @throws IndexOutOfBoundsException If the index is out of bounds.
	 * @return
	 */
	public T remove(int atIndex) {
		if (atIndex < 0 || atIndex >= size) {
			throw new IndexOutOfBoundsException("Index: " + atIndex + " is out of bounds 0 to " + size);
		} 
				
		if (atIndex == 0) {
			T data = head.getData();
			if (!head.hasNext()) {
				head = null;
			} else {
				head = head.getNext();
			}
			size--;
			return data;
		}
		
		LinkedListNode<T> previous = head;
		LinkedListNode<T> current = head.getNext();
		int index = 1;
		
		while (current != null) {
			if (index == atIndex) {
				previous.setNext(current.getNext());
				size--;
				return current.getData();
			}
			previous = current;
			current = current.getNext();
			index++;
		}
		
		return null;
	}
	
	/**
	 * Inner method used for adding to the sorted list.
	 * 
	 * Inserts <code>data</code> at the index <code>atIndex</code>. 
	 * 
	 * If the linked list has no nodes and the atIndex is 0 then it will insert the data at the head.
	 * If the atIndex references the null pointed to by the last node, it will still insert the data as the new tail of the list.
	 * Otherwise if the index is out of the bounds of the list, nothing will be inserted.
	 * 
	 * @param data The data to be inserted.
	 * @param atIndex The index at which to insert.
	 * @return
	 */
	private boolean insert(T data, int atIndex) {
		LinkedListNode<T> nodeToInsert = new LinkedListNode<T>(data);

		if (atIndex < 0 || atIndex > size) {
			return false;
		}
		
		if (atIndex == 0) {
			nodeToInsert.setNext(head);
			head = nodeToInsert;
			size++;
			return true;
		}
				
		LinkedListNode<T> previous = head;
		LinkedListNode<T> current = head.getNext();
		int index = 1;
		
		while (current != null) {
			if (index == atIndex) {
				nodeToInsert.setNext(current);
				previous.setNext(nodeToInsert);
				size++;
				return true;
			}
			
			previous = current;
			current = current.getNext();
			index++;
		}
		
		// append to the end (the null at the end of the list)
		if (index == atIndex) {
			previous.setNext(nodeToInsert);
			size++;
			return true;
		}
		
		return false;
	}
	
	/**
	 * Creates a string representation of the linked list.
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
			
		for (T item : this) {
			string.append(item).append(" -> ");
		}
			
		string.append("(null)");
		
		return string.toString();
	}
	
	/** 
	 * Returns an iterator over the values of the linked list.
	 * 
	 * @return
	 */
	public Iterator<T> iterator() {
		return new SortedListIterator<T>(head);
	}
	
	/**
	 * An iterator over the values of the linked list.
	 */
	private static class SortedListIterator<T> implements Iterator<T> {
		private LinkedListNode<T> current;
		
		/**
		 * Creates a linked list iterator starting on <code>head</code>.
		 * 
		 * @param head The head of the linked list. 
		 */
		private SortedListIterator(LinkedListNode<T> head) {
			this.current = head;
		}
		
		/**
		 * Returns true if the iterator has another element and false if it does not. 
		 * @return
		 */
		@Override
		public boolean hasNext() {
			return current != null; 
		}
		
		/**
		 * Returns the value of the next element in the iterator, consuming the value and progressing the iterator.
		 * @return
		 */
		@Override 
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			
			T data = current.getData();
			current = current.getNext();
			
			return data;
		}
	}
	
	/**
	 * A node in the linked list.
	 */
	private static class LinkedListNode<T> {
		private T data;
		private LinkedListNode<T> next;
		
		/**
		 * Creates a linked list node with the specified data.
		 * 
		 * @param data The data the linked list node is storing (the same as the linked list).
		 */
		private LinkedListNode(T data) {
			this.data = data;
			this.next = null;
		}
		
		/**
		 * Returns true if the linked list node has a <code>next</next>.
		 * 
		 * @return
		 */
		private boolean hasNext() {
			return this.next != null;
		}
		
		/**
		 * Returns the linked list node's next node or child node.
		 * 
		 * @return
		 */
		private LinkedListNode<T> getNext() {
			return this.next;
		}
		
		/**
		 * Sets the next node or child node of the linked list nod.
		 * 
		 * @param next The next node or child node.
		 */
		private void setNext(LinkedListNode<T> next) {
			this.next = next;
		}
		
		/**
		 * Returns the <code>data</code> of the linked list node.
		 * 
		 * @return
		 */
		private T getData() {
			return this.data;
		}
		
		/**
		 * Returns a string representation of the linked list node.
		 * 
		 * @return 
		 */
		@Override 
		public String toString() {
			return this.data.toString(); 
		}
	}
}
