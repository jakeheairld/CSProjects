package assign06;

import java.util.NoSuchElementException;

/**
 * This class represents a linked list stack.
 * 
 * @author Jake Heairld and Anton Smolyanyy
 * @version Feb 29, 2024
 * 
 * @param <T> - the type of elements contained in the stack
 */
public class LinkedListStack<T> implements Stack<T> {

	private SinglyLinkedList<T> backingList;
	
	/**
	 * Creates a linked list stack.
	 */
	public LinkedListStack() {
		this.backingList = new SinglyLinkedList<T>();
	}
	
	/**
	 * Removes all the elements from the stack.
	 */
	public void clear() {
		backingList.clear();
	}

	/**
	 * Checks if the stack is currently empty.
	 * 
	 * @return true if the stack contains no elements; false, otherwise.
	 */
	public boolean isEmpty() {
		return backingList.isEmpty();
	}

	/**
	 * Returns, but does not remove, the element at the top of the stack.
	 * 
	 * @return the element at the top of the stack
	 * @throws NoSuchElementException if the stack is empty
	 */
	public T peek() throws NoSuchElementException {
		if (backingList.isEmpty()) {
			throw new NoSuchElementException("Stack is empty.");
		}
		return backingList.getFirst();
	}

	/**
	 * Returns and removes the item at the top of the stack.
	 * 
	 * @return the element at the top of the stack
	 * @throws NoSuchElementException if the stack is empty
	 */
	public T pop() throws NoSuchElementException {
		if (backingList.isEmpty()) {
			throw new NoSuchElementException("Stack is empty.");
		}
		T lastElem = backingList.deleteFirst();
		return lastElem;
	}

	/**
	 * Adds a given element to the stack, putting it at the top of the stack.
	 * 
	 * @param element - the element to be added
	 */
	public void push(T element) {
		backingList.insertFirst(element);
	}

	/**
	 * @return the number of elements in the stack
	 */
	public int size() {
		return backingList.size();
	}

}