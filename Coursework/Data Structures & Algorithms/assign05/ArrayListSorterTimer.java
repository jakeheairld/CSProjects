package assign05;

import java.util.ArrayList;
import java.util.Random;

import assign05.TimerTemplate;

public class ArrayListSorterTimer extends TimerTemplate{
	
	Random random;
	ArrayList<Integer> listOfIntegers;
	
	public ArrayListSorterTimer(int[] problemSizes, int timesToLoop) {
		super(problemSizes, timesToLoop);
	}

	@Override
	protected void setup(int n) {
		listOfIntegers = ArrayListSorter.generateDescending(n);
	}

	@Override
	protected void timingIteration(int n) {
		ArrayListSorter.quicksort(listOfIntegers);
	}

	@Override
	protected void compensationIteration(int n) {
		ArrayListSorter.generateDescending(n);
	}

	public static void main(String[] args){
		 ArrayList<Integer> ns = new ArrayList<>();
		 for(double n = 2000; n <= 50000; n += 2000) {
			   ns.add((int)n);    
		 }
	
		 int[] problemSizes = new int[ns.size()];
		 
		 for(int i = 0; i < problemSizes.length; i++) {
			   problemSizes[i] = ns.get(i);
		 }
		 
		 var timer = new ArrayListSorterTimer(problemSizes, 1000000);
		 
		 System.out.println("n, time");
		 
		 for(var size : problemSizes){
			 timer.runForSize(size);
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
