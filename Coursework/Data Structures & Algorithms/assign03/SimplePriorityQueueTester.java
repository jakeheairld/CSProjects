package assign03;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This is a tester class for SimplePriorityQueue.
 * 
 * @author Jake Heairld and Anton Smolyanyy
 * @version Jan 30, 2024
 */
class SimplePriorityQueueTester {

	private SimplePriorityQueue<Integer> emptyQueue, intQueue, largeIntQueue;
	private SimplePriorityQueue<String> stringNaturalQueue, stringCustomQueue;
	
	private Comparator<String> stringCmd;
	private Set<Integer> manyNums;
	
	@BeforeEach
	void setUp() {
		stringCmd = (str1, str2) -> (str1.length() - str2.length());
		intQueue = new SimplePriorityQueue<Integer>();
		largeIntQueue = new SimplePriorityQueue<Integer>();
		emptyQueue = new SimplePriorityQueue<Integer>();
		stringNaturalQueue = new SimplePriorityQueue<String>();
		stringCustomQueue = new SimplePriorityQueue<String>(stringCmd);
		
		ArrayList<String> words = new ArrayList<String>();
		words.add("Anton");
		words.add("Alfredo");
		words.add("Jake");
		words.add("apple");
		words.add("100DogsAndCats");
		words.add("Zebra");
		stringNaturalQueue.insertAll(words);
		stringCustomQueue.insertAll(words);
		
		manyNums = new TreeSet<Integer>();

		for(int i = 1000; i>0; i--) {
			manyNums.add(i);
		}
		
		largeIntQueue.insertAll(manyNums);
		
		intQueue.insert(3);
		intQueue.insert(-1);
		intQueue.insert(2);
		intQueue.insert(38);
	}
	
	@Test
	void testFindMaxEmptyQueue() {
		assertThrows(NoSuchElementException.class, ()-> emptyQueue.findMax());
	}
	
	@Test
	void testFindMaxInteger() {
		assertEquals(38, intQueue.findMax());
	}
	
	@Test
	void testFindMaxStringNatural() {
		assertEquals("apple", stringNaturalQueue.findMax());
	}
	
	@Test
	void testFindMaxStringCustom() {
		assertEquals("100DogsAndCats", stringCustomQueue.findMax());
	}

	@Test
	void testFindMaxDoubleElemNatural() {
		stringNaturalQueue.insert("apple");
		assertEquals("apple", stringNaturalQueue.findMax());
	}
	
	@Test
	void testFindMaxDoubleElemCustom() {
		stringCustomQueue.insert("100DogsAndCats");
		assertEquals("100DogsAndCats", stringCustomQueue.findMax());
	}
	
	@Test
	void testFindMaxLargeIntQueue() {
		assertEquals(1000, largeIntQueue.findMax());
	}
	
	@Test
	void testDeleteMaxEmptyQueue() {
		assertThrows(NoSuchElementException.class, ()-> emptyQueue.deleteMax());
	}
	
	@Test
	void testDeleteMaxIntegerQueue() {
		assertEquals(38, intQueue.findMax());
		intQueue.deleteMax();
		assertEquals(3, intQueue.findMax());
	}
	
	@Test
	void testDeleteMaxIntegerQueueDoubleMax() {
		intQueue.insert(38);
		intQueue.deleteMax();
		assertEquals(38, intQueue.findMax());
	}
	
	@Test
	void testDeleteMaxIntegerCheckSize() {
		int prevSize = intQueue.size();	
		intQueue.deleteMax();
		assertEquals(prevSize-1, intQueue.size());		
	}
	
	@Test
	void testDeleteMaxStringNaturalQueue() {
		assertEquals("apple", stringNaturalQueue.findMax());
		stringNaturalQueue.deleteMax();
		assertEquals("Zebra", stringNaturalQueue.findMax());
	}
	
	@Test
	void testDeleteMaxStringNaturalQueueDoubleMax() {
		stringNaturalQueue.insert("apple");
		stringNaturalQueue.deleteMax();
		assertEquals("apple", stringNaturalQueue.findMax());
	}
	
	@Test
	void testDeleteMaxStringNaturalCheckSize() {
		int prevSize = stringNaturalQueue.size();	
		stringNaturalQueue.deleteMax();
		assertEquals(prevSize-1, stringNaturalQueue.size());		
	}
	
	
	@Test
	void testDeleteMaxStringCustomQueue() {
		assertEquals("100DogsAndCats", stringCustomQueue.findMax());
		stringCustomQueue.deleteMax();
		assertEquals("Alfredo", stringCustomQueue.findMax());
	}
	
	@Test
	void testDeleteMaxStringCustomQueueDoubleMax() {
		stringCustomQueue.insert("100DogsAndCats");
		stringCustomQueue.deleteMax();
		assertEquals("100DogsAndCats", stringCustomQueue.findMax());
	}
	
