package Chatr.Server;


import java.util.*;

// this messages represents an L1 implementation of the Database (Key, Value store)
// K -> Timestamp of the entry
// V -> Value @ the specified timestamp

/**
 * @param <V>
 */
public class L1Cache<V> {

	private Map<String, Map<Long, V>> conversations;

	public L1Cache() {
		this.conversations = new HashMap<>();
	}

//	public synchronized Map<Long, V> get() {
//		return this.messages;
//	}

	/**
	 * GET all entries of the DB within the specified range exclusive
	 * TODO: create custom exception
	 *
	 * @param start
	 * @return
	 * @throws NoSuchElementException
	 */
	public synchronized List<V> getNewer(String ID, Long start) throws NoSuchElementException {
		List<V> list = new ArrayList<>();
		Map<Long, V> messages;
		if ((messages = conversations.get(ID)) == null) {
			throw new NoSuchElementException();
		}
		for (Map.Entry<Long, V> entry : messages.entrySet()) {
			if (entry.getKey() > start) {
				list.add(entry.getValue());
			}
		}
		return list;
	}

	/**
	 * Gets the specified element from the Cache
	 *
	 * @param ID        ID to get the Item at
	 * @param timestamp timestamp to get the Item at
	 * @return the element if found at the specified ID and Timestamp
	 * @throws NoSuchElementException
	 */
	public synchronized V get(String ID, Long timestamp) throws NoSuchElementException {
		try {
			V obj = conversations.get(ID).get(timestamp);
			if (obj == null) throw new NoSuchElementException();
			else return obj;
		} catch (NullPointerException e) {
			throw new NoSuchElementException();
		}
	}

	/**
	 * Inserts the Object at the specified ID and timestamp if not already present.
	 *
	 * @param ID        ID to insert at
	 * @param timestamp timestamp to insert at
	 * @param obj       Object to insert at the ID and timestamp
	 * @return weather or not the insertion was successful
	 */
	public synchronized boolean put(String ID, Long timestamp, V obj) {
		conversations.putIfAbsent(ID, new LinkedHashMap<>());
		Map<Long, V> messages = conversations.get(ID);
		boolean inserted = messages.putIfAbsent(timestamp, obj) == null;
		return inserted;
	}

	/**
	 * @return
	 */
	public synchronized boolean isEmpty() {
		return conversations.isEmpty();
	}

	/**
	 *
	 */
	public synchronized void clear() {
		conversations.clear();
	}

	/**
	 * prints the content of the entire Cache to the console
	 */
	public synchronized void print() {
		int count = 0;
		for (Map.Entry<String, Map<Long, V>> messages : conversations.entrySet()) {
			System.out.printf("Conversation: %s\n", messages.getKey());
			for (V v : messages.getValue().values()) {
				System.out.println(" |-- " + v);
				count++;
			}
		}
		System.out.printf("Entries in the Database: %d\n", count);
	}
}
