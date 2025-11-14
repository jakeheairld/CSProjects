package assign06;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This is a tester class for SinglyLinkedList.
 * 
 * @author Jake Heairld and Anton Smolyanyy
 * @version Feb 29, 2024
 */  
class SinglyLinkedListTester {

	private SinglyLinkedList<Integer> emptyNumList, singleNumList, numList;
	private SinglyLinkedList<String> stringList;
	private Iterator<Integer> numIter;
	
	@BeforeEach
	void setup() {
		emptyNumList = new SinglyLinkedList<Integer>();
		
		singleNumList = new SinglyLinkedList<Integer>();
		singleNumList.insertFirst(10);
		
		numList = new SinglyLinkedList<Integer>();
		numList.insertFirst(1);
		numList.insert(1, 2);
		numList.insert(2, 3);
		numList.insert(3, 4);
		numList.insert(4, 5);
		
		stringList = new SinglyLinkedList<String>();
		stringList.insertFirst( "Dog");
		stringList.insert(1, "Doug");
		stringList.insert(2, "Cat");
		stringList.insert(3, "fish");
	}
	
	@Test
	void testInsertFirstEmpty() {
		emptyNumList.insertFirst(25);
		Integer[] expected = {25};
		assertEquals(Arrays.toString(expected), Arrays.toString(emptyNumList.toArray()));
	}
	
	@Test
	void testInsertFirstOneNum() {
		singleNumList.insertFirst(25);
		Integer[] expected = {25, 10};
		assertEquals(Arrays.toString(expected), Arrays.toString(singleNumList.toArray()));
	}
	
	@Test
	void testInsertFirstNumList() {
		numList.insertFirst(25);
		Integer[] expected = {25,1,2,3,4,5};
		assertEquals(Arrays.toString(expected), Arrays.toString(numList.toArray()));
	}
		
	@Test
	void testInsertFirstStringList() {
		stringList.insertFirst("Snowboard");
		String[] expected = {"Snowboard", "Dog", "Doug", "Cat", "fish"};
		assertEquals(Arrays.toString(expected), Arrays.toString(stringList.toArray()));
	}
	
	@Test
	void testInsertExceptionHighIndex() {
		 assertThrows(IndexOutOfBoundsException.class, ()-> numList.insert(6, 19));
	}
	
	@Test
	void testInsertExceptionLowIndex() {
		assertThrows(IndexOutOfBoundsException.class, ()-> numList.insert(-1, 19));
	}
	
	@Test
	void testInsertEmptyNum() {
		emptyNumList.insert(0, 25);
		Integer[] expected = {25};
		assertEquals(Arrays.toString(expected), Arrays.toString(emptyNumList.toArray()));
	}
	
	@Test
	void testInsertOneNum() {
		singleNumList.insert(1, 25);
		Integer[] expected = {10, 25};
		assertEquals(Arrays.toString(expected), Arrays.toString(singleNumList.toArray()));
	}
	
	@Test
	void testInsertNums() {
		numList.insert(2, 25);
		Integer[] expected = {1,2,25,3,4,5};
		assertEquals(Arrays.toString(expected), Arrays.toString(numList.toArray()));
	}
	
	@Test
	void testInsertStrings() {
		stringList.insert(2, "Shilo");
		String[] expected = {"Dog", "Doug", "Shilo", "Cat", "fish"};
		assertEquals(Arrays.toString(expected), Arrays.toString(stringList.toArray()));
	}
	
	@Test
	void testGetFirstEmpty() {
		assertThrows(NoSuchElementException.class, ()-> emptyNumList.getFirst());
	}
	
	@Test
	void testGetFirstSingleNum() {
		assertEquals(10, singleNumList.getFirst());
	}
	
	@Test
	void testGetFirstNums() {
		assertEquals(1, numList.getFirst());
	}
	
	@Test
	void testGetFirstStrings() {
		assertEquals("Dog", stringList.getFirst());
	}
	
	@Test
	void testGetExceptionLowBound() {
		assertThrows(IndexOutOfBoundsException.class, ()-> numList.get(-1));
	}
	
	@Test
	void testGetExceptionHighBound() {
		assertThrows(IndexOutOfBoundsException.class, ()-> numList.get(5));
	}
	