	@Test
	void testDeleteMaxStringCustomCheckSize() {
		int prevSize = stringCustomQueue.size();	
		stringCustomQueue.deleteMax();
		assertEquals(prevSize-1, stringCustomQueue.size());		
	}
	
	@Test
	void testDeleteMaxLargeIntQueueCheckSize() {
		int prevSize = largeIntQueue.size();	
		largeIntQueue.deleteMax();
		assertEquals(prevSize-1, largeIntQueue.size());		
	}
	
	@Test
	void testDeleteMaxLargeIntQueue() {
		assertEquals(1000, largeIntQueue.findMax());
		largeIntQueue.deleteMax();
		assertEquals(999, largeIntQueue.findMax());
	}
	
	@Test
	void testInsertChangeSizeInteger() {
		int prevSize = intQueue.size();	
		intQueue.insert(7);
		assertEquals(prevSize+1, intQueue.size());		
	}
	
	@Test
	void testInsertChangeSizeString() {
		int prevSize = stringNaturalQueue.size();	
		stringNaturalQueue.insert("Bob");
		assertEquals(prevSize+1, stringNaturalQueue.size());		
	}
	
	@Test
	void testInsertAllChangeSizeInteger() {
		int prevSize = intQueue.size();	
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		numbers.add(99);
		numbers.add(99);
		numbers.add(0);
		numbers.add(-1);
		intQueue.insertAll(numbers);
		assertEquals(prevSize + numbers.size(), intQueue.size());		
	}
	
	@Test
	void testInsertAllChangeSizeString() {
		int prevSize = stringNaturalQueue.size();	
		ArrayList<String> names = new ArrayList<String>();
		names.add("Billy");
		names.add("Billy");
		names.add("Rosie");
		names.add("Aaron");
		stringNaturalQueue.insertAll(names);
		assertEquals(prevSize + names.size(), stringNaturalQueue.size());		
	}

	@Test
	void testContainsEmptyQueue() {
		assertFalse(emptyQueue.contains(5));
	}
	
	@Test
	void testContainsIntQueueTrue() {
		assertTrue(intQueue.contains(38));
	}
	
	@Test
	void testContainsIntQueueFalse() {
		assertFalse(intQueue.contains(800));
	}
	
	@Test
	void testContainsStringNaturalQueueTrue() {
		assertTrue(stringNaturalQueue.contains("Jake"));
	}
	
	@Test
	void testContainsStringNaturalQueueFalse() {
		assertFalse(stringNaturalQueue.contains("snowboard"));
	}
	
	@Test
	void testContainsStringCustomQueueTrue() {
		assertTrue(stringCustomQueue.contains("Jake"));
	}
	
	@Test
	void testContainsStringCustomQueueFalse() {
		assertFalse(stringCustomQueue.contains("snowboard"));
	}

	@Test
	void testSizeEmptyQueue() {
		assertEquals(0, emptyQueue.size());
	}
	
	@Test
	void testSizeIntQueue() {
		assertEquals(4, intQueue.size());
	}
	
	@Test
	void testSizeStringNaturalQueue() {
		assertEquals(6, stringNaturalQueue.size());
	}
	
	@Test
	void testContainsStringCustomQueue() {
		assertEquals(6, stringCustomQueue.size());

	}

	@Test
	void testIsEmptyEmptyQueue() {
		assertTrue(emptyQueue.isEmpty());
	}
	
	@Test
	void testIsEmptyIntQueue() {
		assertFalse(intQueue.isEmpty());
	}
	
	@Test
	void testRemoveElemsUntilEmpty() {
		int iterations = intQueue.size();
		for (int i = 0; i < iterations; i++) {
			intQueue.deleteMax();
		}

		assertTrue(intQueue.isEmpty());
	}
	
	@Test
	void testRemoveElemsUntilEmptyLargeQueue() {
		int iterations = largeIntQueue.size();
		for (int i = 0; i < iterations; i++) {
			largeIntQueue.deleteMax();
		}

		assertTrue(largeIntQueue.isEmpty());
	}

	@Test
	void testClearEmptyQueue() {
		emptyQueue.clear();
		assertEquals(0, emptyQueue.size());
	}
	
	@Test
	void testClearIntQueue() {
		intQueue.clear();
		assertEquals(0, intQueue.size());	
	}
	
	@Test
	void testClearStringQueue() {
		stringNaturalQueue.clear();
		assertEquals(0, stringNaturalQueue.size());	
	}
	
	@Test
	void testClearLargeIntQueue() {
		largeIntQueue.clear();
		assertEquals(0, largeIntQueue.size());	
	}
}
