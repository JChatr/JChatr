package Chatr.Server.Database;


import Chatr.Helper.Enums.MessageType;
import Chatr.Helper.HashGen;
import Chatr.Model.Chat;
import Chatr.Model.Message;
import Chatr.Model.User;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

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
		Chat chat = Chat.newChatDB("Randomness", jDoe, dTrump);
		db.addUser(jDoe);
		db.addUser(dTrump);
		db.addChat(chat);
		db.addMessage(chat.getID(), new Message(
				jDoe.getID(),
				"hi there",
				MessageType.TEXT));
		Thread.sleep(2);
		db.addMessage(chat.getID(), new Message(
				dTrump.getID(),
				"let's build a wall",
				MessageType.TEXT));
	}

	/**
	 * inserts Boris Johnson & Angela Merkel with their own conversation into the database
	 *
	 * @param db Database to insert the fixtures into
	 * @throws InterruptedException
	 */
	private static void userSet2(Database db) throws InterruptedException {
		LinkedList<Message> messages = new LinkedList<>();
		User johnson = getJohnson();
		User merkel = getMerkel();
		//Profile Picture Test
		User maroko = getMaroko();
		db.addUser(johnson);
		db.addUser(merkel);
		db.addUser(maroko);
		User hawk = addHawk();
		db.addUser(hawk);
		Chat chat = Chat.preConfigServer(
				"Brexit",
				"6078090697890",
				johnson.getID(),
				new HashSet<>(Arrays.asList(johnson, merkel, maroko)),
				messages);
		db.addChat(chat);
		db.addMessage(chat.getID(), new Message(
				johnson.getID(),
				"this is a random message with no content at all",
				MessageType.TEXT));
		Thread.sleep(2);
		db.addMessage(chat.getID(), new Message(
				merkel.getID(),
				"another random message",
				MessageType.TEXT));
		Thread.sleep(2);
		db.addMessage(chat.getID(), new Message(
				maroko.getID(),
				"Ich heiße Matthias!",
				MessageType.TEXT));
		Thread.sleep(2);
		db.addMessage(chat.getID(), new Message(
				hawk.getID(),
				"https://media1.giphy.com/media/3oKIPf3C7HqqYBVcCk/200.gif?response_id=5947ca346dd3d014d370dded",
				MessageType.GIF,
				356,
				200,
				null));
		Thread.sleep(2);
		db.addMessage(chat.getID(), new Message(
				maroko.getID(),
				"https://media0.giphy.com/media/YIgKKIj5d4CQ/200.gif?response_id=595a1ad643c9378c71945239",
				MessageType.GIF,
				355,
				200,
				null));
		Thread.sleep(2);
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
		Chat chat = Chat.newChatDB("FINANCIAL POWER", merkel, db.readUsers());
		db.addChat(chat);

		db.addChat(chat);
		db.addMessage(chat.getID(), new Message(
				trump.getID(),
				"hey there",
				MessageType.TEXT));
		Thread.sleep(2);
		db.addMessage(chat.getID(), new Message(
				merkel.getID(),
				"whats up?",
				MessageType.TEXT));
		Thread.sleep(2);
		db.addMessage(chat.getID(), new Message(
				merkel.getID(),
				"I am posting as Angela Merkel",
				MessageType.TEXT));
		Thread.sleep(2);
		db.addMessage(chat.getID(), new Message(
				trump.getID(),
				"https://media3.giphy.com/media/3o7TKtivOfkxkD5cME/200.gif?response_id=5949138b79bc94ce4087b4e2",
				MessageType.GIF,
				356,
				200,
				null));
		Thread.sleep(2);
		db.addMessage(chat.getID(), new Message(
				merkel.getID(),
				"Good idea! We germans know hau to build a Wall!",
				MessageType.TEXT));
	}


	private static User getMaroko() {
		return new User("Matthias",
				"@maroko96",
				"maroko96@web.de",
				HashGen.hashPW("12345"));
	}

	private static User getMerkel() {
		return new User("Angela Merkel",
				"@aMerkel",
				"kasanloe@web.de",
				HashGen.hashPW("12345"));
	}

	private static User getDoe() {
		return new User("John Doe",
				"@jDoe",
				"jd-jd@doe.com",
				HashGen.hashPW("12345"));
	}

	private static User getJohnson() {
		return new User("Boris Jonson",
				"@bJohnson",
				"bj@brexit.co.uk",
				HashGen.hashPW("12345"));
	}

	private static User getTrump() {
		return new User("Donald Trump",
				"@dTrump",
				"trump@whitehouse.gov",
				HashGen.hashPW("12345"));
	}

	private static User addHawk() {
		return new User("Simon Haag",
				"@herrhawk",
				"haagsimon@outlook.de",
				HashGen.hashPW("12345"));
	}
}

