package Chatr.Server.Database;

import Chatr.Model.Chat;
import Chatr.Model.Message;
import Chatr.Model.User;

import java.util.ArrayList;
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
		Set<User> users = new HashSet<>();
		LinkedList<Message> messages = new LinkedList<>();
		User jDoe = getDoe();
		User dTrump = getTrump();
		users.add(jDoe);
		users.add(dTrump);
		Chat chat = Chat.preConfigServer("Financial POWER",
				Long.toString(System.currentTimeMillis()),
				jDoe.getUserID(),
				users,
				new ArrayList<>());
		db.addUser(jDoe);
		db.addUser(dTrump);
		db.addChat(chat);
		db.addMessage(chat.getID(), new Message(jDoe.getUserID(), "hi there"));
		Thread.sleep(2);
		db.addMessage(chat.getID(), new Message(dTrump.getUserID(), "let's build a wall"));
	}

	/**
	 * inserts Boris Johnson & Angela Merkel with their own conversation into the database
	 *
	 * @param db Database to insert the fixtures into
	 * @throws InterruptedException
	 */
	private static void userSet2(Database db) throws InterruptedException {
		LinkedList<Message> messages = new LinkedList<>();
		Set<User> users = new HashSet<>();
		User johnson = getJohnson();
		User merkel = getMerkel();
		//Profile Picture Test
		User maroko = getMaroko();

		db.addUser(johnson);
		db.addUser(merkel);
		db.addUser(maroko);
		users.add(johnson);
		users.add(merkel);
		users.add(maroko);

		Chat chat = Chat.preConfigServer("Brexit",
				"6078090697890",
				johnson.getUserID(),
				users,
				messages);
		db.addChat(chat);
		db.addMessage(chat.getID(), new Message(johnson.getUserID(), "this is a random message with no content at all"));
		Thread.sleep(2);
		db.addMessage(chat.getID(), new Message(merkel.getUserID(), "another random message"));
		Thread.sleep(2);
		db.addMessage(chat.getID(), new Message(maroko.getUserID(), "Ich heiße Matthias!"));
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
		User merkel = db.readUser("@aMerkel");
		Chat chat = Chat.preConfigServer("G20",
				Long.toString(System.currentTimeMillis()),
				merkel.getUserID(),
				db.readUsers(),
				new ArrayList<>());
		db.addChat(chat);
		db.addMessage(chat.getID(), new Message(merkel.getUserID(), "hey there"));
		Thread.sleep(2);
		db.addMessage(chat.getID(), new Message(merkel.getUserID(), "whats up?"));
		Thread.sleep(2);
		db.addMessage(chat.getID(), new Message(merkel.getUserID(), "I am posting as Angela Merkel"));
	}


	private static User getMaroko() {
		return new User("Matthias",
				"@maroko96",
				"maroko96@web.de",
				"12345");
	}

	private static User getMerkel() {
		return new User("Angela Merkel",
				"@aMerkel",
				"angela@merkel.#",
				"12345");
	}

	private static User getDoe() {
		return new User("John Doe",
				"@jDoe",
				"jd-jd@doe.com",
				"12345");
	}

	private static User getJohnson() {
		return new User("Boris Jonson",
				"@bJohnson",
				"bj@brexit.co.uk",
				"12345");
	}

	private static User getTrump() {
		return new User("Donald Trump",
				"@dTrump",
				"trump@whitehouse.gov",
				"12345");
	}
}

