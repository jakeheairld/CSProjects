package assign06;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class represents a singly linked list.
 * 
 * @author Jake Heairld and Anton Smolyanyy
 * @version Feb 29, 2024
 * 
 * @param <T> - the type of elements contained in the list
 */
public class SinglyLinkedList<T> implements List<T> {

	private int size;
	private Node firstNode;

	/**
	 * This nested class represents a node in the linked list.
	 */
	private class Node extends LinkedListIterator {
		public Node pointer;
		public T data;

		public Node(T data, Node head) {
			this.pointer = head;
			this.data = data;
		}
	}

	/**
	 * Creates a new singly linked list.
	 */
	public SinglyLinkedList() {
		size = 0;
		firstNode = null;
	}

	/**
	 * Inserts an element at the beginning of the list.
	 * 
	 * @param element - the element to add
	 */
	public void insertFirst(T element) {
		Node temp = new Node(element, firstNode);
		firstNode = temp;
		size++;
	}

	/**
	 * Inserts an element at a specific position in the list.
	 * 
	 * @param index - the specified position
	 * @param element - the element to add
	 * @throws IndexOutOfBoundsException if index is out of range (index < 0 || index > size())
	 */
	public void insert(int index, T element) throws IndexOutOfBoundsException {
		if (index > size || index < 0) {
			throw new IndexOutOfBoundsException("Index out of bounds");
		} else if (index == 0) {
			insertFirst(element);
		} else {
			Node temp = firstNode;
			for(int i = 0; i < index - 1; i++) {
				temp = temp.pointer;
			}
			temp.pointer = new Node(element, temp.pointer);
			size++;
		}
	}

	/**
	 * Gets the first element in the list.
	 * 
	 * @return the first element in the list
	 * @throws NoSuchElementException if the list is empty
	 */
	public T getFirst() throws NoSuchElementException {
		if(firstNode == null) {
			throw new NoSuchElementException("There are no such elements");
		}
		return firstNode.data;
	}

	/**
	 * Gets the element at a specific position in the list.
	 * 
	 * @param index - the specified position
	 * @return the element at the position
	 * @throws IndexOutOfBoundsException if index is out of range (index < 0 || index >= size())
	 */
	public T get(int index) throws IndexOutOfBoundsException {
		if (index > size - 1 || index < 0) {
			throw new IndexOutOfBoundsException();
		} else {
			Iterator<T> iter = iterator();
			int counter = 0;
			while(counter < index && iter.hasNext()) {
				iter.next();
				counter++;
			}
			return iter.next();
		}
	} 

	/**
	 * Deletes and returns the first element from the list.
	 * 
	 * @return the first element
	 * @throws NoSuchElementException if the list is empty
	 */
	public T deleteFirst() throws NoSuchElementException {
		if(firstNode == null) {
			throw new NoSuchElementException("There are no such elements");			
		}
		T firstData = firstNode.data;
		firstNode = firstNode.pointer;
		size--;
		return firstData;
	}

	/**
	 * Deletes and returns the element at a specific position in the list.
	 * 
	 * @param index - the specified position
	 * @return the element at the position
	 * @throws IndexOutOfBoundsException if index is out of range (index < 0 || index >= size())
	 */
	public T delete(int index) throws IndexOutOfBoundsException {
		if(index > size - 1 || index < 0) {
			throw new IndexOutOfBoundsException("Index out of bounds");
		} else if(index == 0) {
			return deleteFirst();
		} else {
			Node temp = firstNode;
			for(int i = 0; i < index - 1; i++) {
				temp = temp.pointer;
			}
			T tempData = temp.pointer.data;
			temp.pointer = temp.pointer.pointer;
			size--;
			return tempData;
		}
	}

	/**
	 * Determines the index of the first occurrence of the specified element in the list, 
	 * or -1 if this list does not contain the element.
	 * 
	 * @param element - the element to search for
	 * @return the index of the first occurrence; -1 if the element is not found
	 */
	public int indexOf(T element) {
		Node temp = firstNode;
		for(int i = 0; i < size; i++) {
			if(temp.data.equals(element)) {
				return i;
			}
			temp = temp.pointer;
		}
		return -1;
	}

	/**
	 * Gives the size of the list.
	 * 
	 * @return the number of elements in this list
	 */
	public int size() {
		return size;
	}

	/**
	 * Checks if the list is empty.
	 * 
	 * @return true if this collection contains no elements; false, otherwise
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Removes all of the elements from this list.
	 */
	public void clear() {
		firstNode = null;
		size = 0;
	}

	/**
	 * Generates an array containing all of the elements in this list in proper sequence 
	 * (from first element to last element).
	 * 
	 * @return an array containing all of the elements in this list, in order
	 */
	public Object[] toArray() {
		Object[] arr = new Object[size];
		Node temp = firstNode;
		for(int i = 0; i < size; i++) {
			arr[i] = temp.data;
			temp = temp.pointer;
		}
		return arr;
	}

	/**
	 * Creates an iterator for our linked list.
	 * 
	 * @return an iterator over the elements in this list in proper sequence (from first 
	 * element to last element)
	 */
	public Iterator<T> iterator() {
		return new LinkedListIterator();
	}

	/**
	 * This class represents an iterator for iterating over elements in a linked list.
	 */
	private class LinkedListIterator implements Iterator<T> {

		public Node previousNode;
		public Node activeNode;
		private boolean canRemove;
		public Node p2Node;

		/**
		 * Creates a new linked list iterator.
		 */
		public LinkedListIterator() {
			p2Node = null;
			previousNode = null;
			activeNode = firstNode;
			canRemove = false;
		}

		/**
		 * Checks if there is an element after the current element.
		 * 
		 * @return true if there is a next element, false otherwise.
		 */
		public boolean hasNext() {
			if (activeNode == null) {
				return false;
			}
			return true;
		}

		/**
		 * Moves to the next current element.
		 * 
		 * @return the element we move to.
		 */
		public T next() {
			if (hasNext()) {
				T temp = activeNode.data;
				p2Node = previousNode;
				previousNode = activeNode;
				activeNode = activeNode.pointer;
				canRemove = true;
				return temp;
			} else {
				throw new NoSuchElementException("There are no such elements.");					
			}
		}

		/**
		 * Removes an element from the singly linked list.
		 */
		public void remove() {
			if(canRemove) {
				if(firstNode == previousNode) {
					firstNode = firstNode.pointer;
				} else {
					p2Node.pointer = activeNode;
				}
				canRemove = false;
				p2Node = null;
				activeNode = previousNode.pointer;
				size--;
			} else {
				throw new IllegalStateException("There are no elements to remove.");
			}
		}
	}
}