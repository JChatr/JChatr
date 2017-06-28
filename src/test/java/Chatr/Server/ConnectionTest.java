package Chatr.Server;

import Chatr.Client.Connection;
import Chatr.Helper.Enums.ContentType;
import Chatr.Controller.Manager;
import Chatr.Model.Chat;
import Chatr.Model.Message;
import Chatr.Model.User;
import Chatr.Server.Database.Database;
import javafx.application.Platform;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.net.UnknownHostException;
import java.util.*;

import static org.junit.Assert.*;

public class ConnectionTest {

	/**
	 * guarantees that the server is started when a test is run
	 */
	private static Server s;
	private static Database d;
	@BeforeClass
	public static void before() {
		try {
			s = new Server(3456);
			s.start();

			d = Database.getCachedDatabase();
		}
		catch (UnknownHostException e){
		}


	}

	@Test
	public void createValidConversation() {
<<<<<<< HEAD

		Set<User> users = d.readUsers();

		Chat c = Chat.preConfigServer("ValidConversationTest", "@aMerkel", users, new LinkedList<>());
		Connection.createConversation(c.getID().get(),
				c.getMemberIDs());

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Set<User> userInDB =d.findConversationUsers("ValidConversationTest");

		assertTrue(userInDB.equals(users));

	}

	@Ignore
	/*public void createIdenticalConversations() {
		User u1 = new User("createIdenticalConversations");
		Chat c = Chat.newConversation(u1, new User("createIdenticalConversations2"));
=======
		User u1 = new User("createValidConversation", "", "", ""),
				u2 = new User("createValidConversation2", "", "", "");
		Connection.createUser(u1);
		Connection.createUser(u2);
		Set<User> users = new HashSet<>();
		users.add(u1);
		users.add(u2);
		Chat c = Chat.preConfigServer("createValidConversation",
				Long.toString(System.nanoTime()),
				u1.getUserID(),
				users, new LinkedList<>());
		boolean status = Connection.createChat(c);
		assertTrue(status);
	}

	@Test
	public void createIdenticalConversations() {
		User u1 = new User("createIdenticalConversations", "", "", ""),
				u2 = new User("createIdenticalConversations2", "", "", "");
		Connection.createUser(u1);
		Connection.createUser(u2);
		Chat c = Chat.newChat("createIdenticalConversations",
				u1,
				u2);
>>>>>>> 58-chat-creation
		Set<Chat> localC = new HashSet<>();
		boolean status = Connection.createChat(c);
		status &= Connection.createChat(c);
		localC.add(c);

		assertFalse(status);
<<<<<<< HEAD
		assertEquals(localC, Connection.readAllConversations(u1.getUserID()));
	}*/
=======
		assertEquals(localC, Connection.readAllUserChats(u1.getUserID()));
	}
