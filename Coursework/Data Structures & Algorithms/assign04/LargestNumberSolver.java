package assign04;

import java.math.BigInteger;
import java.util.*;
import java.io.*;

/**
 * This class determines the largest possible number that can be formed from
 * concatenating an Integer array.
 * 
 * @author Jake Heairld and Anton Smolyanyy
 * @version February 08, 2024
 */
public class LargestNumberSolver {

	/**
	 * Sorts an input array based on the given Comparator object.
	 * 
	 * @param arr - generic type array to be sorted
	 * @param cmp - Comparator object that defines the ordering with respect to which the array is to be sorted
	 */
	public static <T> void insertionSort(T[] arr, Comparator<? super T> cmp) {
		
		for (int i = 1; i < arr.length; i++) {
			T temp = arr[i];
			int j = i;
			while (j > 0 && cmp.compare(temp, arr[j-1]) < 0) {
				
				arr[j] = arr[j-1];
				j--;
				
			}
			arr[j] = temp;
		}
	}

	/**
	 * Finds the largest number that can be formed by arranging the Integers of the
	 * given array.
	 * 
	 * @param arr - Integer type array whose elements are to be arranged into the
	 *            largest possible number
	 * @return a BigInteger object which represents the largest number that can be
	 *         formed by arranging the Integers of the given array
	 */
	public static BigInteger findLargestNumber(Integer[] arr) {
		
		StringBuilder bigNumber = new StringBuilder();
		
		//creates a temp array to avoid altering the original array
		Integer[] temp = Arrays.copyOf(arr, arr.length);
		
		
		if (temp.length > 0) {
			
			//sorts the temp array based on the possible string arrangements of the numbers
			insertionSort(temp, (int1, int2) -> (("" + int1 + int2).compareTo("" + int2 + int1)));
			
			for (int i = temp.length - 1; i >= 0; i--) {
				bigNumber.append(temp[i]);
			}
			
			//creates and returns a BigInteger object 
			return new BigInteger(bigNumber.substring(0));
			
		}
		
		return BigInteger.ZERO;
		
	}
	
	/**
	 * Finds the largest primitive int value that can be formed by arranging the Integers of the
	 * given array.
	 * 
	 * @param arr - Integer type array whose elements are to be arranged into the
	 *            largest possible int
	 * @return an int which represents the largest number that can be
	 *         formed by arranging the Integers of the given array
	 * @throws OutOfRangeException if the largest possible number exceeds the int data type's max value
	 */
	public static int findLargestInt(Integer[] arr) throws OutOfRangeException {
		
		//compares the return value of findLargestNumber to the intValue to see if it exceeds the int limit 
		if (!(findLargestNumber(arr).toString().equals(String.valueOf(findLargestNumber(arr).intValue())))) {
			throw new OutOfRangeException("Largest value exceeds integer storage limit");
		}
		
		return findLargestNumber(arr).intValue();
		
	}
	
	/**
	 * Finds the largest primitive long value that can be formed by arranging the Integers of the
	 * given array.
	 * 
	 * @param arr - Integer type array whose elements are to be arranged into the
	 *            largest possible long
	 * @return an long which represents the largest number that can be
	 *         formed by arranging the Integers of the given array
	 * @throws OutOfRangeException if the largest possible number exceeds the long data type's max value
	 */
	public static long findLargestLong(Integer[] arr) throws OutOfRangeException {
		
		//compares the return value of findLargestNumber to the longValue to see if it exceeds the long limit 
		if (!(findLargestNumber(arr).toString().equals(String.valueOf(findLargestNumber(arr).longValue())))) {
			throw new OutOfRangeException("Largest value exceeds long storage limit");
		}
		
		return findLargestNumber(arr).longValue();
		
	}
	
	/**
	 * Finds the sum of the largest numbers formed by each Integer array in the
	 * List.
	 * 
	 * @param list - List of Integer arrays whose largest numbers are to be summed
	 * @return a BigInteger object which represents the sum of the largest numbers
	 *         formed by each array in the List
	 */
	public static BigInteger sum(List<Integer[]> list) {
		
		BigInteger sum = BigInteger.ZERO;
		
		for (int i = 0; i<list.size(); i++) {
			
			//accumulation loop to find the sum
			sum = sum.add(findLargestNumber(list.get(i)));
			
		}
		return sum;
	}   
	
	/**
	 * Finds the kth largest number that can be formed by each Integer array in the
	 * List.
	 * 
	 * @param list - List of Integer arrays whose largest numbers are to be sorted
	 * @param k    - the position of the desired largest number
	 * @return the Integer array which can be arranged to form the largest number of
	 *         all the arrays in the List
	 * @throws IllegalArgumentException if k is not a valid position in the List
	 */
	public static Integer[] findKthLargest(List<Integer[]> list, int k) throws IllegalArgumentException {
		
		if (k<0 || k>=list.size()) {
			
			throw new IllegalArgumentException("k is not a valid position in the list");
			
		}
		
		//creates a temp 2D array to avoid altering the original List
		Integer[][] listArrays = new Integer[list.size()][];
		
		for (int i = 0; i < list.size(); i++) {
			
			listArrays[i] = list.get(i);
			
		}
		
		//sorts the 2D array 
		insertionSort(listArrays, (bigIntArr1, bigIntArr2) -> findLargestNumber(bigIntArr2).compareTo(findLargestNumber(bigIntArr1)));
		
		return listArrays[k];
		
	}

	/**
	 * Generates a List of Integer arrays from the lines of a text file.
	 * 
	 * @param filename - the name of the file which is to be turned into a List
	 * @return a List of Integer arrays which represent the lines of the given file.
	 *         Returns an empty list in the case of an invalid file name.
	 */
	public static List<Integer[]> readFile(String filename) {
		
		List<Integer[]> numArrs = new ArrayList<Integer[]>();
		
		
		try {
			File file = new File(filename);
			Scanner fileScanner = new Scanner(file);
			
			while (fileScanner.hasNextLine()) {
				String lineNumbers = fileScanner.nextLine();
				Scanner lineCounterScanner = new Scanner(lineNumbers);
				Scanner lineReaderScanner = new Scanner(lineNumbers);
				int size = 0;
				
				//counts how many numbers in the current line
				while (lineCounterScanner.hasNextInt()) {
					
					lineCounterScanner.nextInt();
					size++;
					
				}
				
				Integer[] nums = new Integer[size];
			
				//loops through the line adding the numbers to the array
				int j = 0;
				while (lineReaderScanner.hasNextInt() && j < size) {
					nums[j] = lineReaderScanner.nextInt();
					j++;
				}
				
				//adds current array to overall List
				numArrs.add(nums);
				lineCounterScanner.close();
				lineReaderScanner.close();
			}
			fileScanner.close();
			
		} catch (FileNotFoundException e) {
			return numArrs;
		}
		
		return numArrs;
		
	}
	
}