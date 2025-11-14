package assign09;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a HashTable which resolves collisions using separate chaining and maintains a load factor of less than 10.
 * 
 * @author Jake Heairld and Anton Smolyanyy
 * @version April 03, 2024
 *
 * @param <K> - key type
 * @param <V> - value type
 */
public class HashTable<K,V> implements Map<K,V> {

	final private int loadThreshold = 10;
	private int capacity;
	private int elemCount;
	private ArrayList<LinkedList<MapEntry<K,V>>> backArray;
	
	/**
	 * Constructs an empty HashTable.
	 */
	public HashTable() {
		backArray = new ArrayList<LinkedList<MapEntry<K,V>>>();
		capacity = 10;
		elemCount = 0;
		for(int i = 0; i < capacity; i++) {
			backArray.add(new LinkedList<MapEntry<K,V>>());
		}
	}
	
	/**
	 * Doubles capacity of hash table and reassigns entries.
	 */
	public void rehash() {
		List<MapEntry<K,V>> allEntries = entries();
		for(LinkedList<MapEntry<K,V>> list : backArray) {
			list.clear();
		}
		while(backArray.size() != capacity) {
			backArray.add(new LinkedList<MapEntry<K,V>>());
		}
		for(MapEntry<K,V> entry : allEntries) {
			this.put(entry.getKey(), entry.getValue());
			elemCount--;
		}
	}
	
	/**
	 * Removes all entries from this HashTable.
	 */
	public void clear() {
		elemCount = 0;
		for(LinkedList<MapEntry<K,V>> list : backArray) {
			list.clear();
		}
	}

	/**
	 * Determines if the HashTable contains the given key.
	 * 
	 * @param key - the key which is to be found
	 * @return true if the HashTable contains the given key, false otherwise
	 */
	public boolean containsKey(K key) {
		int index = Math.abs(key.hashCode()%capacity);
		for(MapEntry<K,V> entry : backArray.get(index)) {
			if(entry.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines if the HashTable contains the given value.
	 * 
	 * @param value - the value which is to be found
	 * @return true if the HashTable contains the given value, false otherwise
	 */
	public boolean containsValue(V value) {
		for(LinkedList<MapEntry<K,V>> list : backArray) {
			for(MapEntry<K,V> entry : list) {
				if(entry.getValue().equals(value)) 
					return true;
			}
		}
		return false;
	}

	/**
	 * Returns a List of the Key and Value pairs stored in the Hash Table.
	 * 
	 * @return List of key value pairs 
	 */
	public List<MapEntry<K, V>> entries() {
		List<MapEntry<K,V>> entryList = new ArrayList<MapEntry<K,V>>();
		for(LinkedList<MapEntry<K,V>> list : backArray) {
			for(MapEntry<K,V> entry : list) {
				entryList.add(entry);
			}
		}
		return entryList;
	}

	/**
	 * Returns the value mapped to the given key, if the key exists in the HashTable.
	 * 
	 * @param key - the key whose mapped value is to be returned
	 * @return value mapped to given key if key is present, null otherwise
	 */
	public V get(K key) {
		int index = Math.abs(key.hashCode()%capacity);
		for(MapEntry<K,V> entry : backArray.get(index)) {
			if(entry.getKey().equals(key)) {
				return entry.getValue();
			}
		}
		return null;
	}

	/**
	 * Determines if the HashTable is empty or not.
	 * 
	 * @return true if HashTable is empty, false otherwise.
	 */
	public boolean isEmpty() {
		return elemCount == 0;
	}

	/**
	 * Associates the specified value with the specified key in this HashTable. If
	 * the key already existed in this map, changes the value to this new value,
	 * otherwise adds the new key-value pair.
	 * 
	 * @param key   - given key
	 * @param value - value which is to be mapped to key
	 * @return the previous value mapped to the key, or null if there was no
	 *         previous mapping for key
	 */
	public V put(K key, V value) {
		 if(containsKey(key)) {
			int index = Math.abs(key.hashCode()%capacity);
			for(MapEntry<K,V> entry : backArray.get(index)) {
				if(entry.getKey().equals(key)) {
					V prevValue = entry.getValue();
					entry.setValue(value);
					return prevValue;
				}
			}
			return null;
		} else {
			int index = Math.abs(key.hashCode()%capacity);
			backArray.get(index).add(new MapEntry<K,V>(key, value));
			elemCount++;
			if((double)elemCount/capacity >= loadThreshold) {
				capacity *= 2;
				rehash();
			} 
			return null;
		}
	}

	/**
	 * Removes the value mapped to the given key from the HashTable, if key is present in the table.
	 * 
	 * @param key - the key whose mapped value is to be removed
	 * @return value which was removed if key was present, null otherwise
	 */
	public V remove(K key) {
		if(!containsKey(key)) {
			return null;
		}
		int index = Math.abs(key.hashCode()%capacity);
		V value = null;
		for(int i = 0; i < backArray.get(index).size(); i++) {
			if(backArray.get(index).get(i).getKey().equals(key)) {
				value = backArray.get(index).get(i).getValue();
				backArray.get(index).remove(i);
				break;
			}
		}
		elemCount--;
		return value;
	}

	/**
	 * Returns the number of elements in the HashTable.
	 * 
	 * @return number of elements stored in HashTable
	 */
	public int size() {
		return this.elemCount;
	}
}