package assign07;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class RandomDOTGenerator {

	 public static void generateRandomDotFile(String filename, int vertexCount) {
		    PrintWriter out = null;
		    try {
		      out = new PrintWriter(filename);
		    } 
		    catch (IOException e) {
		      System.out.println(e);
		    }

		    Random rng = new Random();

		    // randomly construct either a graph or a digraph
		    String edgeOp = "--";
		    if (true) {
		      out.print("di");
		      edgeOp = "->";
		    }
		    out.println("graph G {");

		    // generate a list of vertices
		    String[] vertex = new String[vertexCount];
		    for (int i = 0; i < vertexCount; i++) {
		      vertex[i] = "v" + i;
		    }

		    // randomly connect the vertices using 2 * |V| edges
		    for (int i = 0; i < 2 * vertexCount; i++) {
		      out.println("\t" + vertex[rng.nextInt(vertexCount)] + edgeOp + vertex[rng.nextInt(vertexCount)]);
		    }

		    out.println("}");
		    out.close();
		  }
	
	 
	 public static void main(String[] args) {
		 generateRandomDotFile("src/lab08/DotFile1.txt", 8);
	 }
	
}
