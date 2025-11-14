package assign10;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BinaryMaxHeapTest {

	private List<String> strList;
	private List<Integer> intList;
	private BinaryMaxHeap<Integer> intHeap, emptyIntHeap;
	private BinaryMaxHeap<String> strNatHeap, strCusHeap;
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
		intHeap = new BinaryMaxHeap<Integer>(intList);
		emptyIntHeap = new BinaryMaxHeap<Integer>();
		strNatHeap = new BinaryMaxHeap<String>(strList);
		strCusHeap = new BinaryMaxHeap<String>(strList, strLengthCmp);		
	}
	
	@Test
	void testBinaryMaxHeapEmptyInt() {
		assertEquals(0, emptyIntHeap.size());
	}

	@Test
	void testBinaryMaxHeapIntNatural() {
		Integer[] arr = new Integer[] {69, 14, 9, 12, -3, 2};
		assertEquals(Arrays.toString(arr), Arrays.toString(intHeap.toArray()));
	}   
	
	@Test
	void testBinaryMaxHeapStrNatural() {
		String[] arr = new String[] {"mountain", "dog", "Anton", "Jake", "cat"};
		assertEquals(Arrays.toString(arr), Arrays.toString(strNatHeap.toArray()));
	}
	
	@Test
	void testBinaryMaxHeapStrCustom() {
		String[] arr = new String[] {"mountain", "Jake", "Anton", "dog", "cat"};
		assertEquals(Arrays.toString(arr), Arrays.toString(strCusHeap.toArray()));
	}
	
	@Test
	void testAdd() {
		int prevSize = intHeap.size();
		intHeap.add(2);
		assertEquals(prevSize + 1, intHeap.size());
	}
	
	@Test
	void testPeekEmpty() {
		assertThrows(NoSuchElementException.class, ()-> emptyIntHeap.peek());
	}
	
	@Test
	void testPeek() {
		assertEquals(69, intHeap.peek());
	}
	
	@Test
	void testExtractMaxEmpty() {
		assertThrows(NoSuchElementException.class, ()-> emptyIntHeap.extractMax());
	}
	
	@Test
	void testExtractMaxValue() {
		int max = intHeap.extractMax();
		assertEquals(69, max);
	}
	
	@Test
	void testExtractMaxSize() {
		intHeap.extractMax();
		assertEquals(5, intHeap.size());
	}
	
	@Test
	void testIsEmptyTrue() {
		assertTrue(emptyIntHeap.isEmpty());
	}
	
	@Test
	void testIsEmptyFalse() {
		assertFalse(intHeap.isEmpty());
	}
	
	@Test
	void testClearEmpty() {
		assertTrue(emptyIntHeap.isEmpty());
		emptyIntHeap.clear();
		assertTrue(emptyIntHeap.isEmpty());
	}
	
	@Test
	void testClear() {
		assertFalse(intHeap.isEmpty());
		intHeap.clear();
		assertTrue(intHeap.isEmpty());
	}
	
	@Test
	void testToArrayEmpty() {
		Object[] arr = emptyIntHeap.toArray();
		assertEquals(0, arr.length);
	}
	
	@Test
	void testToArray() {
		Integer[] arr = new Integer[] {69, 14, 11, 12, -3, 2, 9};
		intHeap.add(11);
		assertEquals(Arrays.toString(arr), Arrays.toString(intHeap.toArray()));
	}
	
	@Test
	void testToArrayString() {
		String[] arr = new String[] {"mountain", "Jake", "Anton", "dog", "cat", "a"};
		strCusHeap.add("a");
		assertEquals(Arrays.toString(arr), Arrays.toString(strCusHeap.toArray()));
	}
}