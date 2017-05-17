package Chatr.Server.Database;

import Chatr.Model.Message;
import Chatr.Model.User;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

class DatabaseFixtures {

	/**
	 * generated some default data in the Database for testing purposes
	 *
	 * @param db Database to insert the fixtures into
	 */
	static void generate(Database db) {
		try {
			userSet1(db);
			userSet2(db);
			multiUserChat(db);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * inserts John Doe & Donalt Trump with their own conversation into the database
	 *
	 * @param db Database to insert the fixtures into
	 * @throws InterruptedException
	 */
	private static void userSet1(Database db) throws InterruptedException {
		Set<String> uIDs = new HashSet<>();
		LinkedList<Message> messages = new LinkedList<>();
		User u1 = new User("@jDoe");
		User u2 = new User("@dTrump");
		String conID = "2934239402394";
		uIDs.add(u1.getUserID());
		uIDs.add(u2.getUserID());
		db.addUser(u1);
		db.addUser(u2);
		db.addConversation(conID, uIDs);
		db.addMessage(conID, new Message(u1.getUserID(), "hi there"));
		Thread.sleep(2);
		db.addMessage(conID, new Message(u2.getUserID(), "let's build a wall"));
	}

	/**
	 * inserts Boris Johnson & Angela Merkel with their own conversation into the database
	 *
	 * @param db Database to insert the fixtures into
	 * @throws InterruptedException
	 */
	private static void userSet2(Database db) throws InterruptedException {
		Set<String> uIDs = new HashSet<>();
		LinkedList<Message> messages = new LinkedList<>();
		User u1 = new User("@bJohnson");
		User u2 = new User("@aMerkel");
		String conID = "6078090697890";
		uIDs.add(u1.getUserID());
		uIDs.add(u2.getUserID());
		db.addUser(u1);
		db.addUser(u2);
		db.addConversation(conID, uIDs);
		db.addMessage(conID, new Message(u1.getUserID(), "this is a random message with no content at all"));
		Thread.sleep(2);
		db.addMessage(conID, new Message(u2.getUserID(), "another random message"));
	}

	/**
	 * creates a group conversation with with :
	 * <ul>
	 * <li>John Doe</li>
	 * <li>Donald Trump</li>
	 * <li>Boris Johnson</li>
	 * <li>Angela Merkel</li>
	 * </ul>
	 * posts some messages to the conversation
	 *
	 * @param db Database to insert the fixtures into
	 * @throws InterruptedException
	 */
	private static void multiUserChat(Database db) throws InterruptedException {
		Set<String> uIDs = new HashSet<>();
		db.readUsers().forEach(u -> uIDs.add(u.getUserID()));
		User u1 = db.readUser("@aMerkel");
		String conID = "293423988894";
		db.addConversation(conID, uIDs);
		db.addMessage(conID, new Message(u1.getUserID(), "hey there"));
		Thread.sleep(2);
		db.addMessage(conID, new Message(u1.getUserID(), "whats up?"));
		Thread.sleep(2);
		db.addMessage(conID, new Message(u1.getUserID(), "I am posting as Angela Merkel"));
	}
}