package assign06;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This is a tester class for LinkedListTester.
 * 
 * @author Jake Heairld and Anton Smolyanyy
 * @version Feb 29, 2024
 */  
class LinkedListStackTester {

	private LinkedListStack<Integer> emptyNumStack, singleNumStack, numStack;
	private LinkedListStack<String> stringStack;
	
	@BeforeEach
	void setup() {
		emptyNumStack = new LinkedListStack<Integer>();
		
		singleNumStack = new LinkedListStack<Integer>();
		singleNumStack.push(10);

		numStack = new LinkedListStack<Integer>();
		numStack.push(1);
		numStack.push(2);
		numStack.push(3);
		numStack.push(4);
		numStack.push(5);
		
		stringStack = new LinkedListStack<String>();
		stringStack.push("McLaren");
		stringStack.push("Jake");
		stringStack.push("Anton");
		stringStack.push("KVille");
		stringStack.push("Brighton");		
	}
	
	@Test
	void testIsEmptyOnEmptyStack() {
		assertTrue(emptyNumStack.isEmpty());
	}
	
	@Test
	void testIsEmptyOneNumStack() {
		assertFalse(singleNumStack.isEmpty());
	}
	
	@Test
	void testIsEmptyNumStack() {
		assertFalse(numStack.isEmpty());
	}
	
	@Test
	void testIsEmptyStringStack() {
		assertFalse(stringStack.isEmpty());
	}
	
	@Test
	void testClearEmpty() {
		emptyNumStack.clear();
		assertTrue(emptyNumStack.isEmpty());
	}
	
	@Test
	void testClearOneNum() {
		singleNumStack.clear();
		assertTrue(singleNumStack.isEmpty());
	}
	
	@Test
	void testClearNums() {
		numStack.clear();
		assertTrue(numStack.isEmpty());
	}
	
	@Test
	void testClearStrings() {
		stringStack.clear();
		assertTrue(stringStack.isEmpty());
	}
	
	@Test
	void testSizeEmptyStack() {
		assertEquals(0, emptyNumStack.size());
	}
	
	@Test
	void testSizeOneNum() {
		assertEquals(1, singleNumStack.size());
	}
	
	@Test
	void testSizeNums() {
		assertEquals(5, numStack.size());
	}
	
	@Test
	void testSizeStrings() {
		assertEquals(5, stringStack.size());
	}
	
	@Test 
	void testPeekEmptyException() {
		assertThrows(NoSuchElementException.class, ()-> emptyNumStack.peek());
	}
	
	@Test 
	void testPeekOneNum() {
		assertEquals(10, singleNumStack.peek());
	}
	
	@Test 
	void testPeekNums() {
		assertEquals(5, numStack.peek());
	}
	
	@Test 
	void testPeekStrings() {
		assertEquals("Brighton", stringStack.peek());
	}
	
	@Test 
	void testPushEmpty() {
		emptyNumStack.push(6);
		assertEquals(6, emptyNumStack.peek());
		assertEquals(1, emptyNumStack.size());
	}
	
	@Test 
	void testPushOneNum() {
		singleNumStack.push(6);
		assertEquals(6, singleNumStack.peek());
		assertEquals(2, singleNumStack.size());
	}
	
	@Test 
	void testPushNums() {
		int sizeBefore = numStack.size();
		numStack.push(6);
		assertEquals(6, numStack.peek());
		assertEquals(sizeBefore + 1, numStack.size());
	}
	
	@Test 
	void testPushStrings() {
		int sizeBefore = stringStack.size();
		stringStack.push("recursion");
		assertEquals("recursion", stringStack.peek());
		assertEquals(sizeBefore + 1, stringStack.size());
	}
	
	@Test 
	void testPopEmptyException() {
		assertThrows(NoSuchElementException.class, ()-> emptyNumStack.pop());
	}
	
	@Test 
	void testPopOneNum() {
		assertEquals(10, singleNumStack.pop());
		assertEquals(0, singleNumStack.size());
		
		assertThrows(NoSuchElementException.class, ()-> singleNumStack.pop());

	}
	
	@Test 
	void testPopNums() {
		assertEquals(5, numStack.pop());
		assertEquals(4, numStack.size());
		
		assertEquals(4, numStack.pop());
		assertEquals(3, numStack.size());
		
	}
	
	@Test 
	void testPopStrings() {
		assertEquals("Brighton", stringStack.pop());
		assertEquals(4, stringStack.size());
		
		assertEquals("KVille", stringStack.pop());
		assertEquals(3, stringStack.size());
	}
	
}
