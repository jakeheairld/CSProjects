package assign10;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class represents a Max Binary Heap.
 * 
 * @author Jake Heairld and Anton Smolyanyy
 * @version April 10, 2024 
 * 
 * @param <E> - The type of items stored in this heap.
 */
public class BinaryMaxHeap<E> implements PriorityQueue<E>{
	
	private E[] array;
	private int size;
	private Comparator<? super E> cmp;
	
	/**
	 * Constructs an empty binary max heap utilizing natural ordering.
	 */
	@SuppressWarnings("unchecked")
	public BinaryMaxHeap() {
		size = 0;
		array = (E[]) new Object[15];
		this.cmp = null;
	}
	
	/**
	 * Constructs an empty binary max heap utilizing a custom comparator.
	 * 
	 * @param cmp - The comparator being used in the heap
	 */
	@SuppressWarnings("unchecked")
	public BinaryMaxHeap(Comparator<? super E> cmp) {
		size = 0;
		array = (E[]) new Object[15];
		this.cmp = cmp;
	}
	
	/**
	 * Constructs a binary max heap containing all elements in the list utilizing natural ordering.
	 * 
	 * @param list - A list of items to be added to the heap
	 */
	@SuppressWarnings("unchecked")
	public BinaryMaxHeap(List<? extends E> list) {
		size = list.size();
		this.cmp = null;
		array = (E[]) new Object[15];
		buildHeap(list);
	}
	
	/**
	 * Constructs a binary max heap containing all elements in the list utilizing a custom comparator.
	 * 
	 * @param list - A list of items to be added to the heap
	 * @param cmp - The comparator being used in this heap
	 */
	@SuppressWarnings("unchecked")
	public BinaryMaxHeap(List<? extends E> list, Comparator<? super E> cmp) {
		size = list.size();
		this.cmp = cmp;
		array = (E[]) new Object[15];
		buildHeap(list);
	}
	
	/**
	 * Adds the given item to this binary max heap.
	 * 
	 * @param item - The item being added to the heap
	 */
	public void add(E item) {
		size++;
		if(size > array.length) {
			growArray();
		}
		array[size-1] = item;
		percolateUp();
	}

	/**
	 * Returns, but does not remove, the maximum item in the heap.
	 * 
	 * @return The maximum item
	 * @throws NoSuchElementException if this binary max heap is empty
	 */
	public E peek() throws NoSuchElementException {
		if(size < 1) {
			throw new NoSuchElementException("There is no value stored in the heap");
		}
		return array[0];
	}

	/**
	 * Returns and removes the maximum item in the heap.
	 * 
	 * @return The maximum item
	 * @throws NoSuchElementException if this binary max heap is empty
	 */
	public E extractMax() throws NoSuchElementException {
		if(size < 1) {
			throw new NoSuchElementException("There is no value stored in the heap");
		}
		E maxItem = array[0];
		removeMax();
		return maxItem;
	}

	/**
	 * Returns the number of items stored in this binary max heap.
	 * 
	 * @return The size of the heap
	 */
	public int size() {
		return this.size;
	}

	/**
	 * Returns true if the heap is empty, false otherwise.
	 * 
	 * @return true if heap is empty, otherwise false
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Empties this binary max heap.
	 */
	public void clear() {
		this.size = 0;
	}

	/**
	 * Creates and returns an array of the items in this binary max heap,
	 * in the same order they appear in the backing array.
	 */
	public Object[] toArray() {
		Object[] temp = new Object[size];
		for(int i = 0; i < size; i++) {
			temp[i] = array[i];
		}
		return temp;
	}
	
	/**
	 * Fills in the backing array for the heap with the elements from the list,
	 * then percolates elements into a binary max heap.
	 * 
	 * @param list - The list of items to be stored in the heap
	 */
	@SuppressWarnings("unchecked")
	private void buildHeap(List<? extends E> list) {
		if(size > 0) {
			while(array.length < size) {
				growArray();
			}
			array = (E[]) list.toArray((E[]) new Object[array.length]);
		}
		for(int i = (array.length-1)/2 - 1; i >= 0; i--) {
			percolateDown(i);
		}
	}
	
	/**
	 * Percolates the last added element of the binary max heap.
	 */
	private void percolateUp() {
		int parentIndex = getParentIndex(this.size-1);
		int childIndex = size-1;
		while(parentIndex >= 0) {
			if(innerCompare(array[childIndex], array[parentIndex]) > 0) {
				swap(childIndex, parentIndex);
				childIndex = parentIndex;
				parentIndex = getParentIndex(parentIndex);
			} else {
				break;
			}
		}
	}
	
