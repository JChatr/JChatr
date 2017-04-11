package Chatr.Database;

import Chatr.Client.Connection;
import Chatr.Converstation.Conversation;
import Chatr.Converstation.Message;
import Chatr.Converstation.User;
import com.sun.org.apache.xalan.internal.xsltc.runtime.InternalRuntimeError;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class DatabaseFixtures {

	public static void generate(Database db) {
		try {
			userSet1(db);
			userSet2(db);
			multiUserChat(db);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		db.print();
	}

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
