package assign07;

/**
 * This class represents an edge between two vertices in a graph.
 * 
 * @author Jake Heairld and Anton Smolyanyy
 * @version Mar 13, 2024
 * 
 * @param <T> - The type of data stored in the vertices.
 */
public class Edge<T> {

	private Vertex<T> dst;
	
	/**
	 * Creates an Edge object, given the Vertex that is the destination.
	 * (The Vertex that stores this Edge object is the source.)
	 * 
	 * @param dst - the destination Vertex
	 */
	public Edge(Vertex<T> dst) {
		this.dst = dst;
	}

	/**
	 * @return the destination Vertex of this Edge
	 */
	public Vertex<T> getVertex() {
		return this.dst;
	}

	/**
	 * Returns the name of the destination Vertex as a textual representation of this Edge.
	 */
	public String toString() {
		return "" + this.dst.getData();
	}
}