	@Test
	void testGetExceptionEmptyList() {
		assertThrows(IndexOutOfBoundsException.class, ()-> emptyNumList.get(0));
	}
	
	@Test
	void testGetNums() {
		assertEquals(4, numList.get(3));
	}
	
	@Test
	void testGetStrings() {
		assertEquals("Doug", stringList.get(1));
	}
	
	@Test
	void testDeleteFirstEmpty() {
		assertThrows(NoSuchElementException.class, ()-> emptyNumList.deleteFirst());
	}
	
	@Test
	void testDeleteFirstSingleNum() {
		assertEquals(singleNumList.deleteFirst(), 10);
		assertEquals(Arrays.toString(singleNumList.toArray()), Arrays.toString(new String[0]));
	}
	
	@Test
	void testDeleteFirstNums() {
		assertEquals(numList.deleteFirst(), 1);
		assertEquals(Arrays.toString(numList.toArray()), Arrays.toString(new Integer[] {2, 3, 4, 5}));
	}
	
	@Test
	void testDeleteFirstStrings() {
		assertEquals(stringList.deleteFirst(), "Dog");
		assertEquals(Arrays.toString(stringList.toArray()), Arrays.toString(new String[] {"Doug", "Cat", "fish"}));
	}
	
	@Test
	void testDeleteExceptionLowBound() {
		assertThrows(IndexOutOfBoundsException.class, ()-> numList.delete(-1));
	}
	
	@Test
	void testDeleteExceptionHighBound() {
		assertThrows(IndexOutOfBoundsException.class, ()-> numList.delete(5));
	}
	
	@Test
	void testDeleteExceptionEmpty() {
		assertThrows(IndexOutOfBoundsException.class, ()-> emptyNumList.delete(0));
	}
	
	@Test
	void testDeleteOneNum() {
		assertEquals(singleNumList.deleteFirst(), 10);
		assertEquals(Arrays.toString(singleNumList.toArray()), Arrays.toString(new String[0]));
	}
	
	@Test
	void testDeleteNums() {
		assertEquals(numList.delete(3), 4);
		assertEquals(Arrays.toString(new Integer[] {1, 2, 3, 5}), Arrays.toString(numList.toArray()));
	}
	
	@Test
	void testDeleteStrings() {
		assertEquals(stringList.delete(2), "Cat");
		assertEquals(Arrays.toString(new String[] {"Dog", "Doug", "fish"}), Arrays.toString(stringList.toArray()));
	}
	
	@Test
	void testIndexOfEmpty() {
		assertEquals(emptyNumList.indexOf(2), -1);
	}
	
	@Test
	void testIndexOfNumsNegOne() {
		assertEquals(numList.indexOf(26), -1);
	}
	
	@Test
	void testIndexOfNums() {
		assertEquals(2, numList.indexOf(3));
	}
	
	@Test
	void testIndexOfStringsNegOne() {
		assertEquals(stringList.indexOf("Python"), -1);
	}
	
	@Test
	void testIndexOfStrings() {
		assertEquals(stringList.indexOf("fish"), 3);
	}
	
	@Test
	void testSizeEmpty() {
		assertEquals(emptyNumList.size(), 0);
	}
	
	@Test
	void testSizeOneNum() {
		assertEquals(singleNumList.size(), 1);
	}
	
	@Test
	void testSizeNums() {
		assertEquals(numList.size(), 5);
	}
	
	@Test
	void testSizeStrings() {
		assertEquals(stringList.size(), 4);
	}
	
	@Test
	void testIsEmptyOnEmptyList() {
		assertTrue(emptyNumList.isEmpty());
	}
	
	@Test
	void testIsEmptyOnNonEmptyList() {
		assertFalse(numList.isEmpty());
	}
	
	@Test
	void testClearEmpty() {
		singleNumList.clear();
		assertEquals(Arrays.toString(singleNumList.toArray()), Arrays.toString(new Integer[0]));
		assertEquals(singleNumList.size(), 0);

	}
	
	@Test
	void testClearNums() {
		numList.clear();
		assertEquals(Arrays.toString(numList.toArray()), Arrays.toString(new Integer[0]));
		assertEquals(numList.size(), 0);
	}
	
