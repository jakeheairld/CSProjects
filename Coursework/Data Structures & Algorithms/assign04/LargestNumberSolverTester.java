package assign04;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *This is a tester class for LargestNumberSolver.
 * 
 * @author Jake Heairld and Anton Smolyanyy
 * @version February 08, 2024
 */
class LargestNumberSolverTester {

	private Integer[] emptyArr, sortedIntArr, unsortedIntArr;
	private Comparator<Integer> smallToLarge, largeToSmall;
	private String[] sortedStringArrLex, sortedStringArrLength, unsortedStringArrLex, unsortedStringArrLength;
	private Comparator<String> strByLength, strByLex;
	private List<Integer[]> intArrs;

	
	@BeforeEach
	void setUp() {
		emptyArr = new Integer[0];
		sortedIntArr = new Integer[] {-8, 0, 12, 100};
		unsortedIntArr = new Integer[] {56, -3, 9, 43, 13, 0, -5};
		smallToLarge = (int1, int2) -> int1 - int2;
		largeToSmall = (int1, int2) -> int2 - int1;
		
		sortedStringArrLex = new String[] {"100DogsAndCats", "Alphabet", "Apple", "snowboard"};
		unsortedStringArrLex = new String[] {"Alphabet", "snowboard", "100DogsAndCats", "Apple"};
		sortedStringArrLength = new String[] {"", "Apple", "Alphabet", "snowboard", "100DogsAndCats"};
		unsortedStringArrLength = new String[] {"Alphabet", "snowboard", "", "100DogsAndCats", "Apple"};

		strByLex = (str1, str2) -> str1.compareTo(str2);
		strByLength = (str1, str2) -> str1.length() - str2.length();
		
		intArrs = new ArrayList<Integer[]>();
		intArrs.add(new Integer[] {7,11,98});
		intArrs.add(new Integer[] {7,11,984561,0});
		intArrs.add(new Integer[] {7,11,9880, 767});
		intArrs.add(new Integer[] {7});

	}

	@Test
	void testInsertionSortEmptySize() {
		LargestNumberSolver.insertionSort(emptyArr, smallToLarge);
		assertEquals(emptyArr.length, 0);
	}
	
	@Test
	void testInsertionSortSortedInteger() {
		String arrBeforeSort = Arrays.toString(sortedIntArr);
		LargestNumberSolver.insertionSort(sortedIntArr, smallToLarge);
		assertEquals(Arrays.toString(sortedIntArr), arrBeforeSort);
	}
	
	@Test
	void testInsertionSortUnsortedIntegerSmallToLarge() {
		String arrAfterSort = "[-5, -3, 0, 9, 13, 43, 56]";
		LargestNumberSolver.insertionSort(unsortedIntArr, smallToLarge);
		assertEquals(Arrays.toString(unsortedIntArr), arrAfterSort);
	}
	
	@Test
	void testInsertionSortUnsortedIntegerLargeToSmall() {
		String arrAfterSort = "[56, 43, 13, 9, 0, -3, -5]";
		LargestNumberSolver.insertionSort(unsortedIntArr, largeToSmall);
		assertEquals(Arrays.toString(unsortedIntArr), arrAfterSort);
	}
	
	@Test
	void testInsertionSortSortedStringLex() {
		String arrBeforeSort = Arrays.toString(sortedStringArrLex);
		LargestNumberSolver.insertionSort(sortedStringArrLex, strByLex);
		assertEquals(Arrays.toString(sortedStringArrLex), arrBeforeSort);
	}
	
	@Test
	void testInsertionSortUnsortedStringLex() {
		LargestNumberSolver.insertionSort(unsortedStringArrLex, strByLex);
		assertEquals(Arrays.toString(unsortedStringArrLex), Arrays.toString(sortedStringArrLex));
	}
	
	@Test
	void testInsertionSortSortedStringLength() {
		String arrBeforeSort = Arrays.toString(sortedStringArrLength);
		LargestNumberSolver.insertionSort(sortedStringArrLength, strByLength);
		assertEquals(Arrays.toString(sortedStringArrLength), arrBeforeSort);
	}
	
	@Test
	void testInsertionSortUnsortedStringLength() {
		LargestNumberSolver.insertionSort(unsortedStringArrLength, strByLength);
		assertEquals(Arrays.toString(unsortedStringArrLength), Arrays.toString(sortedStringArrLength));
	}
		 
	@Test 
	void testFindLargestNumberEmpty() {
		assertEquals(BigInteger.ZERO, LargestNumberSolver.findLargestNumber(emptyArr));
	}
	
	@Test 
	void testFindLargestNumberOneElem() {
		Integer[] oneElem = new Integer[] {7};
		assertEquals(new BigInteger("7"), LargestNumberSolver.findLargestNumber(oneElem));
	}
	
	 @Test 
	 void testFindLargestNumberRegular() {
		 Integer[] nums = new Integer[]	{11, 67, 79, 7, 22, 13};
		 assertEquals(new BigInteger("79767221311"), LargestNumberSolver.findLargestNumber(nums));
	 }
	 
