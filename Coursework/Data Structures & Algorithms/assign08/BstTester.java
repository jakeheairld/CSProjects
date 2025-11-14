package assign08;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BstTester {
	
	private BinarySearchTree<Integer> testerBST;
	private BinarySearchTree<Integer> bst2;
	private SpellChecker spc;
	
	@BeforeEach
	void setup() {
		testerBST = new BinarySearchTree<Integer>();
		bst2 = new BinarySearchTree<Integer>();
		spc = new SpellChecker();
	}
	
	@Test
	void test() {
		assertTrue(testerBST.add(3));
		assertTrue(testerBST.add(10));
		assertFalse(testerBST.add(10));
		testerBST.add(11);
		testerBST.add(2);
		//BST with 3 10 11 2
		assertTrue(testerBST.contains(11));
		assertTrue(testerBST.contains(10));
		assertTrue(testerBST.contains(3));
		assertTrue(testerBST.contains(2));
		assertFalse(testerBST.contains(0));
		assertFalse(testerBST.contains(120));
		assertEquals(2, testerBST.first());
		assertEquals(11, testerBST.last());
		
		assertTrue(testerBST.remove(2));
		assertFalse(testerBST.contains(2));
		assertTrue(testerBST.remove(10));
		assertFalse(testerBST.contains(10));
		
	}
	
	@Test
	void testComplexTreeRemove() {
		testerBST.add(20);
		testerBST.add(25);
		testerBST.add(10);
		testerBST.add(30);
		testerBST.add(1);
		testerBST.add(9);
		testerBST.add(7);
		testerBST.add(12);
		testerBST.add(21);
		testerBST.add(36);
		testerBST.add(40);
		
		assertTrue(testerBST.remove(20));
		assertFalse(testerBST.contains(20));
		assertEquals(21, testerBST.getRoot());
	}
	
	@Test
	void testToArray() {
		
		bst2.add(1);
		bst2.add(1);
		bst2.add(2);
		bst2.add(4);
		bst2.add(15);
		bst2.add(5);
		bst2.add(8);
		bst2.add(78);
		bst2.remove(4);
		bst2.add(11);
		bst2.add(13);
		bst2.remove(11);

		ArrayList<Integer> nums = new ArrayList<Integer>();
		nums.add(1);
		nums.add(2);
		nums.add(5);
		nums.add(8);
		nums.add(13);	
		nums.add(15);		
		nums.add(78);		

		
		assertEquals(nums, bst2.toArrayList());
	}
	
	@Test 
	void testSpellCheck1() {
		
		spc.addToDictionary("Hello");
		spc.addToDictionary("there");
		spc.addToDictionary("world");
		spc.addToDictionary("Nice");
		spc.addToDictionary("to");
		spc.addToDictionary("meet");
		spc.addToDictionary("you");
		spc.removeFromDictionary("meet");
		spc.removeFromDictionary("there");


		List<String> misspelledWords = new ArrayList<String>();
		misspelledWords.add("helo");
		misspelledWords.add("there");
		misspelledWords.add("worl");
		misspelledWords.add("meet");


		assertEquals(misspelledWords, spc.spellCheck(new File("src/assign08/hello_world.txt")));


	}
	
	@Test 
	void testSpellCheck2() {
		
		List<String> words = new ArrayList<String>();

		words.add("Hello");
		words.add("there");
		words.add("world");
		words.add("Nice");
		words.add("to");
		words.add("meet");
		words.add("you");

		SpellChecker spc2 = new SpellChecker(words);
		
		List<String> misspelledWords = new ArrayList<String>();
		misspelledWords.add("helo");
		misspelledWords.add("worl");

		assertEquals(misspelledWords, spc2.spellCheck(new File("src/assign08/hello_world.txt")));


	}
	
	@Test 
	void testSize() {
		
		BinarySearchTree<Integer> nums = new BinarySearchTree<Integer>();
		
		nums.add(5);
		assertEquals(1, nums.size());
		
		nums.remove(5);
		assertEquals(0, nums.size());

	}

}
