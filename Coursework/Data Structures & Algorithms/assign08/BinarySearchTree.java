package assign08;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * This class represents a Binary Search Tree.
 * 
 * @author Jake Heairld and Anton Smolyanyy
 * @version March 21, 2024
 * 
 * @param <Type> - The type of data stored in the BST.
 */
public class BinarySearchTree<Type extends Comparable<? super Type>> implements SortedSet<Type> {

	private BinaryNode<Type> rootNode;
	private int size;
	
	/**
	 * Returns the root element stored of the BST.
	 * 
	 * @return the root element of the BST
	 */
	public Type getRoot() {
		return this.rootNode.getData();
	}
	
	/**
	 * Adds a given element to its correct position in the BST. 
	 * 
	 * @param item - the element to be added 
	 * @return true if the element was added, false if the element already existed in the BST and therefore was not added again
	 */
	public boolean add(Type item) {
		if(rootNode == null) {
			rootNode = new BinaryNode<Type>(item);
			this.size++;
			return true;
		}		
		return addRecursive(item, rootNode);
	}
	

	/**
	 * Private helper method which recursively traverses the BST to add the elements in the correct order.
	 * 
	 * @param item - The item being added to the BST.
	 * @param currentNode - The current node in the tree traversal.
	 */
	private boolean addRecursive(Type item, BinaryNode<Type> currentNode) {
			if(item.compareTo(currentNode.getData()) == 0) { // Checks if the currentNode is a duplicate of the item being added.
				return false;
			} else if(item.compareTo(currentNode.getData()) < 0) { // Move left down the tree
				if(currentNode.getLeftChild() == null) { // If there isn't a node to move to, add the new node.
					BinaryNode<Type> newNode = new BinaryNode<Type>(item);
					currentNode.setLeftChild(newNode);
					newNode.setParent(currentNode);
					this.size++;
					return true;
				} else { // Traverse to the right child.
					return addRecursive(item, currentNode.getLeftChild());
				}
			} else { // Move right down the tree.
				if(currentNode.getRightChild() == null) { // If there isn't a node to move to, add the new node.		
					BinaryNode<Type> newNode = new BinaryNode<Type>(item);
					currentNode.setRightChild(newNode);
					newNode.setParent(currentNode);
					this.size++;
					return true;
				} else { // Traverse to the right child.
					return addRecursive(item, currentNode.getRightChild());
				}
			}
	}

	/**
	 * Adds all the elements in the given collection to their correct positions in the BST. 
	 * 
	 * @param items - the collection of elements to be added 
	 * @return true if the elements were added, false if any of the elements already existed in the BST and therefore were not added again
	 */
	public boolean addAll(Collection<? extends Type> items) {
		for (Type item : items) {
			add(item);
		}
		return true;
	}

	/**
	 * Deletes all elements from the BST. 
	 */
	public void clear() {
		this.rootNode = null;
		this.size = 0;
	}

	/**
	 * Determines if the given element exists in the BST. 
	 * 
	 * @param item - item which is to be located
	 * @return true if the element exists in the BST, false otherwise
	 */
	public boolean contains(Type item) {
		BinaryNode<Type> tempRoot = rootNode;
		if(tempRoot == null) {
			return false;
		}
		while(tempRoot!=null) {
			if(tempRoot.getData().equals(item)) {
				return true;
			} else if(item.compareTo(tempRoot.getData()) < 0) {
				tempRoot = tempRoot.getLeftChild();
			} else {
				tempRoot = tempRoot.getRightChild();
			}
		}
		return false;
	}

