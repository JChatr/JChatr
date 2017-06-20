package Chatr.Server.Database;

import Chatr.Model.Chat;
import Chatr.Model.Message;
import Chatr.Model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Database abstraction layer implementing the singleton pattern
 */
public class Database {
	// USERID | USER_OBJ
	// ConcurrentHashMap
	private Map<String, User> users;
	// USERID | CHAT_LIST
	// ConcurrentHashMap -> HashSet
	private Map<String, Set<String>> links;
	// CHAT_ID | MESSAGE_TS | MESSAGE
	// ConcurrentHashMap -> LinkedHashMap
	private Map<String, Map<Long, Message>> chats;
	// CHAT_ID | CHAT_NAME | TBD
	private Map<String, List<String>> chatMetadata;
	// ConcurrentHashMap -> LinkedList
	private Logger log = LogManager.getLogger(Database.class);

	private static Database instance;

	/**
	 * enforces the singleton pattern
	 *
	 * @return a globally unique instance of the Database object
	 */
	public static Database getCachedDatabase() {
		if (instance == null) {
			instance = new Database();
			DatabaseFixtures.generate(instance);
		}
		return instance;
	}

	/**
	 * provides concurrency support
	 */
	private Database() {
		this.users = new ConcurrentHashMap<>();
		this.links = new ConcurrentHashMap<>();
		this.chats = new ConcurrentHashMap<>();
		this.chatMetadata = new ConcurrentHashMap<>();
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
	public User readUser(String userID) {
		return this.users.get(userID);
	}

	/**
	 * read the users from the users table
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
	 * @param user user data to forceUpdate
	 * @return if the forceUpdate was successful
	 */
	public boolean updateUser(User user) {
		if (users.get(user.getUserID()) == null) {
			return false;
		} else {
			users.put(user.getUserID(), user);
			return true;
		}
	}

	/**
	 * deletes the user from the table
	 *
	 * @param userID userID to delete at
	 * @return if the deletion was successful
	 */
	public boolean deleteUser(String userID) {
		boolean remove = users.remove(userID) != null;
		links.remove(userID);
		return remove;
	}

	/**
	 * adds a conversation to the database and links it to it's members
	 *
	 * @param chat chat data to add
	 * @return if the insertion was successful
	 */
	public boolean addChat(Chat chat) {
		String chatID = chat.getID().get();
		String chatName = chat.getName().get();
		Set<String> memberIDs = chat.getMemberIDs();
		boolean success;
		linkConversation(chatID, memberIDs);
		success = chats.putIfAbsent(chatID, new LinkedHashMap<>()) == null;
		success &= chatMetadata.putIfAbsent(chatID, new LinkedList<>()) == null;
		chatMetadata.get(chatID).set(0, chatName);
		return success;
	}

	/**
	 * update the users for a given chat
	 *
	 * @param chatID  ID to update for
	 * @param userIDs new chat Users
	 * @return if the update was successful
	 */
	// TODO: Validate
	public boolean updateChatUsers(String chatID, Set<String> userIDs) {
		if (chats.get(chatID) == null ||
				findConversationUsers(chatID).equals(userIDs)) return false;
		unlinkChat(chatID);
		return linkConversation(chatID, userIDs);
	}

	/**
	 * reads and assembles a conversation from the database
	 *
	 * @param chatID conversationID to read at
	 * @param userID local user on the target client
	 * @return the assembled conversation
	 */
	public Chat readChat(String chatID, String userID) {
		Set<User> members = findConversationUsers(chatID);
		List<String> metadata = chatMetadata.get(chatID);
		String chatName = metadata.get(0);
		return Chat.preConfigServer(chatName, chatID, userID,
				members, readNewerMessages(chatID, 0L));
	}

	/**
	 * reads all chats for that user
	 *
	 * @param userID ID to read & assemble for
	 * @return List of chats for that user
	 */
	public Set<Chat> readAllChats(String userID) {
		Set<Chat> userChats = new HashSet<>();
		for (String chatID : links.getOrDefault(userID, new HashSet<>())) {
			Chat c = readChat(chatID, userID);
			userChats.add(c);
		}
		return userChats;
	}

	/**
	 * deltes a conversation from the DB
	 *
	 * @param chatID ID to delete at
	 * @return if the deletion was successful
	 */
	public boolean deleteChat(String chatID) {
		unlinkChat(chatID);
		return chats.remove(chatID) != null && chatMetadata.remove(chatID) != null;
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
			return chats.get(conversationID).put(message.getTime(), message) == null;
		} catch (NullPointerException e) {
			log.info(String.format("unable to add Message %s to conversation %s", message, conversationID));
			log.info(e);
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
			return chats.get(conversationID).get(timestamp);
		} catch (NullPointerException e) {
			log.info(String.format("unable to find Message with %s in conversation %s", timestamp, conversationID), e);
			throw new NoSuchElementException();
		}
	}

	/**
	 * updates the data of a message in the table
	 *
	 * @param conversationID ID of the conversation to forceUpdate
	 * @param message        message to forceUpdate the data for
	 * @return if the forceUpdate was successful
	 */
	public boolean updateMessage(String conversationID, Message message) {
		try {
			return chats.get(conversationID).put(message.getTime(), message) != null;
		} catch (NullPointerException e) {
			log.info(String.format("unable to forceUpdate Message with %s in conversation %s", message, conversationID), e);
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
			return chats.get(conversationID).remove(timestamp) != null;
		} catch (NullPointerException e) {
			log.info(String.format("unable to delete Message %s in conversation %s", timestamp, conversationID), e);
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
		boolean status = false;
		for (String userID : userIDs) {
			status |= links.putIfAbsent(userID, new HashSet<>()) == null;
			status |= links.get(userID).add(conversationID);
		}
		return status;
	}


	/**
	 * breaks all links for that ID
	 *
	 * @param conversationID ID to break the links for
	 */
	private void unlinkChat(final String conversationID) {
		links.values().forEach(c -> {
			c.remove(conversationID);
			if (c.isEmpty()) links.values().remove(c);
		});
	}

	/**
	 * finds all Users for the given conversationID
	 *
	 * @param conversationID ID to search for
	 * @return found users matching that conversationID
	 */
	private Set<User> findConversationUsers(String conversationID) {
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
	 * reads all newer messages than the provided timestamp
	 *
	 * @param conversationID ID to read from
	 * @param timestamp      timestamp to compare the read messages to
	 * @return all newer messages from that conversation
	 */
	public List<Message> readNewerMessages(String conversationID, Long timestamp) {
		Map<Long, Message> messages = chats.get(conversationID);
		List<Message> out = new LinkedList<>();
		messages.forEach((ts, m) -> {
			if (ts > timestamp) out.add(m);
		});
		return out;
	}

	public void print() {
		log.debug("USERS:");
		users.forEach((id, u) -> log.debug("  - " + u));

		log.debug("LINKS:");
		links.forEach((id, l) -> log.debug("  - " + id + ":" + l));

		log.debug("CHAT MESSAGES:");
		for (Map.Entry<String, Map<Long, Message>> c : chats.entrySet()) {
			log.debug("  - " + c.getKey());
			for (Message m : c.getValue().values()) {
				log.debug("    - " + m);
			}
		}
		log.debug("CHAT METADATA:");
		for (Map.Entry<String, List<String>> e : chatMetadata.entrySet()) {
			log.debug("  - " + e.getKey());
			for (String s : e.getValue()) {
				log.debug("    - " + e);
			}
		}
	}

	enum ChatMetadata {
		NAME(0)
	}
}