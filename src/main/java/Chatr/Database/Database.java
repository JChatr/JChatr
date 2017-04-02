package Chatr.Database;

import Chatr.Converstation.Conversation;
import Chatr.Converstation.Message;
import Chatr.Converstation.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Database abstraction layer implementing the singleton pattern
 */
public class Database {
	// USERID | USER_OBJ
	// ConcurrenHashMap
	private Map<String, User> users;
	// USERID | CONV_LIST
	// ConcurrentHashMap -> HashSet
	private Map<String, Set<String>> links;
	// CONV_ID | MESSAGE_TS | MESSAGE
	// ConcurrentHashMap -> LinkedHashMap
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
		this.links = new ConcurrentHashMap<>();
		this.conversations = new ConcurrentHashMap<>();
	}


	/**
	 * adds a User to the users Table
	 *
	 * @param user user object to add
	 * @return if the insertion was successful
	 */
	public boolean addUser(User user) {
		return users.putIfAbsent(user.getUserID(), user) == null;
	}

	/**
	 * read the user from the users table
	 *
	 * @return the found user object if contained in the table
	 */
	public Set<User> readUsers() {
		Set<User> users = new HashSet<>();
		this.users.forEach((uID, user) -> users.add(user));
		return users;
	}

	/**
	 * updates the data for the user in the table
	 *
	 * @param user user data to update
	 * @return if the update was successful
	 */
	public boolean updateUser(User user) {
		return users.put(user.getUserID(), user) != null;
	}

	/**
	 * deletes the user from the table
	 *
	 * @param userID userID to delete at
	 * @return if the deletion was successful
	 */
	public boolean deleteUser(String userID) {
		boolean remove = users.remove(userID) != null;
		remove &= links.remove(userID) != null;
		return remove;
	}

	/**
	 * adds a conversation to the database and links it to it's members
	 *
	 * @param conversationID conversation data to add
	 * @return if the insertion was successful
	 */
	public boolean addConversation(String conversationID, Set<String> userIDs) {
		linkConversation(conversationID, userIDs);
		return conversations.putIfAbsent(conversationID, new LinkedHashMap<>()) == null;
	}

	public boolean updateConversationUsers(String conversationID, Set<String> userIDs) {
		unLinkConversation(conversationID);
		return linkConversation(conversationID, userIDs);
	}

	/**
	 * reads and assembles a conversation from the database
	 *
	 * @param conversationID conversationID to read at
	 * @return the assembled conversation
	 */
	public Conversation readConversation(String conversationID, String userID) {
		Set<User> members = followLinksUser(conversationID);
		Conversation build = Conversation.newConversation();
		build.setID(conversationID).setLocalUserID(userID).setMembers(members);
		build.addMessages(readNewerMessages(conversationID, 0L));
		return build;
	}

	/**
	 * reads all conversations for that user
	 *
	 * @param userID ID to read & assemble forl.forEach(c -> System.out.println(c)
	 * @return List of conversations for that user
	 */
	public Set<Conversation> readUserConversations(String userID) {
		Set<Conversation> userConv = new HashSet<>();
		for (String conversationID : links.getOrDefault(userID, new HashSet<>())) {
			userConv.add(readConversation(conversationID, userID));
		}
		return userConv;
	}

	/**
	 * deltes a conversation from the datatbase
	 *
	 * @param conversationID ID to delte at
	 * @return if the deletion was successful
	 */
	public boolean deleteConversation(String conversationID) {
		unLinkConversation(conversationID);
		return conversations.remove(conversationID) != null;
	}

	/**
	 * adds a message to a conversation
	 *
	 * @param conversationID ID of the conversation to add to
	 * @param message        message to add
	 * @return if the insertion was successful
	 */
	public boolean addMessage(String conversationID, Message message) {
		try {
			return conversations.get(conversationID).put(message.getTime(), message) == null;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * reads a message from the specified conversation
	 *
	 * @param conversationID ID to read from
	 * @param timestamp      message timestamp to read from
	 * @return the read message
	 * @throws NoSuchElementException if such message is found
	 */
	public Message readMessage(String conversationID, Long timestamp) throws NoSuchElementException {
		try {
			return conversations.get(conversationID).get(timestamp);
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new NoSuchElementException();
		}
	}

	/**
	 * updates the data of a message in the table
	 *
	 * @param conversationID ID of the conversation to update
	 * @param message        message to update the data for
	 * @return if the update was successful
	 */
	public boolean updateMessage(String conversationID, Message message) {
		try {
			return conversations.get(conversationID).put(message.getTime(), message) != null;
		} catch (NullPointerException e) {
			return false;
		}
	}
	/**
	 * deletes a message from the table
	 *
	 * @param conversationID ID of the conversation to delte at
	 * @param timestamp      timestamp to delete at
	 * @return if the deletion was successful
	 */
	public boolean deleteMessage(String conversationID, Long timestamp) {
		try {
			return conversations.get(conversationID).remove(timestamp) != null;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * links a conversation to all given useIDs
	 *
	 * @param userIDs        users to links to
	 * @param conversationID ID to links
	 */
	private boolean linkConversation(String conversationID, Set<String> userIDs) {
		boolean status = true;
		for (String userID : userIDs) {
			status &= links.putIfAbsent(userID, new HashSet<>()) == null;
			status &= links.get(userID).add(conversationID);
		}
		return status;
	}

	private Set<User> followLinksUser(String conversationID) {
		Set<User> linkedUsers = new HashSet<>();
		for (Map.Entry<String, Set<String>> link : links.entrySet()) {
			for (String conversation : link.getValue()) {
				if (conversation.equals(conversationID)) {
					linkedUsers.add(users.get(link.getKey()));
				}
			}
		}
		return linkedUsers;
	}

	/**
	 * breaks all links for that ID
	 *
	 * @param conversationID ID to break the links for
	 */
	private void unLinkConversation(final String conversationID) {
		links.values().forEach(c -> c.remove(conversationID));
	}

	/**
	 * reads all newer messages than the provided timestamp
	 *
	 * @param conversationID ID to read from
	 * @param timestamp      timestamp to compare the read messages to
	 * @return all newer messages from that conversation
	 */
	public List<Message> readNewerMessages(String conversationID, Long timestamp) {
		Map<Long, Message> messages = conversations.get(conversationID);
		List<Message> out = new ArrayList<>();
		messages.forEach((ts, m) -> {
			if (ts > timestamp) out.add(m);
		});
		return out;
	}

	public void print(){
		System.out.println("USERS:");
		users.forEach((id, u) -> System.out.println("  - " + u));

		System.out.println("LINKS:");
		links.forEach((id, l) -> System.out.println("  - " + id + ":"+ l));

		System.out.println("CONVERSATIONS:");
		for (Map.Entry<String, Map<Long, Message>> c : conversations.entrySet()) {
			System.out.println("  - " + c.getKey());
			for (Message m : c.getValue().values()) {
				System.out.println("    - " + m);
			}
		}
	}
}