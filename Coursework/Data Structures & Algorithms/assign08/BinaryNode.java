package assign08;

/**
 * This class represents a Binary Node.
 * 
 * @author Jake Heairld and Anton Smolyanyy
 * @version March 21, 2024
 * 
 * @param <Type> - The type of data stored in a Binary Node.
 */
public class BinaryNode<Type> {

	private Type data;	
	private BinaryNode<Type> parent;
	private BinaryNode<Type> leftChild;
	private BinaryNode<Type> rightChild;
	
	/**
	 * Constructor for BinaryNode with data and children.
	 * 
	 * @param data - The data stored in the node.
	 * @param leftChild - The left child of this node.
	 * @param rightChild - The right child of this node.
	 */
	public BinaryNode(Type data, BinaryNode<Type> leftChild, BinaryNode<Type> rightChild) {
		this.data = data;
		this.parent = null;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}

	/**
	 * Constructor for BinaryNode with data.
	 * 
	 * @param data - The data stored in the node.
	 */
	public BinaryNode(Type data) {
		this(data, null, null);
	}
	
	/**
	 * Retrieves the parent node of this node.
	 * 
	 * @return The parent node.
	 */
	public BinaryNode<Type> getParent(){
		return this.parent;
	}
	
	/**
	 * Sets the parent node of this node.
	 * 
	 * @param parent - The parent node.
	 */
	public void setParent(BinaryNode<Type> parent) {
		this.parent = parent;
	}

	/**
	 * @return the node data
	 */
	public Type getData() {
		return data;
	}

	/**
	 * @param data - the node data to be set
	 */
	public void setData(Type data) {
		this.data = data;
	}

	/**
	 * @return reference to the left child node
	 */
	public BinaryNode<Type> getLeftChild() {
		return leftChild;
	}

	/**
	 * @param leftChild - reference of the left child node to be set
	 */
	public void setLeftChild(BinaryNode<Type> leftChild) {
		this.leftChild = leftChild;
	}

	/**
	 * @return reference to the right child node
	 */
	public BinaryNode<Type> getRightChild() {
		return rightChild;
	}

	/**
	 * @param rightChild - reference of the right child node to be set
	 */
	public void setRightChild(BinaryNode<Type> rightChild) {
		this.rightChild = rightChild;
	}

	/**
	 * @return reference to the leftmost node in the binary tree rooted at this node
	 */
	public BinaryNode<Type> getLeftmostNode() {
		BinaryNode<Type> leftNode = getLeftChild();
		if(leftNode == null) {
			return this;
		}
		return leftNode.getLeftmostNode();
	}

	/**
	 * @return reference to the rightmost node in the binary tree rooted at this node
	 */
	public BinaryNode<Type> getRightmostNode() {
		BinaryNode<Type> rightNode = getRightChild();
		if(rightNode == null) {
			return this;
		}
		return rightNode.getRightmostNode();
	}

	/**
	 * @return the height of the binary tree rooted at this node
	 * 
	 * The height of a tree is the length of the longest path to a leaf
	 * node. Consider a tree with a single node to have a height of zero.
	 */
	public int height() {
		if(leftChild == null && rightChild == null)
			return 0;
		
		int rightHeight = 0;
		int leftHeight = 0;
		
		if(rightChild != null) {
			rightHeight = rightChild.height();
		}
		if(leftChild != null) {
			leftHeight = leftChild.height();
		}
		if(leftHeight > rightHeight)
			return leftChild.height() + 1;
		
		return rightChild.height() + 1;
	}
}
