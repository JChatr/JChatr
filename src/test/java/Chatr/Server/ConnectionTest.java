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
		Set<Chat> localC = new HashSet<>();
		boolean status = Connection.createConversation(c.getID().get(), c.getMemberIDs());

		status &= Connection.createConversation(c.getID().get(), c.getMemberIDs());
		status &= Connection.createConversation(c.getID().get(), c.getMemberIDs());
		status &= Connection.createConversation(c.getID().get(), c.getMemberIDs());
		status &= Connection.createConversation(c.getID().get(), c.getMemberIDs());
		status &= Connection.createConversation(c.getID().get(), c.getMemberIDs());
		localC.add(c);

		assertFalse(status);
		assertEquals(localC, Connection.readAllConversations(u1.getUserID()));
	}*/

	@Test
	public void readAllConversationsSingle() {
		User u1 = new User("readAllConversationsSingle");
		Set<Chat> localc = new HashSet<>();
		Set<Chat> chats = null;
		try {
			Chat c1 = Chat.newConversation(u1, new User("b"));
			Thread.sleep(2);
			Chat c2 = Chat.newConversation(u1, new User("b"));
			Thread.sleep(2);
			Chat c3 = Chat.newConversation(u1, new User("b"));
			Connection.createConversation(c1.getID().get(), c1.getMemberIDs());
			Connection.createConversation(c2.getID().get(), c2.getMemberIDs());
			Connection.createConversation(c3.getID().get(), c3.getMemberIDs());
			chats = Connection.readAllConversations(u1.getUserID());
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
		User u1 = new User("readAllConversationsMultiple"),
				u2 = new User("readAllConversationsMultiple2");
		Set<Chat> localc = new HashSet<>();

		try {
			localc.add(Chat.newConversation(u1, u2));
			Thread.sleep(2);
			localc.add(Chat.newConversation(u1, u2));
			Thread.sleep(2);
			localc.add(Chat.newConversation(u1, u2));
			Thread.sleep(2);
			localc.add(Chat.newConversation(u1, u2));
			Thread.sleep(2);
			localc.add(Chat.newConversation(u1, u2));
			Thread.sleep(2);
			localc.add(Chat.newConversation(u1, u2));
			Thread.sleep(2);
			localc.add(Chat.newConversation(u1, u2));
			Thread.sleep(2);
			localc.add(Chat.newConversation(u2, u1));
			Thread.sleep(2);
			localc.add(Chat.newConversation(u2, u1));
			Thread.sleep(2);
			localc.add(Chat.newConversation(u2, u1));
			Thread.sleep(2);
			localc.add(Chat.newConversation(u2, u1));
			Thread.sleep(2);
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

		Connection.createConversation(c.getID().get(), c.getMemberIDs());
		boolean deleted = Connection.deleteConversation(c.getID().get());

		assertTrue(deleted);
		assertTrue(Connection.readAllConversations(u1.getUserID()).isEmpty());
	}*/


/*	public void deleteConversationInvalid() {
		boolean deleted = Connection.deleteConversation("042b9135b65cc71d9c94df01add70cbf");
		assertFalse(deleted);
	}*/


	/*public void updateConversationUsersValid() {
		User u1 = new User("updateConversationUsersValid"),
				u2 = new User("updateConversationUsersValid2"),
				u3 = new User("updateConversationUsersValid3"),
				u4 = new User("updateConversationUsersValid4");
		Chat c = Chat.newConversation(u1, u2);
		Connection.createConversation(c.getID().get(), c.getMemberIDs());
		c.addMember(u3);
		c.addMember(u4);
		boolean updated = Connection.updateConversationUsers(c.getID().get(), c.getMemberIDs());

		assertTrue(updated);
		assertFalse(Connection.readAllConversations(u3.getUserID()).isEmpty());
		assertFalse(Connection.readAllConversations(u4.getUserID()).isEmpty());
	}*/


	/*public void updateConversationUsersInvalid() {
		User u1 = new User("updateConversationUsersInvalid"),
				u2 = new User("updateConversationUsersInvalid2");
		Set<String> uIDs = new HashSet<>();
		uIDs.add(u1.getUserID());
		uIDs.add(u2.getUserID());
		boolean updated = Connection.updateConversationUsers("this is an nonexistent ID", uIDs);
		assertFalse(updated);
	}
*/
	@Ignore
	public void readNewMessagesValid() {
		User u1 = Connection.readUserLogin("@aMerkel"),
				u2 = Connection.readUserLogin("@dTrump");
		Chat c = Chat.newConversation(u1, new User("readNewMessagesValid"));
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
		Connection.createConversation(c.getID().get(), c.getMemberIDs());
		Connection.addMessage(c.getID().get(), m2);
		Connection.addMessage(c.getID().get(), m3);
		Connection.addMessage(c.getID().get(), m4);
		Connection.addMessage(c.getID().get(), m5);
		List<Message> response = Connection.readNewMessages(c.getID().get(), m2.getTime());
		assertTrue(messages.size() == response.size());
		assertEquals(messages, response);
	}

	@Test
	public void readUser() {
		User u3 = new User("93049q34522332");
		Connection.createUserLogin(u3.getUserID(), u3);
		assertEquals(u3, Connection.readUserLogin(u3.getUserID()));
	}



	/*@Test
	public void updateUserValid() {
		User u3 = new User("sekfjselöfs");
		Connection.createUserLogin(u3.getUserID(), u3);
		u3.setUserName("Donald Trump");
		boolean updated = Connection.updateUser(u3.getUserID(), u3);
		assertTrue(updated);
	}*/

	/*@Test
	public void updateUserInvalid() {
		User u3 = new User("sekfjselöfs");
		u3.setUserName("Donald Trump");
		boolean updated = Connection.updateUser(u3.getUserID(), u3);
		assertFalse(updated);
	}*/

	@Test
	public void deleteUserValid() {

	}

	/*@Test
	public void deleteUserInvalid() {
		User u3 = new User("Vladimir Putin");
		boolean deleted = Connection.deleteUser(u3.getUserID());
		assertFalse(deleted);
	}*/

}
