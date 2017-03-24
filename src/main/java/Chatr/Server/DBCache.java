package Chatr.Server;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// this cache represents an L1 implementation of the Database (Key, Value store)
// K -> Timestamp of the entry
// V -> Value @ the specified timestamp
public class DBCache<V> {

	private Map<Long, V> cache;

	public DBCache() {
		this.cache = new LinkedHashMap<>();
	}

	public synchronized Map<Long, V> get() {
		return this.cache;
	}

	// GET all entries of the DB within the specified range exclusive
	public synchronized List<V> getNewer(Long start) {
		List<V> list = new ArrayList<>();
		for (Map.Entry<Long, V> entry : cache.entrySet()) {
			if (entry.getKey() > start) {
				list.add(entry.getValue());
			}
		}
		return list;
	}

	public synchronized void put(Long timestamp, V obj) {
		cache.putIfAbsent(timestamp, obj);
	}

	public synchronized boolean isEmpty() {
		return cache.isEmpty();
	}

	public synchronized void clear() {
		cache.clear();
	}

	public synchronized void print() {
		System.out.println("\nEntries in the Database: " + cache.size());
		for (V v : cache.values()) {
			System.out.println(v);
		}
	}
}
