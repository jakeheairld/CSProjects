package assign07;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Contains several methods for solving problems on generic, directed,
 * unweighted, sparse graphs.
 * 
 * @author CS 2420 instructors and Jake Heairld and Anton Smolyanyy
 * @version Mar 13, 2024
 */
public class GraphUtility {

	/**
	 * Checks if two vertices in a graph are linked to one another from source to
	 * destination.
	 * 
	 * @param <Type>       - The type of data stored in the vertices.
	 * @param sources      - The source vertices for the graph.
	 * @param destinations - The corresponding destination vertices for the graph.
	 * @param srcData      - The data in the source vertex.
	 * @param dstData      - The data in the destination vertex.
	 * @return - True if the vertices are connected, false otherwise.
	 * @throws IllegalArgumentException - Thrown if there does not exist a vertex in
	 *                                  the graph with srcData or dstData, or if the
	 *                                  number of vertices in sources and
	 *                                  destinations varies.
	 */
	public static <Type> boolean areConnected(List<Type> sources, List<Type> destinations, Type srcData, Type dstData)
			throws IllegalArgumentException {
		if (sources.size() != destinations.size()) {
			throw new IllegalArgumentException("There is not a destination vertex for every source.");
		}
		if (!(sources.contains(srcData) || destinations.contains(srcData))) {
			throw new IllegalArgumentException("Source data not found in lists.");
		}
		if (!(destinations.contains(dstData) || sources.contains(dstData))) {
			throw new IllegalArgumentException("Destination data not found in lists.");
		}
		Graph<Type> graph = graphMaker(sources, destinations);
		return graph.dfs(graph.getVertices().get(srcData), graph.getVertices().get(dstData));
	}

	/**
	 * Finds and returns a list with the shortest path between two vertices in a
	 * graph.
	 * 
	 * @param <Type>       - The type of data stored in the vertices.
	 * @param sources      - The source vertices for the graph.
	 * @param destinations - The corresponding destination vertices for the graph.
	 * @param srcData      - The data in the source vertex.
	 * @param dstData      - The data in the destination vertex.
	 * @return - The list with the shortest path.
	 * @throws IllegalArgumentException - Thrown if there is no vertex in the graph
	 *                                  for srcData or dstData, also thrown if no
	 *                                  path exists between the two vertices.
	 */
	public static <Type> List<Type> shortestPath(List<Type> sources, List<Type> destinations, Type srcData,
			Type dstData) throws IllegalArgumentException {
		if (sources.size() != destinations.size()) {
			throw new IllegalArgumentException("There is not a destination vertex for every source.");
		}
		Graph<Type> graph = graphMaker(sources, destinations);
		return graph.bfs(graph.getVertices().get(srcData), graph.getVertices().get(dstData));
	}

	/**
	 * Returns a list with the topologically sorted vertices of the graph.
	 * 
	 * @param <Type>       - The type of data stored in the vertices.
	 * @param sources      - The source vertices for the graph.
	 * @param destinations - The corresponding destination vertices for the graph.
	 * @return - The topologically sorted list of vertices.
	 * @throws IllegalArgumentException - Thrown when there is a cycle in the graph
	 *                                  or when the number of vertices in sources
	 *                                  and destinations varies.
	 */
	public static <Type> List<Type> sort(List<Type> sources, List<Type> destinations) throws IllegalArgumentException {
		if (sources.size() != destinations.size()) {
			throw new IllegalArgumentException("There is not a destination vertex for every source.");
		}
		Graph<Type> graph = graphMaker(sources, destinations);
		return graph.toposort(graph);
	}

	/**
	 * Private helper method that makes a graph from the given list of sources and destinations.
	 * 
	 * @param <Type>       - The type of data stored in the vertices.
	 * @param sources      - The source vertices for the graph.
	 * @param destinations - The corresponding destination vertices for the graph.
	 * @return- The graph made from the two lists.
	 */
	private static <Type> Graph<Type> graphMaker(List<Type> sources, List<Type> destinations) {
		Graph<Type> graph = new Graph<Type>();
		for (int i = 0; i < sources.size(); i++) {
			graph.addEdge(sources.get(i), destinations.get(i));
		}
		return graph;
	}

	/**
	 * Builds "sources" and "destinations" lists according to the edges specified in
	 * the given DOT file (e.g., "a -> b"). Assumes that the vertex data type is
	 * String.
	 * 
	 * Accepts many valid "digraph" DOT files (see examples posted on Canvas).
	 * --accepts \\-style comments --accepts one edge per line or edges terminated
	 * with ; --does not accept attributes in [] (e.g., [label = "a label"])
	 * 
	 * @param filename     - name of the DOT file
	 * @param sources      - empty ArrayList, when method returns it is a valid
	 *                     "sources" list that can be passed to the public methods
	 *                     in this class
	 * @param destinations - empty ArrayList, when method returns it is a valid
	 *                     "destinations" list that can be passed to the public
	 *                     methods in this class
	 */
	public static void buildListsFromDot(String filename, ArrayList<String> sources, ArrayList<String> destinations) {

		Scanner scan = null;
		try {
			scan = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}

		scan.useDelimiter(";|\n");

		// Determine if graph is directed (i.e., look for "digraph id {").
		String line = "", edgeOp = "";
		while (scan.hasNext()) {
			line = scan.next();

			// Skip //-style comments.
			line = line.replaceFirst("//.*", "");

			if (line.indexOf("digraph") >= 0) {
				edgeOp = "->";
				line = line.replaceFirst(".*\\{", "");
				break;
			}
		}
		if (edgeOp.equals("")) {
			System.out.println("DOT graph must be directed (i.e., digraph).");
			scan.close();
			System.exit(0);

		}

		// Look for edge operator -> and determine the source and destination
		// vertices for each edge.
		while (scan.hasNext()) {
			String[] substring = line.split(edgeOp);

			for (int i = 0; i < substring.length - 1; i += 2) {
				// remove " and trim whitespace from node string on the left
				String vertex1 = substring[0].replace("\"", "").trim();
				// if string is empty, try again
				if (vertex1.equals("")) {
					continue;
				}

				// do the same for the node string on the right
				String vertex2 = substring[1].replace("\"", "").trim();
				if (vertex2.equals("")) {
					continue;
				}

				// indicate edge between vertex1 and vertex2
				sources.add(vertex1);
				destinations.add(vertex2);
			}

			// do until the "}" has been read
			if (substring[substring.length - 1].indexOf("}") >= 0) {
				break;
			}

			line = scan.next();

			// Skip //-style comments.
			line = line.replaceFirst("//.*", "");
		}

		scan.close();
	}
}
