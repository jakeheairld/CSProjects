package assign10;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FindKLargestTest {

	private List<String> strList, strEmptyList;
	private List<Integer> intList, intEmptyList;
	private Comparator<String> strLengthCmp;
	
	@BeforeEach 
	void setup() {
		strLengthCmp = (str1, str2) -> str1.length()-str2.length();
		intList = new ArrayList<Integer>();
		intList.add(-3);
		intList.add(12);
		intList.add(9);
		intList.add(69);
		intList.add(14);
		intList.add(2);
		strList = new ArrayList<String>();
		strList.add("Anton");
		strList.add("dog");
		strList.add("mountain");
		strList.add("Jake");
		strList.add("cat");
		intEmptyList = new ArrayList<Integer>();
		strEmptyList = new ArrayList<String>();
	}
	
	@Test
	void findKLargestHeapExceptionKNeg() {
		 assertThrows(IllegalArgumentException.class, ()-> FindKLargest.findKLargestHeap(intList, -1));
	}
	
	@Test
	void findKLargestHeapExceptionKPos() {
		 assertThrows(IllegalArgumentException.class, ()-> FindKLargest.findKLargestHeap(intList, 11));
	}
	
	@Test
	void findKLargestHeapKZero() {
		assertEquals(new ArrayList<Integer>(), FindKLargest.findKLargestHeap(intList, 0));
	}
	
	@Test
	void findKLargestHeapEmptyList() {
		assertEquals(new ArrayList<Integer>(), FindKLargest.findKLargestHeap(intEmptyList, 0));
	}
	
	@Test 
	void findKLargestHeapInts() {
		ArrayList<Integer> nums = new ArrayList<Integer>();
		nums.add(69);
		nums.add(14);
		nums.add(12);
		nums.add(9);
		
		assertEquals(nums, FindKLargest.findKLargestHeap(intList, 4));
	}
	
	@Test 
	void findKLargestHeapStrs() {
		ArrayList<String> words = new ArrayList<String>();
		words.add("mountain");
		words.add("dog");
		words.add("cat");
		words.add("Jake");
		
		assertEquals(words, FindKLargest.findKLargestHeap(strList, 4));
	}
			
	@Test
	void findKLargestHeapCMPExceptionKNeg() {
		 assertThrows(IllegalArgumentException.class, ()-> FindKLargest.findKLargestHeap(strList, -1, strLengthCmp));
	}
	
	@Test
	void findKLargestHeapCMPExceptionKPos() {
		 assertThrows(IllegalArgumentException.class, ()-> FindKLargest.findKLargestHeap(strList, 11, strLengthCmp));
	}
	
	@Test
	void findKLargestHeapCMPKZero() {
		assertEquals(new ArrayList<Integer>(), FindKLargest.findKLargestHeap(strList, 0, strLengthCmp));
	}
	
	@Test
	void findKLargestHeapCMPEmptyList() {
		assertEquals(new ArrayList<Integer>(), FindKLargest.findKLargestHeap(strEmptyList, 0, strLengthCmp));
	}
	
	@Test 
	void findKLargestHeapCMPStrs() {
		ArrayList<String> words = new ArrayList<String>();
		words.add("mountain");
		words.add("Anton");
		words.add("Jake");
		
		assertEquals(words, FindKLargest.findKLargestHeap(strList, 3, strLengthCmp));
	}
	
	@Test
	void findKLargestSortExceptionKNeg() {
		 assertThrows(IllegalArgumentException.class, ()-> FindKLargest.findKLargestSort(intList, -1));
	}
	
	@Test
	void findKLargestSortExceptionKPos() {
		 assertThrows(IllegalArgumentException.class, ()-> FindKLargest.findKLargestSort(intList, 11));
	}
	
	@Test
	void findKLargestSortKZero() {
		assertEquals(new ArrayList<Integer>(), FindKLargest.findKLargestSort(intList, 0));
	}
	
	@Test
	void findKLargestSortEmptyList() {
		assertEquals(new ArrayList<Integer>(), FindKLargest.findKLargestSort(intList, 0));
	}
	
	@Test 
	void findKLargestSortInts() {
		ArrayList<Integer> nums = new ArrayList<Integer>();
		nums.add(69);
		nums.add(14);
		nums.add(12);
		nums.add(9);
		
		assertEquals(nums, FindKLargest.findKLargestSort(intList, 4));
	}
	
	@Test 
	void findKLargestSortStrs() {
		ArrayList<String> words = new ArrayList<String>();
		words.add("mountain");
		words.add("dog");
		words.add("cat");
		words.add("Jake");
		
		assertEquals(words, FindKLargest.findKLargestSort(strList, 4));
	}
	
	@Test
	void findKLargestSortCMPExceptionKNeg() {
		 assertThrows(IllegalArgumentException.class, ()-> FindKLargest.findKLargestSort(strList, -1, strLengthCmp));
	}
	
	@Test
	void findKLargestSortCMPExceptionKPos() {
		 assertThrows(IllegalArgumentException.class, ()-> FindKLargest.findKLargestSort(strList, 11, strLengthCmp));
	}
	
	@Test
	void findKLargestSortCMPKZero() {
		assertEquals(new ArrayList<Integer>(), FindKLargest.findKLargestSort(strList, 0, strLengthCmp));
	}
	
	@Test
	void findKLargestSortCMPEmptyList() {
		assertEquals(new ArrayList<Integer>(), FindKLargest.findKLargestSort(strEmptyList, 0, strLengthCmp));
	}
	
	@Test 
	void findKLargestSortCMPStrs() {
		ArrayList<String> words = new ArrayList<String>();
		words.add("mountain");
		words.add("Anton");
		words.add("Jake");
		
		assertEquals(words, FindKLargest.findKLargestSort(strList, 3, strLengthCmp));
	}
}