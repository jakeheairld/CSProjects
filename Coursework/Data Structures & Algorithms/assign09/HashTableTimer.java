package assign09;

import java.util.ArrayList;
import java.util.Random;

public class HashTableTimer extends TimerTemplate{
	
	ArrayList<String> sources;
	ArrayList<String> destinations;
	Random r;;
	
	public HashTableTimer(int[] problemSizes, int timesToLoop) {
		super(problemSizes, timesToLoop);
	}

	@Override
	protected void setup(int n) {
		
	}

	@Override
	protected void timingIteration(int n) {
		
	}

	@Override
	protected void compensationIteration(int n) {
		 
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
		 
		 var timer = new HashTableTimer(problemSizes, 1000);
		 
		 @SuppressWarnings("unused")
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
