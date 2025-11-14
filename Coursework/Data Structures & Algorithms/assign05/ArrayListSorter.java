package assign05;

import java.util.*;

/**
 * This class is an array list sorter for generic array lists using merge sort and quick sort.
 * 
 * @author Jake Heairld and Anton Smolyanyy
 * @version Feb 21, 2024
 */
public class ArrayListSorter {

	private static final int threshold = 10;
	
	/**
	 * Driver method for merge sort to sort a generic array list.
	 * 
	 * @param <T> - The type of the objects stored in the array list.
	 * @param arr - The array list containing the items to be sorted.
	 */
	public static <T extends Comparable<? super T>> void mergesort(ArrayList<T> arr) {
		if(arr.size() <= 1) {
			return;
		}
		mergesortRecursive(arr);
	}
	
	/**
	 * Recursive method for merge sort for sorting a generic array list.
	 * 
	 * @param <T> - The type of the items stored in the array.
	 * @param arr - The array to be sorted.
	 */
	private static <T extends Comparable<? super T>> void mergesortRecursive(ArrayList<T> arr) {
		int middle = arr.size()/2;
		ArrayList<T> left = new ArrayList<T>();
		ArrayList<T> right = new ArrayList<T>();
		if(arr.size() <= threshold) {
			insertionSort(arr);
		} else {
			for(int i = 0; i <= middle; i++) {
				left.add(arr.get(i));
			}
			for(int i = middle + 1; i < arr.size(); i++) {
				right.add(arr.get(i));
			}
			mergesort(left);
			mergesort(right);
		}
		merge(arr, left, right);
	}
	
	/**
	 * Merges two sub arrays into a larger array.
	 * 
	 * @param <T> - The type of the items stored in the array lists.
	 * @param arr - The larger merged array of the two sub arrays.
	 * @param left - The left sub array.
	 * @param right - The right sub array.
	 */
	private static <T extends Comparable<? super T>> void merge(ArrayList<T> arr, ArrayList<T> left, ArrayList<T> right) {
		int i = 0, j = 0, k = 0;
		while(i < left.size() && j < right.size()) {
			if(left.get(i).compareTo(right.get(j)) <= 0) {
				arr.set(k, left.get(i));
				i++;
			} else {
				arr.set(k, right.get(j));
				j++;
			}
			k++;
		}
		while (i < left.size()) {
			arr.set(k, left.get(i));
			i++;
			k++;
		}
		while(j < right.size()) {
			arr.set(k, right.get(j));
			j++;
			k++;
		}
	}
	
	/**
	 * Sorts an array list using the insertion sort algorithm.
	 * 
	 * @param <T> - The type of items stored in the array list.
	 * @param arr - The array list to be sorted.
	 */
	private static <T extends Comparable<? super T>> void insertionSort(ArrayList<T> arr) {
		for (int i = 1; i < arr.size(); i++) {
			T temp = arr.get(i);
			int j = i;
			while (j > 0 && temp.compareTo(arr.get(j-1)) < 0) {
				arr.set(j, arr.get(j-1));
				j--;
			}
			arr.set(j, temp);
		}
	}
	
	/**
	 * Driver method for quick sort to sort a generic array list.
	 * 
	 * @param <T> - The type of the objects stored in the array list.
	 * @param arr - The array list containing the items to be sorted.
	 */
	public static <T extends Comparable<? super T>> void quicksort(ArrayList<T> arr) {
		quicksortRecursive(arr, 0, arr.size()-1);
	}
	
	/**
	 * Recursive method for quick sort for sorting a generic array list.
	 * 
	 * @param <T> - The type of the objects stored in the array list.
	 * @param arr - The array list containing the items to be sorted.
	 * @param l - The lower index for quick sort.
	 * @param h - The higher index for quick sort.
	 */
	public static <T extends Comparable<? super T>> void quicksortRecursive(ArrayList<T> arr, int l, int h) {
		if(l >= h) {
			return;
		}
		int pivotIndex = findMedianPivot(arr, l, h);
		T temp = arr.get(pivotIndex);
		arr.set(pivotIndex, arr.get(h));
		arr.set(h, temp);
		
		int partitionIndex = partition(arr, l, pivotIndex, h);
		
		quicksortRecursive(arr, l, partitionIndex - 1);
		quicksortRecursive(arr, partitionIndex + 1, h);
	}
	
