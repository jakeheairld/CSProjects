package assign07;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class GraphTester {

	@Test
	void testUltimate() {
		Graph<String> sample = new Graph<String>();
		sample.addEdge("a", "b");
		sample.addEdge("b", "c");
		sample.addEdge("c", "d");
		sample.addEdge("b", "d");
		//sample.addEdge("d", "a");
		
		//assertTrue(sample.dfs(, ));

		// print textual representation of sample graph
		//System.out.println("DOT Encoding:");
		//System.out.println(sample.generateDot());
		
		Vertex<String> startVertex = sample.getVertices().get("a");
		Vertex<String> goalVertex = sample.getVertices().get("d");
		
		assertEquals(true, sample.dfs(startVertex, goalVertex));
	}
	
	@Test
	void testAreConnected() {
		List<String> srcs = new ArrayList<String>();
		srcs.add("a");
		srcs.add("b");
		srcs.add("c");
		srcs.add("b");		
		List<String> dsts = new ArrayList<String>();
		dsts.add("b");
		dsts.add("c");
		dsts.add("d");
		dsts.add("d");
		assertEquals(true, GraphUtility.areConnected(srcs, dsts, "a", "d"));
		
	}
	
	@Test
	void testAreConnectedCyclesFalse() {
		List<String> srcs = new ArrayList<String>();
		srcs.add("a");
		srcs.add("a");
		srcs.add("b");
		//srcs.add("c");
		srcs.add("c");
		srcs.add("d");		
		srcs.add("c");
		List<String> dsts = new ArrayList<String>();
		dsts.add("b");
		dsts.add("d");
		dsts.add("c");
		//dsts.add("a");
		dsts.add("d");
		dsts.add("e");
		dsts.add("e");

		//assertFalse(GraphUtility.areConnected(srcs, dsts, "a", "a"));
		
	}
	
	@Test
	void testAreConnectedCyclesTrue() {
		List<String> srcs = new ArrayList<String>();
		srcs.add("a");
		srcs.add("a");
		srcs.add("b");
		srcs.add("c");
		srcs.add("c");
		srcs.add("d");		
		srcs.add("c");
		List<String> dsts = new ArrayList<String>();
		dsts.add("b");
		dsts.add("d");
		dsts.add("c");
		dsts.add("a");
		dsts.add("d");
		dsts.add("e");
		dsts.add("e");

		assertTrue(GraphUtility.areConnected(srcs, dsts, "a", "a"));
		
	}
	
	@Test
	void testShortestPath() {
		List<String> srcs = new ArrayList<String>();
		srcs.add("a");
		srcs.add("b");
		srcs.add("c");
		srcs.add("b");		
		List<String> dsts = new ArrayList<String>();
		dsts.add("b");
		dsts.add("c");
		dsts.add("d");
		dsts.add("d");
		
		List<String> path = new ArrayList<String>();
		path.add("a");
		path.add("b");
		path.add("d");
		
		assertEquals(path, GraphUtility.shortestPath(srcs, dsts, "a", "d"));
		
	}
	
	@Test
	void testShortestPath2() {
		List<String> srcs = new ArrayList<String>();
		srcs.add("a");
		srcs.add("a");
		srcs.add("b");
		srcs.add("c");
		srcs.add("d");	
		srcs.add("c");	
		srcs.add("f");		
		srcs.add("a");		
		srcs.add("d");		

	
		List<String> dsts = new ArrayList<String>();
		dsts.add("b");
		dsts.add("d");
		dsts.add("c");
		dsts.add("d");
		dsts.add("e");
		dsts.add("e");
		dsts.add("a");
		dsts.add("f");
		dsts.add("f");

		
		List<String> path = new ArrayList<String>();
		path.add("a");
//		path.add("d");
//		path.add("f");
//		path.add("a");

		
		assertEquals(path, GraphUtility.shortestPath(srcs, dsts, "a", "a"));
		
	}
	
	@Test
	void testTopo() {
		List<String> srcs = new ArrayList<String>();
		srcs.add("a");
		srcs.add("b");
		srcs.add("b");
		srcs.add("f");		
		srcs.add("f");		
		srcs.add("a");	
		srcs.add("d");	
		srcs.add("a");
		srcs.add("c");
		srcs.add("d");	
		srcs.add("g");	
		srcs.add("j");		
		
		//srcs.add("k");
	
		List<String> dsts = new ArrayList<String>();
		dsts.add("b");
		dsts.add("e");
		dsts.add("f");
		dsts.add("i");	
		dsts.add("j");	
		dsts.add("d");
		dsts.add("h");
		dsts.add("c");
		dsts.add("g");
		dsts.add("g");
		dsts.add("k");
		dsts.add("k");
		
		//dsts.add("a");
		
		List<String> path = new ArrayList<String>();
		path.add("a");
		path.add("b");
		path.add("d");
		path.add("c");
		path.add("e");
		path.add("f");
		path.add("h");
		path.add("g");
		path.add("i");		
		path.add("j");
		path.add("k");
		
		//assertThrows(IllegalArgumentException.class, ()-> GraphUtility.sort(srcs, dsts));
		assertEquals(path, GraphUtility.sort(srcs, dsts));
		
	}
	
	@Test
	void testTopoCycle() {
		List<String> srcs = new ArrayList<String>();
		srcs.add("a");
		srcs.add("b");
		srcs.add("c");
		List<String> dsts = new ArrayList<String>();
		dsts.add("b");
		dsts.add("c");
		dsts.add("a");
		assertThrows(IllegalArgumentException.class, ()-> GraphUtility.sort(srcs, dsts));
	}

	@Test
	void testTopoTINY() {
		List<String> srcs = new ArrayList<String>();
		srcs.add("a");
		srcs.add("b");
		srcs.add("c");
		List<String> dsts = new ArrayList<String>();
		dsts.add("b");
		dsts.add("c");
		dsts.add("d");
		List<String> path = new ArrayList<String>();
		path.add("a");
		path.add("b");
		path.add("c");
		path.add("d");
		assertEquals(path, GraphUtility.sort(srcs, dsts));
	}
	
	
	
}
