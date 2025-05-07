package com.vgb;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * A generic linked list implementation that supports iteration. 
 * 
 * @param <T> The item the linked list holds. 
 */
public class LinkedList<T> implements Iterable<T> {
	private LinkedListNode<T> head;
	private int size;
	
	/**
	 * Creates an empty linked list.
	 */
	public LinkedList() {
		head = null;
		size = 0;
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
	 * Returns the value of the node (if it exists) at index <code>fromIndex</code>.
	 * 
	 * If the index is outside the bounds of the list, an empty Optional is always returned.
	 * Does not mutate the list. 
	 * 
	 * @param fromIndex The index from which to retrieve the data.
	 * @return
	 */
	public Optional<T> get(int fromIndex) {
		LinkedListNode<T> current = head;
		int index = 0;
		
		while (current != null) {
			if (index == fromIndex) {
				return Optional.of(current.getData());
			}	
			current = current.getNext();
			index++;
		}
		
		return Optional.empty();
	}
	
//	private Optional<LinkedListNode<T>> getPreviousNode(int ofIndex) {
//		if (ofIndex < 0 || ofIndex > size) {
//			return Optional.empty();
//		}
//		
//		Optional<LinkedListNode<T>> previous = Optional.empty();
//		LinkedListNode<T> current = head;
//		int index = 0;
//		
//		while (current != null && index < ofIndex) {
//			previous = Optional.of(current);
//			current = current.getNext();
//			index++;
//		}
//		
//		return previous;
//	}
	
	/**
	 * Removes and returns the element (if it exists) at the index <code>atIndex</code>.
	 * 
	 * If the index is outside the bounds of the list, an empty Optional is always returned.
	 * 
	 * @param atIndex The index of the element to remove.
	 * @return
	 */
	public Optional<T> remove(int atIndex) {
		if (atIndex < 0 || atIndex >= size) {
			return Optional.empty();
		} 
				
		if (atIndex == 0) {
			if (head == null) {
				return Optional.empty();
			} else {
				T data = head.getData();
				if (!head.hasNext()) {
					head = null;
				} else {
					head = head.getNext();
				}
				size--;
				return Optional.of(data);
			}
		}
		
		if (head == null) {
			return Optional.empty();
		}
		
		LinkedListNode<T> previous = head;
		LinkedListNode<T> current = head.getNext();
		int index = 1;
		
		while (current != null) {
			if (index == atIndex) {
				previous.setNext(current.getNext());
				size--;
				return Optional.of(current.getData());
			}
			previous = current;
			current = current.getNext();
			index++;
		}
		
		return Optional.empty();
	}
	
	/**
	 * Inserts <code>data</code> at the index <code>atIndex</code>. 
	 * 
	 * If the linked list has no nodes and the atIndex is 0 then it will insert the data at the head.
	 * If the atIndex references the null pointed to by the last node, it will still insert the data.
	 * Otherwise if the index is out of the bounds of the list, nothing will be inserted.
	 * 
	 * @param data The data to be inserted.
	 * @param atIndex The index at which to insert.
	 * @return
	 */
	public boolean insert(T data, int atIndex) {
		LinkedListNode<T> nodeToInsert = new LinkedListNode<T>(data);

		if (atIndex < 0 || atIndex > size) {
			return false;
		}
		
		if (atIndex == 0) {
			if (head == null) {
				head = nodeToInsert;
			} else {
				nodeToInsert.setNext(head);
				head = nodeToInsert;
			}
			size++;
			return true;
		}
		
		if (head == null) {
			return false;
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
	 * Creates a string to represent the linked list.
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
	
	public static void main(String[] args) {
		LinkedList<Integer> list = new LinkedList<>();
//		System.out.println(list.getPreviousNode(1));

		System.out.println(list);
		System.out.println(list.size());
		for (int i = 0; i < 10; i++) {
			list.insert(i, i);
			System.out.println(list);
			System.out.println(list.size());
		}
		
		list.insert(11, 5);
		System.out.println(list);
		System.out.println(list.insert(0, -1));
		
//		System.out.println(list);
//		System.out.println(list.remove(-1));
//		System.out.println(list);
		System.out.println(list.remove(0));
		System.out.println(list);
		System.out.println(list.get(9));
		System.out.println(list);
		
//		System.out.println(list.getPreviousNode(10));
//		System.out.println(list.remove(3));
//		System.out.println(list);
		
//		Iterator<Integer> listIter = list.iterator();
//		
//		while (listIter.hasNext()) {
//			int data = listIter.next();
//			System.out.println(data);
//			System.out.println("Done");
//		}
//		list.insert(1, 0);
//		list.insert(2, 1);
//		list.insert(3, 2);
//		list.insert(4, 3);
//		list.insert(5, 4);
//		System.out.println(list.insert(6, 5));
////		list.addToEnd(1);
////		list.addToEnd(1000);
//		
//Z		
//
//		list.print();
//		list.addToEnd(2);
//		list.print();

		
	}
	
	/** 
	 * Returns an iterator over the values of the linked list.
	 * 
	 * @return
	 */
	public Iterator<T> iterator() {
		return new LinkedListIterator<T>(head);
	}
	
	/**
	 * An iterator over the values of the linked list.
	 */
	private static class LinkedListIterator<T> implements Iterator<T> {
		private LinkedListNode<T> current;
		
		/**
		 * Creates a linked list iterator starting on <code>head</code>.
		 * 
		 * @param head The head of the linked list. 
		 */
		private LinkedListIterator(LinkedListNode<T> head) {
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
		 * @return
		 */
		private boolean hasNext() {
			return this.next != null;
		}
		
		/**
		 * Returns the linked list node's next node or child node.
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
		 * @return
		 */
		private T getData() {
			return this.data;
		}
		
		@Override public String toString() { return this.data.toString(); }
	}
}