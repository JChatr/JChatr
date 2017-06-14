package Chatr.Server;

import Chatr.Client.Connection;
import Chatr.Model.Chat;
import Chatr.Model.Message;
import Chatr.Model.User;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class ConnectionTest {

	/**
	 * guarantees that the server is started when a test is run
	 */
	@Ignore
	public void before() {

	}

	@Test
	public void createValidConversation() {
		User u1 = new User("createValidConversation"),
				u2 = new User("createValidConversation2");
		Set<User> users = new HashSet<>();
		users.add(u1);
		users.add(u2);
		Chat c = Chat.preConfigServer("asihdfakslöf#", u1.getUserID(), users, new LinkedList<>());
		boolean status = Connection.createConversation(c.getID().get(),
				c.getMemberIDs());
		assertTrue(status);
	}

	@Test
	public void createIdenticalConversations() {
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
	}

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
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		localc.forEach(c -> Connection.createConversation(c.getID().get(), c.getMemberIDs()));

		assertEquals(localc, Connection.readAllConversations(u1.getUserID()));
	}

	@Test
	public void deleteConversationValid() {
		User u1 = new User("deleteConversationValid"),
				u2 = new User("deleteConversationValid2");
		Chat c = Chat.newConversation(u1, u2);

		Connection.createConversation(c.getID().get(), c.getMemberIDs());
		boolean deleted = Connection.deleteConversation(c.getID().get());

		assertTrue(deleted);
		assertTrue(Connection.readAllConversations(u1.getUserID()).isEmpty());
	}

	@Test
	public void deleteConversationInvalid() {
		boolean deleted = Connection.deleteConversation("042b9135b65cc71d9c94df01add70cbf");
		assertFalse(deleted);
	}

	@Test
	public void updateConversationUsersValid() {
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
	}

	@Test
	public void updateConversationUsersInvalid() {
		User u1 = new User("updateConversationUsersInvalid"),
				u2 = new User("updateConversationUsersInvalid2");
		Set<String> uIDs = new HashSet<>();
		uIDs.add(u1.getUserID());
		uIDs.add(u2.getUserID());
		boolean updated = Connection.updateConversationUsers("this is an nonexistent ID", uIDs);
		assertFalse(updated);
	}

	@Test
	public void readNewMessagesValid() {
		User u1 = Connection.readUserLogin("@aMerkel"),
				u2 = Connection.readUserLogin("@dTrump");
		Chat c = Chat.newConversation(u1, new User("readNewMessagesValid"));
		List<Message> messages = new ArrayList<>();
		Message m1 = null, m2 = null, m3 = null, m4 = null, m5 = null;
		try {
			m1 = new Message(u1.getUserID(), "test1");
			Thread.sleep(2);
			m2 = new Message(u1.getUserID(), "test1.5");
			Thread.sleep(2);
			m3 = new Message(u1.getUserID(), "test2");
			Thread.sleep(2);
			m4 = new Message(u2.getUserID(), "test3");
			Thread.sleep(2);
			m5 = new Message(u2.getUserID(), "test4");
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

	@Ignore
	public void addMessage() {
		User u1 = new User("addMessage"),
				u2 = new User("addMessage2");
		Chat c = Chat.newConversation(u1, u2);
		Message m1 = new Message(u1.getUserID(), "testtesttesttesttesttesttesttesttesttest");

		Connection.createConversation(c.getID().get(), c.getMemberIDs());
		//boolean posted = Connection.addMessage(c.getID().get(), m1);
		//assertTrue(posted);
	}


	@Test
	public void createUser() {
		User u3 = new User("@createUserLogin");
		boolean created = Connection.createUserLogin(u3.getUserID(), u3);
		assertTrue(created);
	}

	@Test
	public void readUser() {
		User u3 = new User("93049q34522332");
		Connection.createUserLogin(u3.getUserID(), u3);
		assertEquals(u3, Connection.readUserLogin(u3.getUserID()));
	}

	@Test
	public void readUsers() {
		User u3 = new User("93049q345");
		User u4 = new User("4563563546");
		User u5 = new User("sdgsdfgsdfgdfg");
		User u6 = new User("dgzujfhkhik");
		User u7 = new User("slkdkjsdkgfm");
		User u8 = new User("siortuwoepit");
		User u9 = new User("oe4u5we4u5w4o5");


		Connection.createUserLogin(u3.getUserID(), u3);
		Connection.createUserLogin(u4.getUserID(), u4);
		Connection.createUserLogin(u5.getUserID(), u5);
		Connection.createUserLogin(u6.getUserID(), u6);
		Connection.createUserLogin(u7.getUserID(), u7);
		Connection.createUserLogin(u8.getUserID(), u8);
		Connection.createUserLogin(u9.getUserID(), u9);

		assertTrue(Connection.readUsers().contains(u3));
		assertTrue(Connection.readUsers().contains(u4));
		assertTrue(Connection.readUsers().contains(u5));
		assertTrue(Connection.readUsers().contains(u6));
		assertTrue(Connection.readUsers().contains(u7));
		assertTrue(Connection.readUsers().contains(u8));
		assertTrue(Connection.readUsers().contains(u9));
	}

	@Test
	public void updateUserValid() {
		User u3 = new User("sekfjselöfs");
		Connection.createUserLogin(u3.getUserID(), u3);
		u3.setUserName("Donald Trump");
		boolean updated = Connection.updateUser(u3.getUserID(), u3);
		assertTrue(updated);
	}

	@Test
	public void updateUserInvalid() {
		User u3 = new User("sekfjselöfs");
		u3.setUserName("Donald Trump");
		boolean updated = Connection.updateUser(u3.getUserID(), u3);
		assertFalse(updated);
	}

	@Test
	public void deleteUserValid() {
		User u3 = new User("sekfjselöfs");
		boolean created = Connection.createUserLogin(u3.getUserID(), u3);
		boolean deleted = Connection.deleteUser(u3.getUserID());
		assertTrue(created && deleted);
	}

	@Test
	public void deleteUserInvalid() {
		User u3 = new User("Vladimir Putin");
		boolean deleted = Connection.deleteUser(u3.getUserID());
		assertFalse(deleted);
	}

}