>>>>>>> 58-chat-creation

	@Test
	public void readAllConversationsSingle() {
		User u1 = new User("readAllConversationsSingle", "", "", "");
		Connection.createUser(u1);
		Set<Chat> localc = new LinkedHashSet<>();
		Set<Chat> chats = null;
		try {
			Chat c1 = Chat.newChat("readAllConversationsSingle", u1, new HashSet<>());
			Thread.sleep(2);
			Chat c2 = Chat.newChat("readAllConversationsSingle", u1, new HashSet<>());
			Thread.sleep(2);
			Chat c3 = Chat.newChat("readAllConversationsSingle", u1, new HashSet<>());
			chats = Connection.readAllUserChats(u1.getUserID());
			localc.add(c1);
			localc.add(c2);
			localc.add(c3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(localc, chats);
	}

	@Test
	public void readAllConversationsMultiple() {
		User u1 = new User("readAllConversationsMultiple", "", "", ""),
				u2 = new User("readAllConversationsMultiple2", "", "", "");
		Connection.createUser(u1);
		Connection.createUser(u2);
		Set<Chat> localc = new HashSet<>();
		try {
			localc.add(Chat.newChat("readAllConversationsMultiple1", u1, u2));
			Thread.sleep(2);
			localc.add(Chat.newChat("readAllConversationsMultiple2", u1, u2));
			Thread.sleep(2);
			localc.add(Chat.newChat("readAllConversationsMultiple3", u1, u2));
			Thread.sleep(2);
			localc.add(Chat.newChat("readAllConversationsMultiple4", u1, u2));
			Thread.sleep(2);
			localc.add(Chat.newChat("readAllConversationsMultiple5", u1, u2));
			Thread.sleep(2);
			localc.add(Chat.newChat("readAllConversationsMultiple6", u1, u2));
			Thread.sleep(2);
			localc.add(Chat.newChat("readAllConversationsMultiple7", u1, u2));
			Thread.sleep(2);
			localc.add(Chat.newChat("readAllConversationsMultiple8", u1, u2));
			Thread.sleep(2);
			localc.add(Chat.newChat("readAllConversationsMultiple9", u1, u2));
			Thread.sleep(2);
			localc.add(Chat.newChat("readAllConversationsMultiple10", u1, u2));
			Thread.sleep(2);
			localc.add(Chat.newChat("readAllConversationsMultiple11", u1, u2));
			Thread.sleep(2);
<<<<<<< HEAD
			localc.add(Chat.newConversation(u2, u1));
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		assertEquals(localc, d.readUserConversations(u1.getUserID()));
	}


	/*public void deleteConversationValid() {
		User u1 = new User("deleteConversationValid"),
				u2 = new User("deleteConversationValid2");
		Chat c = Chat.newConversation(u1, u2);
=======
			localc.add(Chat.newChat("readAllConversationsMultiple12", u1, u2));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		localc.forEach(Connection::createChat);
		assertEquals(localc, Connection.readAllUserChats(u1.getUserID()));
	}

	@Test
	public void deleteConversationValid() {
		User u1 = new User("deleteConversationValid", "", "", ""),
				u2 = new User("deleteConversationValid2", "", "", "");
		Chat c = Chat.newChat("testname", u1, u2);
>>>>>>> 58-chat-creation

		Connection.createChat(c);
		boolean deleted = Connection.deleteChat(c.getID());

		assertTrue(deleted);
<<<<<<< HEAD
		assertTrue(Connection.readAllConversations(u1.getUserID()).isEmpty());
	}*/


/*	public void deleteConversationInvalid() {
		boolean deleted = Connection.deleteConversation("042b9135b65cc71d9c94df01add70cbf");
=======
		assertTrue(Connection.readAllUserChats(u1.getUserID()).isEmpty());
	}

	@Test
	public void deleteConversationInvalid() {
		boolean deleted = Connection.deleteChat("042b9135b65cc71d9c94df01add70cbf");
>>>>>>> 58-chat-creation
		assertFalse(deleted);
	}*/

<<<<<<< HEAD

	/*public void updateConversationUsersValid() {
		User u1 = new User("updateConversationUsersValid"),
				u2 = new User("updateConversationUsersValid2"),
				u3 = new User("updateConversationUsersValid3"),
				u4 = new User("updateConversationUsersValid4");
		Chat c = Chat.newConversation(u1, u2);
		Connection.createConversation(c.getID().get(), c.getMemberIDs());
=======
	@Test
	public void updateConversationUsersValid() {
		User u1 = new User("updateConversationUsersValid", "", "", ""),
				u2 = new User("updateConversationUsersValid2", "", "", ""),
				u3 = new User("updateConversationUsersValid3", "", "", ""),
				u4 = new User("updateConversationUsersValid4", "", "", "");
		Connection.createUser(u1);
		Connection.createUser(u2);
		Connection.createUser(u3);
		Connection.createUser(u4);
		Chat c = Chat.newChat("updateConversationUsersValid", u1, u2);
		Connection.createChat(c);
>>>>>>> 58-chat-creation
		c.addMember(u3);
		c.addMember(u4);
		boolean updated = Connection.updateChatUsers(c.getID(), c.getMemberIDs());

		assertTrue(updated);
<<<<<<< HEAD
		assertFalse(Connection.readAllConversations(u3.getUserID()).isEmpty());
		assertFalse(Connection.readAllConversations(u4.getUserID()).isEmpty());
	}*/


	/*public void updateConversationUsersInvalid() {
		User u1 = new User("updateConversationUsersInvalid"),
				u2 = new User("updateConversationUsersInvalid2");
=======
		assertFalse(Connection.readAllUserChats(u3.getUserID()).isEmpty());
		assertFalse(Connection.readAllUserChats(u4.getUserID()).isEmpty());
	}

	@Test
	public void updateConversationUsersInvalid() {
		User u1 = new User("updateConversationUsersInvalid", "", "", ""),
				u2 = new User("updateConversationUsersInvalid2", "", "", "");
		Connection.createUser(u1);
		Connection.createUser(u2);
>>>>>>> 58-chat-creation
		Set<String> uIDs = new HashSet<>();
		uIDs.add(u1.getUserID());
		uIDs.add(u2.getUserID());
		boolean updated = Connection.updateChatUsers("this is an nonexistent ID", uIDs);
		assertFalse(updated);
	}
*/
	@Ignore
	public void readNewMessagesValid() {
<<<<<<< HEAD
		User u1 = Connection.readUserLogin("@aMerkel"),
				u2 = Connection.readUserLogin("@dTrump");
		Chat c = Chat.newConversation(u1, new User("readNewMessagesValid"));
=======
		User u1 = new User("readNewMessagesValid", "", "", ""),
				u2 = new User("readNewMessagesValid2", "", "", "");
		Connection.createUser(u1);
		Connection.createUser(u2);
		Chat c = Chat.newChat("readNewMessagesValid",
				u1,
				u2);
>>>>>>> 58-chat-creation
		List<Message> messages = new ArrayList<>();
		Message m1 = null, m2 = null, m3 = null, m4 = null, m5 = null;
		try {
			m1 = new Message(u1.getUserID(), "test1", ContentType.TEXT);
			Thread.sleep(2);
			m2 = new Message(u1.getUserID(), "test1.5", ContentType.TEXT);
			Thread.sleep(2);
			m3 = new Message(u1.getUserID(), "test2", ContentType.TEXT);
			Thread.sleep(2);
			m4 = new Message(u2.getUserID(), "test3", ContentType.TEXT);
			Thread.sleep(2);
			m5 = new Message(u2.getUserID(), "test4", ContentType.TEXT);
		} catch (InterruptedException e) {
		}
		messages.add(m3);
		messages.add(m4);
		messages.add(m5);
		Connection.createChat(c);
		Connection.addMessage(c.getID(), m2);
		Connection.addMessage(c.getID(), m3);
		Connection.addMessage(c.getID(), m4);
		Connection.addMessage(c.getID(), m5);
		List<Message> response = Connection.readNewMessages(c.getID(), m2.getTime());
		assertTrue(messages.size() == response.size());
		assertEquals(messages, response);
	}

	@Test
<<<<<<< HEAD
	public void readUser() {
		User u3 = new User("93049q34522332");
		Connection.createUserLogin(u3.getUserID(), u3);
		assertEquals(u3, Connection.readUserLogin(u3.getUserID()));
	}

=======
	public void addMessage() {
		User u1 = new User("addMessage", "", "" , ""),
				u2 = new User("addMessage2", "", "", "");
		Connection.createUser(u1);
		Connection.createUser(u2);
		Chat c = Chat.newChat("addMessage", u1, u2);
		Message m1 = new Message(u1.getUserID(), "testtesttesttesttesttesttesttesttesttest");

		Connection.createChat(c);
		boolean posted = Connection.addMessage(c.getID(), m1);
		assertTrue(posted);
	}


	@Test
	public void createUser() {
		User u3 = new User("@createUser", "", "", "");
		boolean created = Connection.createUser(u3);
		assertTrue(created);
	}

	@Test
	public void readUser() {
		User u3 = new User("93049q34522332", "", "", "");
		Connection.createUser(u3);
		assertEquals(u3, Connection.readUser(u3.getUserID()));
	}

	@Test
	public void readUsers() {
		User u3 = new User("93049q345", "", "", "");
		User u4 = new User("4563563546", "", "", "");
		User u5 = new User("sdgsdfgsdfgdfg", "", "", "");
		User u6 = new User("dgzujfhkhik", "", "", "");
		User u7 = new User("slkdkjsdkgfm", "", "", "");
		User u8 = new User("siortuwoepit", "", "", "");
		User u9 = new User("oe4u5we4u5w4o5", "", "", "");


		Connection.createUser(u3);
		Connection.createUser(u4);
		Connection.createUser(u5);
		Connection.createUser(u6);
		Connection.createUser(u7);
		Connection.createUser(u8);
		Connection.createUser(u9);

		assertTrue(Connection.readUsers().contains(u3));
		assertTrue(Connection.readUsers().contains(u4));
		assertTrue(Connection.readUsers().contains(u5));
		assertTrue(Connection.readUsers().contains(u6));
		assertTrue(Connection.readUsers().contains(u7));
		assertTrue(Connection.readUsers().contains(u8));
		assertTrue(Connection.readUsers().contains(u9));
	}
>>>>>>> 58-chat-creation


	/*@Test
	public void updateUserValid() {
<<<<<<< HEAD
		User u3 = new User("sekfjselöfs");
		Connection.createUserLogin(u3.getUserID(), u3);
=======
		User u3 = new User("sekfjselöfs", "" , "", "");
		Connection.createUser(u3);
>>>>>>> 58-chat-creation
		u3.setUserName("Donald Trump");
		boolean updated = Connection.updateUser(u3.getUserID(), u3);
		assertTrue(updated);
	}*/

	/*@Test
	public void updateUserInvalid() {
		User u3 = new User("sekfjselöfs", "", "", "");
		u3.setUserName("Donald Trump");
		boolean updated = Connection.updateUser(u3.getUserID(), u3);
		assertFalse(updated);
	}*/

	@Test
	public void deleteUserValid() {
<<<<<<< HEAD

=======
		User u3 = new User("sekfjselöfs", "", "", "");
		boolean created = Connection.createUser(u3);
		boolean deleted = Connection.deleteUser(u3.getUserID());
		assertTrue(created && deleted);
>>>>>>> 58-chat-creation
	}

	/*@Test
	public void deleteUserInvalid() {
		User u3 = new User("Vladimir Putin", "" ,"", "");
		boolean deleted = Connection.deleteUser(u3.getUserID());
		assertFalse(deleted);
	}*/

}
