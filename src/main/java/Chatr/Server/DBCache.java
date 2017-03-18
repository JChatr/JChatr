package Chatr.Server;


import Chatr.Message;

import java.util.*;
public class DBCache<T> {

	private Map<Long, T> cache;

	public DBCache() {
		this.cache = new LinkedHashMap<>();
	}

	public synchronized Map<Long, T> get() {
		return this.cache;
	}

	public synchronized List<T> getRange(Long start, Long end) {
		List<T> list = new ArrayList<>();
		for (Map.Entry<Long, T> entry : cache.entrySet()) {
			if (entry.getKey() > start && entry.getKey() < end) {
				list.add(entry.getValue());
			}
		}
		return list;
	}

	public synchronized void put(Long timestamp, T obj) {
		cache.putIfAbsent(timestamp, obj);
	}

	public synchronized boolean isEmpty() {
		return cache.isEmpty();
	}

	public synchronized void clear() {
		cache.clear();
	}
	public synchronized void print(){
		for (T t : cache.values()) {
			System.out.println(t);
		}
	}
}