	/**
	 * Percolates down each element in the backing array,
	 * starting from the given index to index 0.
	 * 
	 * @param index - The first element index to percolate down
	 */
	private void percolateDown(int index) {
		while(index >= 0 && index < size) {
			if(hasRightChild(index) && hasLeftChild(index)) {
				if(innerCompare(array[getLeftChild(index)], array[index]) > 0 || innerCompare(array[getRightChild(index)], array[index]) > 0) {
					if(innerCompare(array[getLeftChild(index)], array[getRightChild(index)]) > 0) {
						swap(index, getLeftChild(index));
						index = getLeftChild(index);
					} else {
						swap(index, getRightChild(index));
						index = getRightChild(index);
					}
				} else {
					break;
				}
			} else if(hasLeftChild(index)) {
				if(innerCompare(array[getLeftChild(index)], array[index]) > 0) {
					swap(index, getLeftChild(index));
					index = getLeftChild(index);
				} else {
					break;
				}
			} else if(hasRightChild(index)) {
				if(innerCompare(array[getRightChild(index)], array[index]) > 0) {
					swap(index, getRightChild(index));
					index = getRightChild(index);
				} else {
					break;
				}
			} else {
				break;
			}
		}
	}
	
	/**
	 * Removes the maximum value stored in the binary max heap.
	 */
	private void removeMax() {
		swap(0, size-1);
		size--;
		percolateDown(0);
	}
	
	/**
	 * Swaps the elements at two given indices in the binary max heap.
	 * 
	 * @param i1 - The index of the first element to swap
	 * @param i2 - The index of the second element to swap
	 */
	private void swap(int i1, int i2) {
		E temp = array[i1];
		array[i1] = array[i2];
		array[i2] = temp;
	}
	
	/**
	 * Increases the size of the backing array for the binary max heap,
	 * maintains the binary heap structure.
	 */
	@SuppressWarnings("unchecked")
	private void growArray() {
		int newSize = array.length*2 + 1;
		E[] temp = (E[]) new Object[newSize];
		for(int i = 0; i < array.length; i++) {
			temp[i] = array[i];
		}
		this.array = temp;
	}
	
	/**
	 * Isolates the decision of whether to invoke natural ordering
	 * or a custom comparator for comparisons.
	 * 
	 * @param elem1 - The first element being compared
	 * @param elem2 - The second element being compared
	 * @return - The resulting comparison value
	 */
	@SuppressWarnings("unchecked")
	private int innerCompare(E elem1, E elem2) {
		if(cmp == null) {
			return (((Comparable<? super E>) elem1).compareTo(elem2));
		} else {
			return cmp.compare(elem1, elem2);
		}
	}
	
	/**
	 * Returns the index of the given element's left child,
	 * if the element does not have a left child, returns -1.
	 * 
	 * @param parent - The index of the element for whose child you are checking for
	 * @return The index of the left child, or -1 if no child is found
	 */
	private int getLeftChild(int parent) {
		int temp = parent * 2 + 1;
		if(temp >= size) {
			return -1;
		}
		return temp;
	}
	
	/**
	 * Checks if the given element has a left child.
	 * 
	 * @param parent - The index of the element being checked.
	 * @return True if the element has a left child, false otherwise.
	 */
	private boolean hasLeftChild(int parent) {
		return getLeftChild(parent) != -1;
	}
	
	/**
	 * Returns the index of the given element's right child,
	 * if the element does not have a right child, returns -1.
	 * 
	 * @param parent - The index of the element for whose child you are checking for
	 * @return The index of the right child, or -1 if no child is found
	 */
	private int getRightChild(int parent) {
		int temp = parent * 2 + 2;
		if(temp >= size) {
			return -1;
		}
		return temp;
	}
	
	/**
	 * Checks if the given element has a right child.
	 * 
	 * @param parent - The index of the element being checked.
	 * @return True if the element has a right child, false otherwise.
	 */
	private boolean hasRightChild(int parent) {
		return getRightChild(parent) != -1;
	}
	
	/**
	 * Returns the index of the given element's parent,
	 * if the element does not have a parent, returns -1.
	 * 
	 * @param childIndex - The index of the element for whose parent you are checking for
	 * @return The index of the parent, or -1 if no parent is found
	 */
	private int getParentIndex(int childIndex) {
		int temp = (childIndex + 1)/2 - 1;
		if(temp < 0) {
			return -1;
		}
		return temp;
	}
}