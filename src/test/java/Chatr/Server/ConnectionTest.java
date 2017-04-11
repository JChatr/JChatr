package Chatr.Server;

import Chatr.Client.Connection;
import Chatr.Converstation.Conversation;
import Chatr.Converstation.Message;
import Chatr.Converstation.User;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class ConnectionTest {


	/**
	 * guarantees that the server is started when a test is run
	 */
	@Before
	public void before() {
		new Thread(new Server()).start();
	}

	@Test
	public void createValidConversation() {
		Conversation c = Conversation.newConversation(new User("createValidConversation"),
				new User("createValidConversation2"));
		boolean status = Connection.createConversation(c.getID(),
				c.getMemberIDs());
		assertTrue(status);
	}

	@Test
	public void createIdenticalConversations() {
		User u1 = new User("createIdenticalConversations");
		Conversation c = Conversation.newConversation(u1, new User("createIdenticalConversations2"));
		Set<Conversation> localC = new HashSet<>();
		boolean status = Connection.createConversation(c.getID(), c.getMemberIDs());

		status &= Connection.createConversation(c.getID(), c.getMemberIDs());
		status &= Connection.createConversation(c.getID(), c.getMemberIDs());
		status &= Connection.createConversation(c.getID(), c.getMemberIDs());
		status &= Connection.createConversation(c.getID(), c.getMemberIDs());
		status &= Connection.createConversation(c.getID(), c.getMemberIDs());
		localC.add(c);

		assertFalse(status);
		assertEquals(localC, Connection.readAllConversations(u1.getUserID()));
	}

	@Test
	public void readAllConversationsSingle() {
		User u1 = new User("readAllConversationsSingle");
		Set<Conversation> localc = new HashSet<>();
		Set<Conversation> conversations = null;
		try {
			Conversation c1 = Conversation.newConversation(u1, new User("b"));
			Thread.sleep(2);
			Conversation c2 = Conversation.newConversation(u1, new User("b"));
			Thread.sleep(2);
			Conversation c3 = Conversation.newConversation(u1, new User("b"));
			Connection.createConversation(c1.getID(), c1.getMemberIDs());
			Connection.createConversation(c2.getID(), c2.getMemberIDs());
			Connection.createConversation(c3.getID(), c3.getMemberIDs());
			conversations = Connection.readAllConversations(u1.getUserID());
			localc.add(c1);
			localc.add(c2);
			localc.add(c3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(localc, conversations);
	}

	@Test
	public void readAllConversationsMultiple() {
		User u1 = new User("readAllConversationsMultiple"),
				u2 = new User("readAllConversationsMultiple2");
		Set<Conversation> localc = new HashSet<>();

		try {
			localc.add(Conversation.newConversation(u1, u2));
			Thread.sleep(2);
			localc.add(Conversation.newConversation(u1, u2));
			Thread.sleep(2);
			localc.add(Conversation.newConversation(u1, u2));
			Thread.sleep(2);
			localc.add(Conversation.newConversation(u1, u2));
			Thread.sleep(2);
			localc.add(Conversation.newConversation(u1, u2));
			Thread.sleep(2);
			localc.add(Conversation.newConversation(u1, u2));
			Thread.sleep(2);
			localc.add(Conversation.newConversation(u1, u2));
			Thread.sleep(2);
			localc.add(Conversation.newConversation(u2, u1));
			Thread.sleep(2);
			localc.add(Conversation.newConversation(u2, u1));
			Thread.sleep(2);
			localc.add(Conversation.newConversation(u2, u1));
			Thread.sleep(2);
			localc.add(Conversation.newConversation(u2, u1));
			Thread.sleep(2);
			localc.add(Conversation.newConversation(u2, u1));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		localc.forEach(c -> Connection.createConversation(c.getID(), c.getMemberIDs()));

		assertEquals(localc, Connection.readAllConversations(u1.getUserID()));
	}

	@Test
	public void deleteConversationValid() {
		User u1 = new User("deleteConversationValid"),
				u2 = new User("deleteConversationValid2");
		Conversation c = Conversation.newConversation(u1, u2);

		Connection.createConversation(c.getID(), c.getMemberIDs());
		boolean deleted = Connection.deleteConversation(c.getID());

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
		Conversation c = Conversation.newConversation(u1, u2);
		Connection.createConversation(c.getID(), c.getMemberIDs());
		c.addMember(u3);
		c.addMember(u4);
		boolean updated = Connection.updateConversationUsers(c.getID(), c.getMemberIDs());

		assertTrue(updated);
		assertFalse(Connection.readAllConversations(u3.getUserID()).isEmpty());
		assertFalse(Connection.readAllConversations(u4.getUserID()).isEmpty());
	}

	@Test
	public void updateConversationUsersInvalid() {
		User u1 = new User("updateConversationUsersInvalid"),
				u2 = new User("updateConversationUsersInvalid2");
		Conversation c = Conversation.newConversation(u1, u2);

		boolean updated = Connection.updateConversationUsers(c.getID(), c.getMemberIDs());
		assertFalse(updated);
	}

	@Test
	public void readNewMessagesValid() {
		User u1 = Connection.readUser("@aMerkel"),
				u2 = Connection.readUser("@dTrump");
		Conversation c = Conversation.newConversation(u1, new User("readNewMessagesValid"));
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
		Connection.createConversation(c.getID(), c.getMemberIDs());
		Connection.addMessage(c.getID(), m2);
		Connection.addMessage(c.getID(), m3);
		Connection.addMessage(c.getID(), m4);
		Connection.addMessage(c.getID(), m5);
		List<Message> response = Connection.readNewMessages(c.getID(), m2.getTime());
		assertTrue(messages.size() == response.size());
		assertEquals(messages, response);
	}

	@Test
	public void addMessage() {
		User u1 = new User("addMessage"),
				u2 = new User("addMessage2");
		Conversation c = Conversation.newConversation(u1, u2);
		Message m1 = new Message(u1.getUserID(), "testtesttesttesttesttesttesttesttesttest");

		Connection.createConversation(c.getID(), c.getMemberIDs());
		boolean posted = Connection.addMessage(c.getID(), m1);
		assertTrue(posted);
	}


	@Test
	public void createUser() {
		User u3 = new User("93049q345");
		boolean created = Connection.createUser(u3.getUserID(), u3);
		assertTrue(created);
	}

	@Test
	public void readUser() {
		User u3 = new User("93049q34522332");
		Connection.createUser(u3.getUserID(), u3);
		assertEquals(u3, Connection.readUser(u3.getUserID()));
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


		Connection.createUser(u3.getUserID(), u3);
		Connection.createUser(u4.getUserID(), u4);
		Connection.createUser(u5.getUserID(), u5);
		Connection.createUser(u6.getUserID(), u6);
		Connection.createUser(u7.getUserID(), u7);
		Connection.createUser(u8.getUserID(), u8);
		Connection.createUser(u9.getUserID(), u9);

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
		Connection.createUser(u3.getUserID(), u3);
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
		boolean created = Connection.createUser(u3.getUserID(), u3);
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
