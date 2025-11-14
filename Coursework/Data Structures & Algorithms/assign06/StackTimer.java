package assign06;

import java.util.ArrayList;
import java.util.Random;

import assign06.TimerTemplate;

public class StackTimer extends TimerTemplate{
	
	private LinkedListStack<Integer> linkedListStack;
	
	public StackTimer(int[] problemSizes, int timesToLoop) {
		super(problemSizes, timesToLoop);
	}

	@Override
	protected void setup(int n) {
		linkedListStack = new LinkedListStack<Integer>();
		for (int i = 0; i < n; i++) {
			linkedListStack.push(i);
		}
	}

	@Override
	protected void timingIteration(int n) {
		linkedListStack.push(n);
		linkedListStack.push(n);
		linkedListStack.pop();
	}

	@Override
	protected void compensationIteration(int n) {
		linkedListStack.pop();
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
		 
		 var timer = new StackTimer(problemSizes, 500);
		 
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
