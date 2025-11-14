package assign09;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This is a tester class for the HashTable class.
 * 
 * @author Jake Heairld and Anton Smolyanyy
 * @version April 04, 2024
 */  
class HashTableTest {

	HashTable<StudentGoodHash, Double> gpaTable;
	HashTable<StudentBadHash, Double> emptyTable;
	HashTable<Integer, String> stringTable;
	
	@BeforeEach
	void setup() {
		StudentGoodHash alan = new StudentGoodHash(1019999, "Alan", "Turing");
		StudentGoodHash ada = new StudentGoodHash(1004203, "Ada", "Lovelace");
		StudentGoodHash edsger = new StudentGoodHash(1010661, "Edsger", "Dijkstra");
		StudentGoodHash grace = new StudentGoodHash(1019941, "Grace", "Hopper");
		gpaTable = new HashTable<StudentGoodHash, Double>();
		gpaTable.put(alan, 3.2);  
		gpaTable.put(ada, 3.5);
		gpaTable.put(edsger, 3.8);
		gpaTable.put(grace, 4.0);
		emptyTable = new HashTable<StudentBadHash, Double>();
		stringTable = new HashTable<Integer, String>();
		stringTable.put(1, "a");
		stringTable.put(2, "b");
		stringTable.put(3, "c");
		stringTable.put(4, "d");
	}
	
	@Test
	void testSize() {
		assertEquals(4, gpaTable.size());
	}
	
	@Test
	void testSizeEmpty() {
		assertEquals(0, emptyTable.size());
	}
		
	@Test
	void testIsEmptyTrue() {
		assertTrue(emptyTable.isEmpty());
	}
	
	@Test
	void testIsEmptyFalse() {
		StudentBadHash joe = new StudentBadHash(1024513, "Joe", "Johnson");
		emptyTable.put(joe, 20.5);
		assertFalse(emptyTable.isEmpty());
	}
	
	@Test
	void testContainsKeyTrue() {
		StudentGoodHash alan = new StudentGoodHash(1019999, "Alan", "Turing");
		assertTrue(gpaTable.containsKey(alan));
	}
	
	@Test
	void testContainsKeyFalse() {
		StudentGoodHash paul = new StudentGoodHash(1111111, "Fake", "Person");
		assertFalse(gpaTable.containsKey(paul));
	}
	
	@Test
	void testContainsKeyTrueString() {
		assertTrue(stringTable.containsKey(1));
	}
	
	@Test
	void testContainsKeyFalseString() {
		assertFalse(stringTable.containsKey(99));
	}
	
	@Test
	void testContainsValueTrue() {
		assertTrue(gpaTable.containsValue(3.2));
	}
	
	@Test
	void testContainsValueFalse() {
		assertFalse(gpaTable.containsValue(1.1));
	}
	
	@Test
	void testContainsValueTrueString() {
		assertTrue(stringTable.containsValue("b"));
	}
	
	@Test
	void testContainsValueFalseString() {
		assertFalse(stringTable.containsValue("f"));
	}
	
	@Test
	void testClear() {
		gpaTable.clear();
		assertEquals(0, gpaTable.size());
	}

	@Test
	void testClearEmpty() {
		emptyTable.clear();
		assertEquals(0, emptyTable.size());
	}
	
	@Test
	void testGet() {
		StudentGoodHash ada = new StudentGoodHash(1004203, "Ada", "Lovelace");
		assertEquals(3.5, gpaTable.get(ada));
	}
	
	@Test
	void testGetString() {
		assertEquals("c", stringTable.get(3));
	}
	
	@Test
	void testGetFalse() {
		StudentGoodHash paul = new StudentGoodHash(1111111, "Fake", "Person");
		assertEquals(null, gpaTable.get(paul));
	}
	
	@Test
	void testGetFalseSTring() {
		assertEquals(null, stringTable.get(6));
	}
	
