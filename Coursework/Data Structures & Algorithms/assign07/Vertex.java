package assign07;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * This class represents a vertex in a graph.
 * 
 * @author Jake Heairld and Anton Smolyanyy
 * @version Mar 13, 2024
 * 
 * @param <T> - The type of data stored in the vertex.
 */
public class Vertex<T> {

	private T data;
	private LinkedList<Edge<T>> adj;
	private boolean visited;
	private Vertex<T> cameFrom;
	private int indegree;

	/**
	 * Creates a vertex holding data.
	 * 
	 * @param data - The data being stored in the vertex.
	 */
	public Vertex(T data) {
		this.data = data;
		this.adj = new LinkedList<Edge<T>>();	
		this.visited = false;
		this.cameFrom = null;
		this.indegree = 0;
	}
	
	/**
	 * Retrieves the data stored in this vertex.
	 * 
	 * @return - The data.
	 */
	public T getData() {
		return this.data;
	}
	
	/**
	 * Sets the visited status of this vertex to true.
	 */
	public void setVisited() {
		this.visited = true;
	}
	
	/**
	 * Sets the visited status of this vertex to false.
	 */
	public void setNotVisited() {
		this.visited = false;
	}
	
	/**
	 * Retrieves the visited status of this vertex.
	 * 
	 * @return - the visited status of this vertex.
	 */
	public boolean getVisited() {
		return this.visited;
	}
	
	/**
	 * Sets the came from vertex for this vertex.
	 * 
	 * @param cameFrom - The vertex this vertex came from.
	 */
	public void setCameFrom(Vertex<T> cameFrom) {
		this.cameFrom = cameFrom;
	}
	
	/**
	 * Sets the came from value for this vertex to null;
	 */
	public void undoCameFrom() {
		this.cameFrom = null;
	}
	
	/**
	 * Retrieves the vertex that this vertex came from.
	 * 
	 * @return - The vertex this vertex came from.
	 */
	public Vertex<T> getCameFrom() {
		return this.cameFrom;
	}
	
	/**
	 * Adds 1 to the in degree value for this vertex.
	 */
	public void addIndegree() {
		this.indegree++;
	}
	
	/**
	 * Subtracts 1 from the in degree for this vertex..
	 */
	public void decrIndegree() {
		this.indegree--;
	}
	
	/**
	 * Sets the in degree for this vertex to 0.
	 */
	public void undoIndegree() {
		this.indegree = 0;
	}
	
	/**
	 * Retrieves the in degree of this vertex.
	 * 
	 * @return - The in degree for this vertex.
	 */
	public int getIndegree() {
		return this.indegree;
	}

	/**
	 * Adds a directed edge from this Vertex to another.
	 * 
	 * @param otherVertex - the Vertex object that is the destination of the edge
	 */
	public void addEdge(Vertex<T> otherVertex) {
		adj.add(new Edge<T>(otherVertex));
	}

	/**
	 * @return a iterator for accessing the edges for which this Vertex is the source
	 */
	public Iterator<Edge<T>> edges() {
		return adj.iterator();
	}

	/**
	 * Generates and returns a textual representation of this Vertex.
	 */
	public String toString() {
		String s = "Vertex " + this.data.toString() + " adjacent to vertices ";
		Iterator<Edge<T>> itr = adj.iterator();
		while(itr.hasNext())
			s += itr.next() + "  ";
		return s;
	}
	
}
