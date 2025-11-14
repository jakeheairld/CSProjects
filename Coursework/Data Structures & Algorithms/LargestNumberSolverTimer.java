package assign04;

import java.util.ArrayList;
import java.util.Random;

public class LargestNumberSolverTimer extends TimerTemplate{
	
	Random random;
	ArrayList<Integer[]> listOfIntegerArrays;
	
	public LargestNumberSolverTimer(int[] problemSizes, int timesToLoop) {
		super(problemSizes, timesToLoop);
	}

	@Override
	protected void setup(int n) {
		listOfIntegerArrays = new ArrayList<Integer[]>();
		random = new Random();
		for(int i = 0; i<n; i++) {
			Integer[] array = new Integer[random.nextInt(100)];
			for(int j = 0; j < array.length; j++) {
				array[j] = random.nextInt(10000);
			}
			
			listOfIntegerArrays.add(array);
			
		}
	}

	@Override
	protected void timingIteration(int n) {
		LargestNumberSolver.findKthLargest(listOfIntegerArrays, n/2);
	}

	@Override
	protected void compensationIteration(int n) {
		random.nextInt(100);
		random.nextInt(1000);
	}

	public static void main(String[] args){
		 ArrayList<Integer> ns = new ArrayList<>();
		 for(double n = 200; n < 5000; n += 200) {
			   ns.add((int)n);    
		 }
	
		 int[] problemSizes = new int[ns.size()];
		 
		 for(int i = 0; i < problemSizes.length; i++) {
			   problemSizes[i] = ns.get(i);
		 }
		 
		 var timer = new LargestNumberSolverTimer(problemSizes, 100);
		// var results = timer.run();
		 
		 System.out.println("n, time");
		 
		 for(var size : problemSizes){
			 timer.runForSize(size);
			 //System.out.println(result.n() + ", " + result.avgNanoSecs());
		 }
		 
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