	/**
	 * Sorts the sub array using a pivot index.
	 * 
	 * @param <T> - The type of the items in the array list.
	 * @param arr - The array list to be sorted.
	 * @param l - The lower index.
	 * @param p - The index of the pivot.
	 * @param h - The higher index/
	 * @return The partitioned matrix.
	 */
	private static <T extends Comparable<? super T>> int partition(ArrayList<T> arr, int l, int p, int h) {
		int i = l;
		int j = h;
		while(i < j) {
			while(arr.get(i).compareTo(arr.get(h)) <= 0 && i < j) {
				i++;
			}
			while(arr.get(j).compareTo(arr.get(h)) >= 0 && i < j) {
				j--;
			}
			T tempElement = arr.get(j);
			arr.set(j, arr.get(i));
			arr.set(i, tempElement);
		}
		T tempPivotIndex = arr.get(i);
		arr.set(i, arr.get(h));
		arr.set(h, tempPivotIndex);
		return i;
	}
	
	/**
	 * Finds and returns the index of a random pivot in the array.
	 * 
	 * @param <T> - The type of the items stored in the array.
	 * @param arr - The array the pivot is being found for.
	 * @param l - The low index.
	 * @param h - The high index.
	 * @return - The index of our random pivot.
	 */
	private static <T> int findRandomPivot(ArrayList<T> arr, int l, int h){
		Random r = new Random();
		return r.nextInt(h-l+1) + l;
	}
	
	/**
	 * Finds and returns the high index of the array for a  pivot.
	 * 
	 * @param <T> - The type of the items stored in the array.
	 * @param arr - The array the pivot is being found for.
	 * @param h - The high index.
	 * @return - The index for our last element pivot.
	 */
	private static <T> int findLastPivot(ArrayList<T> arr, int h) {
		return h;
	}
	
	/**
	 * Finds and returns the pivot index for the median of the high, low, and middle elements.
	 * 
	 * @param <T> - the type of the items stored in the array.
	 * @param arr - The array the pivot is being found for.
	 * @param l - The low index.
	 * @param h - The high index.
	 * @return - The index for our last element pivot.
	 */
	private static <T extends Comparable<? super T>> int findMedianPivot(ArrayList<T> arr, int l, int h) {
		T leftElement = arr.get(l);
		T rightElement = arr.get(h);
		T middleElement = arr.get(l + (h-l)/2);
		int medianIndex;
		if((leftElement.compareTo(middleElement) <= 0 && leftElement.compareTo(rightElement) >= 0) || (leftElement.compareTo(middleElement) >= 0 && leftElement.compareTo(rightElement) <= 0)) {
			medianIndex = l; 
		} else if((middleElement.compareTo(leftElement) <= 0 && middleElement.compareTo(rightElement) >= 0) || (middleElement.compareTo(leftElement) >= 0 && middleElement.compareTo(rightElement) <= 0)){
			medianIndex = l + (h-l)/2;
		} else {
			medianIndex = h;
		} 
		return medianIndex;
	}
	
	/**
	 * Generates an array list with numbers 1 to size in ascending order.
	 * 
	 * @param size - The size of the array list and the value of the last element.
	 * @return - The ascending array list.
	 */
	public static ArrayList<Integer> generateAscending(int size){
		ArrayList<Integer> arr = new ArrayList<Integer>();
		for(int i = 1; i<=size; i++) {
			arr.add(i);
		}
		return arr;
	}
	
	/**
	 * Generates an array list with number 1 to size in random order.
	 * 
	 * @param size - The size of the array list.
	 * @return - The permuted array list.
	 */
	public static ArrayList<Integer> generatePermuted(int size){
		ArrayList<Integer> arr = new ArrayList<Integer>();
		for(int i = 1; i<=size; i++) {
			arr.add(i);
		}
		
		Collections.shuffle(arr, new Random(20));
		return arr;
	}
	
	/**
	 * Generates an array list with numbers 1 to size in descending order.
	 * 
	 * @param size - The size of the array list and the value of the first element.
	 * @return - The descending array list.
	 */
	public static ArrayList<Integer> generateDescending(int size){
		ArrayList<Integer> arr = new ArrayList<Integer>();
		for(int i = size; i>0; i--) {
			arr.add(i);
		}
		return arr;
	}
}