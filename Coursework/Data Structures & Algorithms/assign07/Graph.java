package assign07;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * This class represents a graph.
 * 
 * @author Jake Heairld and Anton Smolyanyy
 * @version Mar 13, 2024
 * 
 * @param <T> - The type of data stored in the graph.
 */
public class Graph<T> {

	// the graph -- a set of vertices (String name mapped to Vertex instance)
	private HashMap<T, Vertex<T>> vertices;

	/**
	 * Constructs an empty graph.
	 */
	public Graph() {
		vertices = new HashMap<T, Vertex<T>>();
	}

	/**
	 * Retrieves the vertices in the graph.
	 * 
	 * @return - A hash map of the data and their corresponding vertices.
	 */
	public HashMap<T, Vertex<T>> getVertices() {
		return this.vertices;
	}
	
	/**
	 * Resets the visited status of all vertices in the graph.
	 */
	private void unvisitVertices() {
		for(Vertex<T> vertex : vertices.values()) {
			vertex.setNotVisited();
		}
	}
	
	/**
	 * Resets the came from status of all vertices in the graph.
	 */
	private void resetCameFrom() {
		for(Vertex<T> vertex : vertices.values()) {
			vertex.undoCameFrom();
		}
	}

	/**
	 * Resets the in degree status of all vertices in the graph.
	 */
	private void resetIndegree() {
		for(Vertex<T> vertex : vertices.values()) {
			vertex.undoIndegree();
		}
	}
	
	/**
	 * Utilizes the depth first search algorithm to determine if a path exists
	 * between two vertices.
	 * 
	 * @param current - The current vertex.
	 * @param goal    - The vertex being searched for.
	 * @return - True if a path is found.
	 */
	public boolean dfs(Vertex<T> current, Vertex<T> goal) {
		if (current.equals(goal)) {
			unvisitVertices();
			return true;
		}
		current.setVisited();
		Iterator<Edge<T>> iter = current.edges();
		while (iter.hasNext()) {
			Edge<T> edge = iter.next();
			Vertex<T> nextVertex = edge.getVertex();
			if (!nextVertex.getVisited()) {
				return dfs(nextVertex, goal);
			}
		}
		unvisitVertices();
		return false;
	}

	/**
	 * Utilizes the breadth first search algorithm to find the shortest path between
	 * two vertices in a graph.
	 * 
	 * @param source - The source vertex.
	 * @param goal   - The end vertex.
	 * @return - The shortest path between the two vertices.
	 * @throws IllegalArgumentException - If the graph does not contain source or
	 *                                  goal, also thrown if no path exists between
	 *                                  the two vertices.
	 */
	public List<T> bfs(Vertex<T> source, Vertex<T> goal) throws IllegalArgumentException {
		if(!vertices.containsValue(source)) {
			throw new IllegalArgumentException("Source data not found in graph.");
		}
		if(!vertices.containsValue(goal)) {
			throw new IllegalArgumentException("Destination data not found in graph.");
		}
		if(!dfs(source, goal)) {
			throw new IllegalArgumentException("There does not exist a path between the two vertices.");
		}
		Queue<Vertex<T>> vertices = new LinkedList<Vertex<T>>();
		vertices.offer(source);
		List<T> shortestPath = new ArrayList<T>();
		shortestPath.add(goal.getData());
		while (vertices.size() > 0) {
			Vertex<T> currentVertex = vertices.poll();
			currentVertex.setVisited();
			Iterator<Edge<T>> iter = currentVertex.edges();
			while (iter.hasNext()) {
				Edge<T> edge = iter.next();
				Vertex<T> nextVertex = edge.getVertex();
				if (!nextVertex.getVisited()) {
					vertices.offer(nextVertex);
					nextVertex.setVisited();
					nextVertex.setCameFrom(currentVertex);
				}
				if (nextVertex.equals(goal)) {
					break;
				}
			}
		}
		while (goal.getCameFrom() != null) {
			shortestPath.add(0, goal.getCameFrom().getData());
			goal = goal.getCameFrom();
		}
		unvisitVertices();
		resetCameFrom();
		return shortestPath;
	}

	/**
	 * Generates a sorted ordering of the vertices in the graph using the topological sorting algorithm.
	 * 
	 * @param graph - The graph used for sorting.
	 * @return - The sorted list of vertices.
	 * @throws IllegalArgumentException - Thrown if the graph contains a cycle.
	 */
	public List<T> toposort(Graph<T> graph) throws IllegalArgumentException {
		int verticeCounter = 0;
		Queue<Vertex<T>> vertices = new LinkedList<Vertex<T>>();
		List<T> ordering = new ArrayList<T>();
		for (Vertex<T> vertex : graph.getVertices().values()) {
			if (vertex.getIndegree() == 0) {
				vertices.offer(vertex);
				verticeCounter++;
			}
		}
		while (vertices.size() > 0) {
			Vertex<T> currentVertex = vertices.poll();
			Iterator<Edge<T>> iter = currentVertex.edges();
			ordering.add(currentVertex.getData());
			while (iter.hasNext()) {
				Edge<T> edge = iter.next();
				Vertex<T> nextVertex = edge.getVertex();
				nextVertex.decrIndegree();
				if (nextVertex.getIndegree() == 0) {
					verticeCounter++;
					vertices.offer(nextVertex);
				}
			}
		}
		resetIndegree();
		if(verticeCounter != this.vertices.values().size()) {
			throw new IllegalArgumentException("Graph contains a cycle.");
		}
		return ordering;
	}

	/**
	 * Adds to the graph a directed edge from the vertex with data "data1" to the
	 * vertex with data "data2". (If either vertex does not already exist in the
	 * graph, it is added.)
	 * 
	 * @param data1 - data for source vertex
	 * @param data2 - data for destination vertex
	 */
	public void addEdge(T data1, T data2) {
		Vertex<T> vertex1;
		// if vertex already exists in graph, get its object
		if (vertices.containsKey(data1))
			vertex1 = vertices.get(data1);
		// else, create a new object and add to graph
		else {
			vertex1 = new Vertex<T>(data1);
			vertices.put(data1, vertex1);
		}
		Vertex<T> vertex2;
		if (vertices.containsKey(data2)) {
			vertex2 = vertices.get(data2);
			vertex2.addIndegree();
		} else {
			vertex2 = new Vertex<T>(data2);
			vertex2.addIndegree();
			vertices.put(data2, vertex2);
		}
		// add new directed edge from vertex1 to vertex2
		vertex1.addEdge(vertex2);
	}

	/**
	 * Generates the DOT encoding of this graph as string, which can be pasted into
	 * http://www.webgraphviz.com to produce a visualization.
	 */
	public String generateDot() {
		StringBuilder dot = new StringBuilder("digraph d {\n");
		// for every vertex
		for (Vertex<T> v : vertices.values()) {
			// for every edge
			Iterator<Edge<T>> edges = v.edges();
			while (edges.hasNext())
				dot.append("\t\"" + v.getData().toString() + "\" -> \"" + edges.next() + "\"\n");
		}
		return dot.toString() + "}";
	}

	/**
	 * Generates a simple textual representation of this graph.
	 */
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (Vertex<T> v : vertices.values())
			result.append(v + "\n");
		return result.toString();
	}
}