	 @Test 
	 void testFindLargestNumberDoesNotAlterArray() {
		 Integer[] nums = new Integer[]	{11, 67, 79, 7, 22, 13};
		 String before = Arrays.toString(nums);
		 LargestNumberSolver.findLargestNumber(nums);
		 assertEquals(before, Arrays.toString(nums));
	 }
	 
	 @Test
	 void findLargestIntEmpty() {
		 assertEquals(0, LargestNumberSolver.findLargestInt(emptyArr));
	 }
	 
	 @Test
	 void findLargestIntOneElem() {
	 	 Integer[] oneElem = new Integer[] {75};
		 assertEquals(75, LargestNumberSolver.findLargestInt(oneElem));
	 }
	 
	 @Test
	 void findLargestIntInsideBounds() {
		 Integer[] nums = new Integer[] {7, 11, 4};
		 assertEquals(7411, LargestNumberSolver.findLargestInt(nums));
	 }
	 
	 @Test
	 void findLargestIntOutsideBounds() {
		 Integer[] nums = new Integer[]	{11, 67, 79, 7, 22, 13};
		 assertThrows(OutOfRangeException.class, ()-> LargestNumberSolver.findLargestInt(nums));
	 }
	 
	 @Test
	 void findLargestLongEmpty() {
		 assertEquals(0, LargestNumberSolver.findLargestLong(emptyArr));
	 }
	 
	 @Test
	 void findLargestLongOneElem() {
	 	 Integer[] oneElem = new Integer[] {75};
		 assertEquals(75, LargestNumberSolver.findLargestLong(oneElem));
	 }
	 
	 @Test
	 void findLargestLongInsideBounds() {
		 Integer[] nums = new Integer[] {7, 11, 4};
		 assertEquals(7411, LargestNumberSolver.findLargestLong(nums));
	 }
	 
	 @Test
	 void findLargestLongOutsideBounds() {
		 Integer[] nums = new Integer[] {78800, 14561, 31794, 54544, 2034, 653, 4};
		 assertThrows(OutOfRangeException.class, ()-> LargestNumberSolver.findLargestLong(nums));
	 }
	 
	 @Test
	 void findLargestLongBetweenIntAndLongBounds() {
		 Integer[] nums = new Integer[]	{11, 67, 79, 7, 22, 13};
		 assertEquals(79767221311L, LargestNumberSolver.findLargestLong(nums));
	 }
	 
	 @Test
	 void findSumEmpty() {
		 assertEquals(BigInteger.ZERO, LargestNumberSolver.sum(new ArrayList<Integer[]>()));
	 }
	 
	 @Test
	 void findSumOneElemList() {
		 List<Integer[]> intArrsOneElem = new ArrayList<Integer[]>();
		 intArrsOneElem.add(new Integer[] {4, 55, 1});
 		 assertEquals(new BigInteger("5541"), LargestNumberSolver.sum(intArrsOneElem));
	 }
	 
	 @Test
	 void findSumNormal(){
		 assertEquals(new BigInteger("19726492539"), LargestNumberSolver.sum(intArrs));		 
	 }
	 
	 @Test 
	 void findKthLargestLowerBoundException() {
		 assertThrows(IllegalArgumentException.class, ()-> LargestNumberSolver.findKthLargest(intArrs, -1));
	 }
	 
	 @Test 
	 void findKthLargestUpperBoundException() {
		 assertThrows(IllegalArgumentException.class, ()-> LargestNumberSolver.findKthLargest(intArrs, 4));
	 }
	 
	 @Test
	 void findKthLargestEmptyArr() {
		 List<Integer[]> intArrsEmpty = new ArrayList<Integer[]>();
		 assertThrows(IllegalArgumentException.class, ()-> LargestNumberSolver.findKthLargest(intArrsEmpty, 0));
	 }
	 
	 @Test
	 void findKthLargestOneElemArr() {
		 List<Integer[]> intArrsOneElem = new ArrayList<Integer[]>();
		 intArrsOneElem.add(new Integer[] {4, 55, 1});
		 String expected = Arrays.toString(new Integer[] {4, 55, 1});
		 String actual = Arrays.toString(LargestNumberSolver.findKthLargest(intArrsOneElem, 0));
		 assertEquals(expected, actual);
	 }
	 
	 @Test 
	 void findKthLargestLargestNumber() {
		 String expected = Arrays.toString(new Integer[] {7,11,9880, 767});
		 String actual = Arrays.toString(LargestNumberSolver.findKthLargest(intArrs, 0));
		 assertEquals(expected, actual);
	 }
	 
	 @Test 
	 void findKthLargestSmallestNumber() {
		 String expected = Arrays.toString(new Integer[] {7});
		 String actual = Arrays.toString(LargestNumberSolver.findKthLargest(intArrs, 3));
		 assertEquals(expected, actual);
	 }
	 
	 @Test 
	 void readFileCatchesException() {
		 assertEquals(0, LargestNumberSolver.readFile("integersWrong.txt").size());
	 }
	 
	 @Test 
	 void readFileEmptyFile() {
		 assertEquals(0, LargestNumberSolver.readFile("src/assign04/emptyFile.txt").size());
	 }
	 
	 @Test 
	 void readFileCorrectlySetsList() {
		 assertEquals(903, LargestNumberSolver.readFile("src/assign04/integers.txt").size());
	 }
	 
}