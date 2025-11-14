package assign07;

import java.util.ArrayList;
import java.util.Random;

import assign06.ArrayStack;
import assign06.LinkedListStack;

public class GraphTimer extends TimerTemplate{
	
	ArrayList<String> sources;
	ArrayList<String> destinations;
	Random r;;
	
	public GraphTimer(int[] problemSizes, int timesToLoop) {
		super(problemSizes, timesToLoop);
	}

	@Override
	protected void setup(int n) {
		sources = new ArrayList<String>();
		destinations = new ArrayList<String>();
		r = new Random();
		RandomDOTGenerator.generateRandomDotFile("src/assign07/dotFile.txt", n);
		GraphUtility.buildListsFromDot("src/assign07/dotFile.txt", sources, destinations);
	}

	@Override
	protected void timingIteration(int n) {
		try {
			GraphUtility.sort(sources, destinations);
		} catch (IllegalArgumentException e) {
			
		}
	}

	@Override
	protected void compensationIteration(int n) {
		 sources.get(r.nextInt(sources.size()));
		 destinations.get(r.nextInt(destinations.size()));
	}

	public static void main(String[] args){
		 ArrayList<Integer> ns = new ArrayList<>();
		 for(double n = 1000; n <= 20000; n += 1000) {
			   ns.add((int)n);    
		 }
	
		 int[] problemSizes = new int[ns.size()];
		 
		 for(int i = 0; i < problemSizes.length; i++) {
			   problemSizes[i] = ns.get(i);
		 }
		 
		 var timer = new GraphTimer(problemSizes, 1000);
		 
		 var results = timer.run();
		 System.out.println("n, time");
		 
		// for(var size : problemSizes){
			 
		// }
		 
	}
	
	public void runForSize(int n) {
		setup(n);
		long startTime = System.nanoTime();
		timingIteration(n);
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		System.out.println(n + ", " + duration);
		compensationIteration(n);
	}
}
