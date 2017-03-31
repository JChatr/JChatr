package Chatr.Database;

import Chatr.Converstation.Message;
import Chatr.Converstation.PrivateConversation;
import Chatr.Converstation.User;
import org.omg.PortableServer.POAPackage.NoServantHelper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Database abstraction layer implementing the singleton pattern
 */
public class Database {
	// USERID | USER_OBJ
	private Map<String, User> users;
	// USERID | CONV_LIST
	private Map<String, List<String>> link;
	// CONV_ID | MESSAGE_TS | MESSAGE
	private Map<String, Map<Long, Message>> conversations;

	private static Database instance;
	/**
	 * enforces the singleton pattern
	 *
	 * @return a globally unique instance of the Database object
	 */
	public static Database getCachedDatabase() {
		return (instance == null) ? instance = new Database() : instance;
	}

	/**
	 * provides concurrency support
	 */
	private Database() {
		this.users = new ConcurrentHashMap<>();
		this.link = new ConcurrentHashMap<>();
		this.conversations = new ConcurrentHashMap<>();
	}


	/**
	 * @param user
	 * @return
	 */
	public boolean addUser(User user) {
		return users.putIfAbsent(user.getUserID(), user) != null;
	}

	/**
	 * @param userID
	 * @return
	 * @throws NoSuchElementException
	 */
	public User readUser(String userID) throws NoSuchElementException {
		User u;
		if ((u = users.get(userID)) == null) throw new NoSuchElementException();
		else return u;
	}

	/**
	 * @param user
	 * @return
	 */
	public boolean updateUser(User user) {
		return users.put(user.getUserID(), user) != null;
	}

	/**
	 * @param userID
	 * @return
	 */
	public boolean deleteUser(String userID) {
		boolean remove = users.remove(userID) != null;
		remove &= link.remove(userID) != null;
		return remove;
	}

	/**
	 * @param userID
	 * @return
	 */
	public List<PrivateConversation> readUserConversations(String userID) {
		User user = users.get(userID);
		List<PrivateConversation> userConv = new ArrayList<>();
		for (String conversation : link.get(userID)) {
			userConv.add(readConversation(conversation));
		}
		return userConv;
	}

	/**
	 * TODO: assemble conversation
	 *
	 * @param conversationID
	 * @return
	 */
	public PrivateConversation readConversation(String conversationID) {
		conversations.get(conversationID);
		return new PrivateConversation();
	}

	/**
	 * @param conversationID
	 * @param timestamp
	 * @return
	 */
	public List<Message> readNewerMessages(String conversationID, Long timestamp) {
		Map<Long, Message> messages = conversations.get(conversationID);
		List<Message> out = Collections.synchronizedList(new ArrayList<Message>());
		messages.forEach((ts, m) -> {
			if (ts > timestamp) out.add(m);
		});
		return out;
	}

	/**
	 * @param conversationID
	 * @param messages
	 * @return
	 */
	public boolean addMessage(final String conversationID, final List<Message> messages) {
		boolean result = true;
		messages.forEach(m ->
				result &= addMessage(conversationID, m));
		return result;
	}

	/**
	 * @param conversationID
	 * @param message
	 * @return
	 */
	public boolean addMessage(String conversationID, Message message) {
		conversations.putIfAbsent(conversationID, new LinkedHashMap<>());
		return conversations.get(conversationID).put(message.getTime(), message) != null;
	}

	/**
	 * @param conversationID
	 * @param timestamp
	 * @return
	 * @throws NoSuchElementException
	 */
	public Message readMessage(String conversationID, Long timestamp) throws NoSuchElementException {
		try {
			return conversations.get(conversationID).get(timestamp);
		} catch (NullPointerException e) {
			throw new NoSuchElementException();
		}
	}

	/**
	 * @param conversationID
	 * @param timestamp
	 * @return
	 */
	public boolean deleteMessage(String conversationID, Long timestamp) {
		try {
			return conversations.get(conversationID).remove(timestamp) != null;
		} catch (NullPointerException e) {
			return false;
		}
	}

	/**
	 * @param conversationID
	 * @param message
	 * @return
	 */
	public boolean updateMessage(String conversationID, Message message) {
		try {
			return conversations.get(conversationID).put(message.getTime(), message) != null;
		} catch (NullPointerException e) {
			return false;
		}
	}

	/**
	 * @param userID
	 * @param conversationID
	 */
	private void linkUser(String userID, Stirng conversationID) {

	}
}