	/**
	 * Determines if all elements in the given Collection exist in the BST.
	 * 
	 * @param items - Collection of items which are to be located
	 * @return true if all elements exist in the BST, false otherwise
	 */	public boolean containsAll(Collection<? extends Type> items) {
		for (Type item : items) {
			if (!(contains(item))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the minimum element stored in the BST.
	 * 
	 * @return the minimum element in the BST
	 * @throws NoSuchElementException if the BST is empty
	 */
	public Type first() throws NoSuchElementException {
		if (isEmpty()) {
			throw new NoSuchElementException("This tree holds no elements.");
		}
		return rootNode.getLeftmostNode().getData();
	}

	/**
	 * Determines if the current BST is empty (has no elements).
	 * 
	 * @return true if there are no elements stored in the BST, false if there are 
	 */
	public boolean isEmpty() {
		return this.size == 0;
	}

	/**
	 * Returns the maximum element stored in the BST.
	 * 
	 * @return the maximum element in the BST
	 * @throws NoSuchElementException if the BST is empty
	 */
	public Type last() throws NoSuchElementException {
		if (isEmpty()) {
			throw new NoSuchElementException("This tree holds no elements.");
		}
		return rootNode.getRightmostNode().getData();
	}

	/**
	 * Removes an item from the BST.
	 * 
	 * @param item - The item to be removed.
	 * @return True if the item was removed, false otherwise.
	 */
	public boolean remove(Type item) {
		BinaryNode<Type> tempRoot = rootNode;
		if(tempRoot == null) {
			return false;
		}
		while(tempRoot!=null) {
			if(tempRoot.getData().equals(item)) {
				
				//Removing a leaf node.
				if(tempRoot.getRightChild() == null && tempRoot.getLeftChild() == null) {
					if(tempRoot.getParent() == null) {
						this.rootNode = null;
					} else if(tempRoot.equals(tempRoot.getParent().getLeftChild())) {
						tempRoot.getParent().setLeftChild(null);
						tempRoot.setParent(null);
					} else {
						tempRoot.getParent().setRightChild(null);
						tempRoot.setParent(null);
					}
					
					//Removing node with one child
				} else if(tempRoot.getLeftChild() == null || tempRoot.getRightChild() == null) {
					BinaryNode<Type> child;
					
					if(tempRoot.getLeftChild() != null) {
						child = tempRoot.getLeftChild();
					} else {
						child = tempRoot.getRightChild();
					}
					
					if(tempRoot.getParent() == null) {
						this.rootNode = child;
					} else if(tempRoot.equals(tempRoot.getParent().getLeftChild())) {
						child.setParent(tempRoot.getParent());
						tempRoot.getParent().setLeftChild(child);
					} else {
						child.setParent(tempRoot.getParent());
						tempRoot.getParent().setRightChild(child);
					}

					//Removing node with two children
				} else {
					BinaryNode<Type> successor = tempRoot.getRightChild().getLeftmostNode();
					tempRoot.setData(successor.getData());
					BinaryNode<Type> successorParent = successor.getParent();
					if(successor.getRightChild() == null) {
						successorParent.setLeftChild(null);
					} else {
						successor.getRightChild().setParent(successorParent);
						successorParent.setRightChild(successor.getRightChild());
					} 
				}
				this.size--;
				return true;
			} else if(item.compareTo(tempRoot.getData()) < 0) {
				tempRoot = tempRoot.getLeftChild();
			} else {
				tempRoot = tempRoot.getRightChild();
			}
		}
		return false;
	}

	/**
	 * Removes all items in the collection from the BST.
	 * 
	 * @param items -The items to be removed from the BST.
	 * @return True if anything was removed, false otherwise. 
	 */
	public boolean removeAll(Collection<? extends Type> items) {
		boolean didRemove = false;
		for (Type item : items) {
			if(remove(item)) {				
				didRemove = true;
			}
		}
		return didRemove;
	}

	/**
	 * Returns the size of the BST.
	 * 
	 * @return the number of elements in the BST
	 */
	public int size() {
		return this.size;
	}

	/**
	 * Generates an ArrayList which holds the elements of the BST in sorted order.
	 * 
	 * @return the ArrayList with sorted elements 
	 */
	public ArrayList<Type> toArrayList() {
		ArrayList<Type> treeList = new ArrayList<Type>();
		
		inOrder(treeList, rootNode);
		
		return treeList;
	}
	
	/**
	 * Private helper method which recursively uses inorder traversal of BST to add the elements to the ArrayList in sorted order.
	 * 
	 * @param list - the ArrayList which represents the BST in sorted order
	 * @param tempRoot - the rootNode of the current subtree on each given recursive call 
	 */
	private void inOrder(ArrayList<Type> list, BinaryNode<Type> tempRoot) {
		
		//base case, this means that on the previous iteration the method had reached a leaf 
		if (tempRoot == null) {
			return;
		}
		
		//recursively calls this method on left and right subtrees separately
		inOrder(list, tempRoot.getLeftChild());
		list.add(tempRoot.getData());
		inOrder(list, tempRoot.getRightChild());		
	}

}
