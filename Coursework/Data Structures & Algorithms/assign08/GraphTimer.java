package assign08;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GraphTimer extends TimerTemplate{
	
	BinarySearchTree<Integer> binaryTree;
	ArrayList<Integer> numList;
	Random r;
	
	public GraphTimer(int[] problemSizes, int timesToLoop) {
		super(problemSizes, timesToLoop);
	}

	@Override
	protected void setup(int n) {
		r = new Random();
		binaryTree = new BinarySearchTree<Integer>();
		numList = new ArrayList<Integer>();
		for(int i = 0; i < n; i++) {
			numList.add(i);
		}
		//Collections.shuffle(numList);
		//binaryTree.addAll(numList);
	}

	@Override
	protected void timingIteration(int n) {
		for(int i = 0; i < numList.size(); i++) {			
			binaryTree.add(numList.get(i));
		}
	}

	@Override
	protected void compensationIteration(int n) {
		for(int i = 0; i < numList.size(); i++) {			
			numList.get(i);
		}
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
