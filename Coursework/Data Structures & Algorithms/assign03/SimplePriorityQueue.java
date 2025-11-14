package assign03;

import java.util.Collection;
import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * A simple priority queue that can accommodate any type of item,
 * access is limited to the "highest priority" item.
 * 
 * @author Jake Heairld and Anton Smolyanyy
 * @version Jan 30, 2024
 * 
 * @param <E> - The type of objects stored in the SimplePriorityQueue.
 */
public class SimplePriorityQueue<E> implements PriorityQueue<E>{
	
	private E[] backingArray;
	private int size;
	private Comparator<? super E> cmp;
	
	/**
	 * Simple Constructor for the priority cue organized in natural order.
	 */
	@SuppressWarnings("unchecked")
	public SimplePriorityQueue() {
		backingArray = (E[]) new Object[10];
		size = 0;
		this.cmp = null;
	}
	
	/**
	 * Constructor for the priority cue organized in custom order.
	 * 
	 * @param cmp - The comparator and custom ordering used in this queue.
	 */
	@SuppressWarnings("unchecked")
	public SimplePriorityQueue(Comparator<? super E> cmp) {
		backingArray = (E[]) new Object[10];
		size = 0;
		this.cmp = cmp;
	}
	
	/**
	 * Finds the "max" item in the array.
	 * 
	 * @return The max item in the array.
	 * @throws NoSuchElementException if the priority queue is empty.
	 */
	public E findMax() throws NoSuchElementException {
		if(size == 0) {
			throw new NoSuchElementException("Nothing stored in queue.");
		} else {
			return backingArray[size-1];
		}
	}

	/**
	 * Retrieves and removes the max item from the array.
	 * 
	 * @return The max item in the array.
	 * @throws NoSuchElementException if the priority queue is empty.
	 */
	public E deleteMax() throws NoSuchElementException {
		if(size == 0) {
			throw new NoSuchElementException("Nothing stored in queue.");
		} else {	
			size--;
			return backingArray[size];
		}          
	}

	/**
	 * Inserts an item into the priority queue.
	 * 
	 * @param item - The item to insert be inserted.
	 */
	@SuppressWarnings("unchecked")
	public void insert(E item) {
		if(size + 1 > backingArray.length) {
			E[] tempArr = backingArray;
			backingArray = (E[]) new Object[tempArr.length*2];
			for(int i = 0; i<tempArr.length; i++) {
				backingArray[i] = tempArr[i];
			}
		}
		int index = binarySearch(item);
		for(int i = size; i > binarySearch(item); i--) {
				backingArray[i] = backingArray[i-1];
		}
		backingArray[index] = item;
		size++;
	}

	/**
	 * Inserts all of the items in a collection into the priority queue.
	 * 
	 * @param coll - The collection of items to be inserted.
	 */
	public void insertAll(Collection<? extends E> coll) {
		for(E elem : coll) {
			insert(elem);
		}
	}

	/**
	 * Indicates whether this priority queue contains the specified item.
	 * 
	 * @param item - The item thats containment is being checked.
	 * @return True if the item is contained in this priority queue
	 */
	public boolean contains(E item) {		
		if(binarySearch(item) >= size) {
			return false;
		}
		if(size > 0 && backingArray[binarySearch(item)].equals(item)) {
			return true;
		}
		return false;
	}

	/**
	 * Retrieves the size of the queue.
	 * 
	 * @return The number of items in this priority queue.
	 */
	public int size() {
		return size;
	}

	/**
	 * Checks if the queue is empty.
	 * 
	 * @return true if the queue is empty, false otherwise.
	 */
	public boolean isEmpty() {
		if(size == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Clears the queue.
	 */
	public void clear() {
		size = 0;
	}
	
	/**
	 * Searches the queue for an item and returns that index if found,
	 * otherwise returns the farthest right index.
	 * 
	 * @param tar
	 * @return The index of the target item if found,
	 * otherwise the farthest right index is returned.
	 */
	@SuppressWarnings("unchecked")
	private int binarySearch(E tar) {
		if(size == 0) {
			return 0;
		}
		int left = 0;
		int right = size-1;
		int center;
		while(left <= right) {
			center = left + (right-left)/2;
			if(backingArray[center].equals(tar)) {
				return center;
			}
			if(cmp == null) {
				if(((Comparable<? super E>)backingArray[center]).compareTo(tar) > 0) {
					right = center - 1;
				} else {
					left = center + 1;
				}
			} else {
				if(cmp.compare(backingArray[center], tar) > 0) {
					right = center - 1;
				} else {
					left = center + 1;
				}
			}
		}
		return left;
	}
}