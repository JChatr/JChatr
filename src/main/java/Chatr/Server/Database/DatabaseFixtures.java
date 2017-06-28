package Chatr.Server.Database;


import Chatr.Helper.Enums.ContentType;
import Chatr.Model.Chat;
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
		User jDoe = getDoe();
		User dTrump = getTrump();
		Chat chat = Chat.newChat("Randomness", jDoe, dTrump);
		db.addUser(jDoe);
		db.addUser(dTrump);
		db.addChat(chat);
		db.addMessage(chat.getID(), new Message(
				jDoe.getUserID(),
				"hi there",
				ContentType.TEXT));
		Thread.sleep(2);
		db.addMessage(chat.getID(), new Message(
				dTrump.getUserID(),
				"let's build a wall",
				ContentType.TEXT));
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
		User hawk = addHawk();
		db.addUser(hawk);
		Chat chat = Chat.preConfigServer(
				"Brexit",
				"6078090697890",
				johnson.getUserID(),
				users,
				messages);
		db.addChat(chat);
		db.addMessage(chat.getID(), new Message(
				johnson.getUserID(),
				"this is a random message with no content at all",
				ContentType.TEXT));
		Thread.sleep(2);
		db.addMessage(chat.getID(), new Message(
				merkel.getUserID(),
				"another random message",
				ContentType.TEXT));
		Thread.sleep(2);
		db.addMessage(chat.getID(), new Message(
				maroko.getUserID(),
				"Ich heiße Matthias!",
				ContentType.TEXT));
		db.addMessage(chat.getID(), new Message(
				hawk.getUserID(),
				"https://media1.giphy.com/media/3oKIPf3C7HqqYBVcCk/200.gif?response_id=5947ca346dd3d014d370dded",
				ContentType.GIF));
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
		User trump = db.readUser("@dTrump");
		Chat chat = Chat.newChat("FINANCIAL POWER", merkel, db.readUsers());
		db.addChat(chat);

		db.addChat(chat);
		db.addMessage(chat.getID(), new Message(
				merkel.getUserID(),
				"hey there",
				ContentType.TEXT));
		Thread.sleep(2);
		db.addMessage(chat.getID(), new Message(
				merkel.getUserID(),
				"whats up?",
				ContentType.TEXT));
		Thread.sleep(2);
		db.addMessage(chat.getID(), new Message(
				merkel.getUserID(),
				"I am posting as Angela Merkel",
				ContentType.TEXT));
		Thread.sleep(2);
		db.addMessage(chat.getID(), new Message(
				merkel.getUserID(),
				"https://media3.giphy.com/media/3o7TKtivOfkxkD5cME/200.gif?response_id=5949138b79bc94ce4087b4e2",
				ContentType.GIF));
		Thread.sleep(2);
		db.addMessage(chat.getID(), new Message(
				merkel.getUserID(),
				"Good idea! We germans know hau to build a Wall!",
				ContentType.TEXT));
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

	private static User addHawk() {
		return new User("@herrhawk",
				"Simon Haag",
				"haagsimon@outlook.de",
				"123passwort");
	}
}