	@Test
	void testToArrayEmpty() {
		Object[] expected = new Object[0];
		assertEquals(Arrays.toString(expected), Arrays.toString(emptyNumList.toArray()));
	} 
	
	@Test
	void testToArrayOneNum() {
		Object[] expected = {10};
		assertEquals(Arrays.toString(expected), Arrays.toString(singleNumList.toArray()));
	} 
	
	@Test
	void testToArrayNums() {
		Object[] expected = {1, 2, 3, 4, 5};
		assertEquals(Arrays.toString(expected), Arrays.toString(numList.toArray()));
	} 
	
	@Test
	void testToArrayStrings() {
		Object[] expected = {"Dog", "Doug", "Cat", "fish"};
		assertEquals(Arrays.toString(expected), Arrays.toString(stringList.toArray()));
	} 
	
	@Test
	void testHasNextIteratorEmptyNums() {
		numIter = emptyNumList.iterator();
		assertFalse(numIter.hasNext());
	}
	
	@Test
	void testHasNextIteratorNums() {
		numIter = numList.iterator();
		assertTrue(numIter.hasNext());
		numIter.next();
		numIter.next();
		numIter.next();
		numIter.next();
		assertTrue(numIter.hasNext());
		numIter.next();
		assertFalse(numIter.hasNext());
	}
	
	@Test
	void testNextIteratorEmptyNums() {
		numIter = emptyNumList.iterator();
		assertThrows(NoSuchElementException.class, () -> numIter.next());
	}
	
	@Test
	void testNextIteratorSingleNum() {
		numIter = singleNumList.iterator();
		assertEquals(10, numIter.next());
	}
	
	@Test
	void testNextIteratorNums() {
		numIter = numList.iterator();
		assertEquals(1, numIter.next());
		assertEquals(2, numIter.next());
		assertEquals(3, numIter.next());
		assertEquals(4, numIter.next());
		assertEquals(5, numIter.next());
	}
	
	@Test
	void testNextIteratorException() {
		numIter = numList.iterator();
		numIter.next();
		numIter.next();
		numIter.next();
		numIter.next();
		numIter.next();
		assertThrows(NoSuchElementException.class, () -> numIter.next());
	}
	
	@Test
	void testRemoveIteratorEmpty() {
		numIter = emptyNumList.iterator();
		assertThrows(IllegalStateException.class, () -> numIter.remove());
	}
	
	@Test
	void testRemoveIteratorSingleNum() {
		numIter = singleNumList.iterator();
		numIter.next();
		numIter.remove();
		assertEquals(singleNumList.size(), 0);
	}
	
	@Test
	void testRemoveSingleIteratorNums() {
		numIter = numList.iterator();
		numIter.next();
		numIter.next();
		numIter.next();
		numIter.remove();
		Object[] expected = {1, 2, 4, 5};
		assertEquals(Arrays.toString(expected), Arrays.toString(numList.toArray()));
	}
	
	@Test
	void testRemoveSingleLastIteratorNums() {
		numIter = numList.iterator();
		numIter.next();
		numIter.next();
		numIter.next();
		numIter.next();
		numIter.next();
		numIter.remove();
		Object[] expected = {1, 2, 3, 4};
		assertEquals(Arrays.toString(expected), Arrays.toString(numList.toArray()));
	}
	
	@Test
	void testRemoveMultipleIteratorNum() {
		numIter = numList.iterator();
		numIter.next();
		numIter.remove();
		numIter.next();
		numIter.remove();
		numIter.next();
		numIter.remove();
		numIter.next();
		numIter.next();
		Object[] expected = {4, 5};
		assertEquals(Arrays.toString(expected), Arrays.toString(numList.toArray()));
	}
	
	@Test
	void testRemoveMultipleIteratorAll() {
		numIter = numList.iterator();
		numIter.next();
		numIter.remove();
		numIter.next();
		numIter.remove();
		numIter.next();
		numIter.remove();
		numIter.next();
		numIter.remove();
		numIter.next();
		numIter.remove();
		Integer[] expected = new Integer[0];
		assertEquals(Arrays.toString(expected), Arrays.toString(numList.toArray()));
	}
	
}
