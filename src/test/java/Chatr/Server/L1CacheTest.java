package Chatr.Server;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class L1CacheTest {

	@Test
	public void storeStrings() {
		L1Cache<String> cache = new L1Cache<>();
		long t1, t2, t3, t4;
		cache.put("1", t1 = System.currentTimeMillis(), "123456");
		cache.put("2", t2 = System.currentTimeMillis(), "234567");
		cache.put("3", t3 = System.currentTimeMillis(), "345678");
		cache.put("4", t4 = System.currentTimeMillis(), "456789");
		assertEquals("123456", cache.get("1", t1));
		assertEquals("234567", cache.get("2", t2));
		assertEquals("345678", cache.get("3", t3));
		assertEquals("456789", cache.get("4", t4));
	}

	@Test
	public void storeIntegers() {
		L1Cache<Integer> cache = new L1Cache<>();
		long t1, t2, t3, t4;
		cache.put("1", t1 = System.currentTimeMillis(), 123);
		cache.put("2", t2 = System.currentTimeMillis(), 234);
		cache.put("3", t3 = System.currentTimeMillis(), 345);
		cache.put("4", t4 = System.currentTimeMillis(), 456);
		assertEquals((Integer) 123, cache.get("1", t1));
		assertEquals((Integer) 234, cache.get("2", t2));
		assertEquals((Integer) 345, cache.get("3", t3));
		assertEquals((Integer) 456, cache.get("4", t4));
	}

	@Test
	public void putNoDuplicateEntriesAllowed() {
		L1Cache<Integer> cache = new L1Cache<>();
		long t1 = System.currentTimeMillis();
		cache.put("1", t1, 123);
		cache.put("1", t1, 456);
		assertEquals((Integer) 123, cache.get("1", t1));
	}

	@Test(expected = NoSuchElementException.class)
	public void getNonexistent() {
		L1Cache<String> cache = new L1Cache<>();
		assertNull(cache.get("3123", System.currentTimeMillis()));
	}

	@Test(expected = NoSuchElementException.class)
	public void getNewerNonexistent() {
		L1Cache<String> cache = new L1Cache<>();
		assertNull(cache.getNewer("sadlkjf", System.currentTimeMillis()));
	}

	@Test
	public void getNewer() {
		L1Cache<String> cache = new L1Cache<>();
		long t2;
		cache.put("1", System.currentTimeMillis(), "123456");
		cache.put("1", t2 =System.currentTimeMillis(), "234567");
		cache.put("1", System.currentTimeMillis(), "345678");
		cache.put("1", System.currentTimeMillis(), "456789");
		List<String> newer = new ArrayList<>();
		newer.add("345678");
		newer.add("456789");
		List<String> returned= cache.getNewer("1", t2);
		for (int i = 0; i < returned.size(); i++) {
			assertEquals(newer.get(i), returned.get(i));
		}
	}
}
