package assign05;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This is a tester class for ArrayListSorter.
 * 
 * @author Jake Heairld and Anton Smolyanyy
 * @version Feb 21, 2024
 */  
class ArrayListSorterTester {

	private ArrayList<Integer> empty;
	private ArrayList<Integer> oneNum;
	private ArrayList<Integer> numsAscending;
	private ArrayList<Integer> numsDescending;
	private ArrayList<Integer> numsRandom;
	private ArrayList<String> oneWord;
	private ArrayList<String> words;
	
	@BeforeEach
	void setUp() {
		empty = new ArrayList<Integer>();
		oneNum = new ArrayList<Integer>();
		numsAscending = ArrayListSorter.generateAscending(30);
		numsDescending = ArrayListSorter.generateDescending(30);
		numsRandom = ArrayListSorter.generatePermuted(30);

		oneWord = new ArrayList<String>();
		words = new ArrayList<String>();
		
		oneNum.add(5);
		oneWord.add("Snowboard");
		words.add("Anton");
		words.add("Alfredo");
		words.add("Jake");
		words.add("apple");
		words.add("100DogsAndCats");
		words.add("Zebra");
	}
	
	@Test 
	void testGenerateAscending() {
		ArrayList<Integer> expected = new ArrayList<Integer>();
		expected.add(1);		
		expected.add(2);
		expected.add(3);
		expected.add(4);
		expected.add(5);
		assertEquals(expected, ArrayListSorter.generateAscending(5));
	}
	
	@Test 
	void testGenerateDescending() {
		ArrayList<Integer> expected = new ArrayList<Integer>();
		expected.add(5);		
		expected.add(4);
		expected.add(3);
		expected.add(2);
		expected.add(1);
		assertEquals(expected, ArrayListSorter.generateDescending(5));
	}
	
	@Test 
	void testGeneratePermuted() {
		assertEquals(5, ArrayListSorter.generatePermuted(5).size());
	}
	
	@Test
	void testMergeSortEmpty() {
		ArrayListSorter.mergesort(empty);
		assertEquals(empty, new ArrayList<Integer>());
	}
	
	@Test
	void testMergeSortOneNum() {
		ArrayList<Integer> oneNumBefore = oneNum;
		ArrayListSorter.mergesort(oneNum);
		assertEquals(oneNum, oneNumBefore);
	}
	
	@Test
	void testMergeSortNumsAscending() {
		ArrayList<Integer> numsAscBefore = numsAscending;
		ArrayListSorter.mergesort(numsAscending);
		assertEquals(numsAscending, numsAscBefore);
	}
	
	@Test
	void testMergeSortNumsDescending() {
		ArrayList<Integer> expected = numsAscending;
		ArrayListSorter.mergesort(numsDescending);
		assertEquals(expected, numsDescending);
	}
	
	@Test
	void testMergeSortNumsPermuted() {
		ArrayList<Integer> expected = numsAscending;
		ArrayListSorter.mergesort(numsRandom);
		assertEquals(expected, numsRandom);
	}
	
	@Test
	void testMergeSortOneStr() {
		ArrayList<String> oneWordBefore = oneWord;
		ArrayListSorter.mergesort(oneWord);
		assertEquals(oneWordBefore, oneWord);
	}
	
	@Test
	void testMergeSortWords() {
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("100DogsAndCats");
		expected.add("Alfredo");
		expected.add("Anton");
		expected.add("Jake");
		expected.add("Zebra");
		expected.add("apple");
		ArrayListSorter.mergesort(words);
		assertEquals(expected, words);
	}
	
	@Test
	void testQuickSortEmpty() {
		ArrayListSorter.quicksort(empty);
		assertEquals(empty, new ArrayList<Integer>());
	}
	
	@Test
	void testQuickSortOneNum() {
		ArrayList<Integer> oneNumBefore = oneNum;
		ArrayListSorter.quicksort(oneNum);
		assertEquals(oneNum, oneNumBefore);
	}
	
	@Test
	void testQuickSortNumsAscending() {
		ArrayList<Integer> numsAscBefore = numsAscending;
		ArrayListSorter.quicksort(numsAscending);
		assertEquals(numsAscending, numsAscBefore);
	}
	
	@Test
	void testQuickSortNumsDescending() {
		ArrayList<Integer> expected = numsAscending;
		ArrayListSorter.quicksort(numsDescending);
		assertEquals(expected, numsDescending);
	}
	
	@Test
	void testQuickSortNumsPermuted() {
		ArrayList<Integer> expected = numsAscending;
		ArrayListSorter.quicksort(numsRandom);
		assertEquals(expected, numsRandom);
	}
	
	@Test
	void testQuickSortOneStr() {
		ArrayList<String> oneWordBefore = oneWord;
		ArrayListSorter.quicksort(oneWord);
		assertEquals(oneWordBefore, oneWord);
	}
	
	@Test
	void testQuickSortWords() {
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("100DogsAndCats");
		expected.add("Alfredo");
		expected.add("Anton");
		expected.add("Jake");
		expected.add("Zebra");
		expected.add("apple");
		ArrayListSorter.quicksort(words);
		assertEquals(expected, words);
	}
}