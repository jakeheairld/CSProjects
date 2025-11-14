package assign03;

import java.util.ArrayList;
import java.util.Random;

public class PriorityQueueTimer extends TimerTemplate{

	private SimplePriorityQueue<Integer> setupQueue;
	
	Random random = new Random();
	
	public PriorityQueueTimer(int[] problemSizes, int timesToLoop) {
		super(problemSizes, timesToLoop);
	}

	@Override
	protected void setup(int n) {
		setupQueue = new SimplePriorityQueue<Integer>();
		for (int i = setupQueue.size(); i < n; i++) {
			setupQueue.insert(i);
		}
	}

	@Override
	protected void timingIteration(int n) {
		
		setupQueue.insert(random.nextInt());
		
		//setupQueue.findMax();
	}

	@Override
	protected void compensationIteration(int n) {
		random.nextInt();
	}

	public static void main(String[] args){
		 ArrayList<Integer> ns = new ArrayList<>();
		 for(double n = 100000; n < 2000000; n += 100000) {
			   ns.add((int)n);
		 }
	
		 int[] problemSizes = new int[ns.size()];
		 
		 for(int i = 0; i < problemSizes.length; i++) {
			   problemSizes[i] = ns.get(i);
		 }
		 
		 var timer = new PriorityQueueTimer(problemSizes, 10);
		 var results = timer.run();
		 
		 System.out.println("n, time");
		 
		 for(var result : results){
			 System.out.println(result.n() + ", " + result.avgNanoSecs());
		 }
		 
	}
}
