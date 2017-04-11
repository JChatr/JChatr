package Chatr.Helper;

import Chatr.Client.Connection;
import Chatr.Converstation.Conversation;
import Chatr.Converstation.Message;
import Chatr.Converstation.User;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class DatabaseFixtures {

	public static void generateFixtures(){
		userSet1();
		userSet2();
		multiUserChat();
	}

	private static void userSet1() {
		Set<User> users1 = new HashSet<>();
		LinkedList<Message> messages = new LinkedList<>();
		User u1 = new User("@jDoe");
		User u2 = new User("@dTrump");
		String conID = "2934239402394";
		users1.add(u1);
		users1.add(u2);
		Conversation cv = Conversation.preConfigServer(conID, u1.getUserID(), users1, messages);
		Connection.createConversation(conID, cv.getMemberIDs());
		Connection.addMessage(conID, new Message(u1.getUserID(), "hi there"));
		Connection.addMessage(conID, new Message(u2.getUserID(), "let's build a wall"));
	}

	private static void userSet2() {
		Set<User> users1 = new HashSet<>();
		LinkedList<Message> messages = new LinkedList<>();
		User u1 = new User("@bJohnson");
		User u2 = new User("@aMerkel");
		String conID = "6078090697890";
		users1.add(u1);
		users1.add(u2);
		Conversation cv = Conversation.preConfigServer(conID, u1.getUserID(), users1, messages);
		Connection.createConversation(conID, cv.getMemberIDs());
		Connection.addMessage(conID, new Message(u1.getUserID(), "make UK great again"));
		Connection.addMessage(conID, new Message(u2.getUserID(), "Wir schaffen das!"));
	}

	private static void multiUserChat() {
		Set<User> users = Connection.readUsers();
		User u1;
		users.add(u1 = new User("@aMerkel"));
		users.add(new User("@rUser"));
		String conID = "2934239402394";
		Conversation cv = Conversation.preConfigServer(conID, u1.getUserID(), users, new LinkedList<>());
		Connection.updateConversationUsers(conID, cv.getMemberIDs());
	}
}