	@Test
	void testEntries() {
		MapEntry<Integer, String> entry1 = new MapEntry<Integer, String>(1, "a");
		MapEntry<Integer, String> entry2 = new MapEntry<Integer, String>(2, "b");
		MapEntry<Integer, String> entry3 = new MapEntry<Integer, String>(3, "c");
		MapEntry<Integer, String> entry4 = new MapEntry<Integer, String>(4, "d");
		ArrayList<MapEntry<Integer, String>> list = new ArrayList<MapEntry<Integer, String>>();
		list.add(entry1);
		list.add(entry2);
		list.add(entry3);
		list.add(entry4);
		assertEquals(list, stringTable.entries());
	}
	
	@Test
	void testEntriesEmpty() {
		ArrayList<MapEntry<Integer, String>> list = new ArrayList<MapEntry<Integer, String>>();
		stringTable.clear();
		assertEquals(list, emptyTable.entries());
	}
	
	@Test
	void testPutDifferentKeyPrev() {
		StudentGoodHash adam = new StudentGoodHash(1059798, "Adan", "New");
		assertEquals(null, gpaTable.put(adam, 0.9));
	}
	
	@Test
	void testPutDifferentKey() {
		StudentGoodHash adam = new StudentGoodHash(1059798, "Adan", "New");
		gpaTable.put(adam, 0.9);
		assertTrue(gpaTable.containsKey(adam));
		assertTrue(gpaTable.containsValue(0.9));
	}
	
	@Test
	void testPutSameKeyPrev() {
		StudentGoodHash edsger = new StudentGoodHash(1010661, "Edsger", "Dijkstra");
		assertEquals(3.8, gpaTable.put(edsger, 0.9));
	}
	
	@Test
	void testPutSameKey() {
		StudentGoodHash edsger = new StudentGoodHash(1010661, "Edsger", "Dijkstra");
		gpaTable.put(edsger, 0.1);
		assertFalse(gpaTable.containsValue(3.8));
		assertTrue(gpaTable.containsValue(0.1));	
	}
	
	@Test
	void testRemoveValue() {
		StudentGoodHash grace = new StudentGoodHash(1019941, "Grace", "Hopper");
		gpaTable.remove(grace);
		assertFalse(gpaTable.containsValue(4.0));
	}
	
	@Test
	void testRemoveNull() {
		StudentGoodHash paul = new StudentGoodHash(1111111, "Fake", "Person");
		assertEquals(null, gpaTable.remove(paul));
	}
	
	@Test
	void testRemove() {
		StudentGoodHash grace = new StudentGoodHash(1019941, "Grace", "Hopper");
		gpaTable.remove(grace);
		assertFalse(gpaTable.containsKey(grace));
		assertFalse(gpaTable.containsValue(4.0));
	}
	
	@Test
	void testRemoveString() {
		stringTable.remove(1);
		assertFalse(stringTable.containsKey(1));
		assertFalse(stringTable.containsValue("a"));
	}
	
	@Test
	void testPutManyElements() {
		ArrayList<MapEntry<Integer, String>> entries = new ArrayList<MapEntry<Integer, String>>();
		for(int i = 0; i < 304; i++) {
			entries.add(new MapEntry<Integer, String>(i, "val"));
		}
		for(MapEntry<Integer,String> entry : entries) {
			stringTable.put(entry.getKey(), entry.getValue());
		}
		assertEquals(304, stringTable.size());
	}
	
	@Test
	void testRehash() {
		StudentBadHash g = new StudentBadHash(1019999, "G", "Y");
		StudentBadHash mississippi = new StudentBadHash(1019999, "Mississippi", "Y");
		emptyTable.put(g, 3.0);
		emptyTable.put(mississippi, 2.0);
		emptyTable.rehash();
		assertTrue(emptyTable.containsKey(g));
		assertTrue(emptyTable.containsKey(mississippi));
	}
}
