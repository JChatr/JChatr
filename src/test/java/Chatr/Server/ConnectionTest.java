package Chatr.Server;

import Chatr.Client.Connection;
import Chatr.Converstation.Conversation;
import Chatr.Converstation.Message;
import Chatr.Converstation.User;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class ConnectionTest {
	private User u1;
	private User u2;
	private Conversation conversation;

	@Before
	public void before() {
		new Thread(new Server()).start();
		u1 = Connection.readUser("");
		u2 = Connection.readUser("");
//		conversation = Connection.readAllConversations();
	}

	@Test
	public void createValidConversation() {
		Conversation c = Conversation.newConversation(new User("a"), new User("b"));
		boolean status = Connection.createConversation(c.getID(),
				c.getMemberIDs());
		assertTrue(status);
	}

	@Test
	public void createIdenticalConversations() {
		User u1 = new User("createIdenticalConversations");
		Conversation c = Conversation.newConversation(u1, new User("b"));
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
		Conversation c = Conversation.newConversation(u1, new User("b"));

		Connection.createConversation(c.getID(), c.getMemberIDs());
		Set<Conversation> conversations = Connection.readAllConversations(u1.getUserID());
		localc.add(c);

		assertEquals(localc, conversations);
	}

	@Test
	public void readAllConversationsMultiple() {
		User u1 = new User("readAllConversationsMultiple"),
				u2 = new User("readAllConversationsMultiple2");
		Set<Conversation> localc = new HashSet<>();

		localc.add(Conversation.newConversation(u1, u2));
		localc.add(Conversation.newConversation(u1, u2));
		localc.add(Conversation.newConversation(u1, u2));
		localc.add(Conversation.newConversation(u1, u2));
		localc.add(Conversation.newConversation(u1, u2));
		localc.add(Conversation.newConversation(u1, u2));
		localc.add(Conversation.newConversation(u1, u2));
		localc.add(Conversation.newConversation(u2, u1));
		localc.add(Conversation.newConversation(u2, u1));
		localc.add(Conversation.newConversation(u2, u1));
		localc.add(Conversation.newConversation(u2, u1));
		localc.add(Conversation.newConversation(u2, u1));
		localc.forEach(c -> Connection.createConversation(c.getID(), c.getMemberIDs()));

		assertEquals(localc, Connection.readAllConversations(u1.getUserID()));
	}

	@Test
	public void deleteConversationValid() {
		User u1 = new User("deleteConversationValid"),
				u2 = new User("deleteConversationValid2");
		Conversation c = Conversation.newConversation(u1, u2);

		Connection.createConversation(c.getID(), c.getMemberIDs());
		boolean status = Connection.deleteConversation(c.getID());

		assertTrue(status);
		assertEquals(new HashSet<>(), Connection.readAllConversations(u1.getUserID()));
	}

	@Test
	public void deleteConversationInvalid() {
		boolean status = Connection.deleteConversation("042b9135b65cc71d9c94df01add70cbf");
		assertFalse(status);
	}

	@Test
	public void updateConversationUsersValid() {
		User u1 = new User("updateConversationUsersValid"),
				u2 = new User("updateConversationUsersValid2"),
				u3 = new User("Charlie"),
				u4 = new User("David");
		Conversation c = Conversation.newConversation(u1, u2);
		Set<Conversation> localc = new HashSet<>();

		localc.add(c);
		Connection.createConversation(c.getID(), c.getMemberIDs());
		c.addMember(u3);
		c.addMember(u4);
		boolean status = Connection.updateConversationUsers(c.getID(), c.getMemberIDs());

		assertTrue(status);
		assertEquals(localc, Connection.readAllConversations(u1.getUserID()));
	}

	@Test
	public void updateConversationUsersInvalid() {
		User u1 = new User("updateConversationUsersValid"),
				u2 = new User("updateConversationUsersValid2"),
				u3 = new User("Charlie"),
				u4 = new User("David");
		Conversation c = Conversation.newConversation(u1, u2);

		Set<Conversation> localc = new HashSet<>();
		localc.add(conversation);

		conversation.addMember(u3);
		conversation.addMember(u4);
		boolean status = Connection.updateConversationUsers(conversation.getID(), conversation.getMemberIDs());
		assertFalse(status);
	}

	@Test
	public void readNewMessagesValid() {
		List<Message> messages = new ArrayList<>();
		Message m1 = null, m2 = null, m3 = null, m4 = null, m5 = null;
		try {
			m1 = new Message(u1.getUserID(), "test1");
			Thread.sleep(10);
			m2 = new Message(u1.getUserID(), "test1.5");
			Thread.sleep(10);
			m3 = new Message(u1.getUserID(), "test2");
			Thread.sleep(10);
			m4 = new Message(u2.getUserID(), "test3");
			Thread.sleep(10);
			m5 = new Message(u2.getUserID(), "test4");
		} catch (InterruptedException e) {
		}
		Connection.createConversation(conversation.getID(), conversation.getMemberIDs());
		Connection.addMessage(conversation.getID(), m1);
		Connection.addMessage(conversation.getID(), m2);
		Connection.addMessage(conversation.getID(), m3);
		Connection.addMessage(conversation.getID(), m4);
		Connection.addMessage(conversation.getID(), m5);
		messages.add(m3);
		messages.add(m4);
		messages.add(m5);
		List<Message> response = Connection.readNewMessages(conversation.getID(), m2.getTime());
		assertTrue(messages.size() == response.size());
		for (int i = 0; i < response.size(); i++) {
			assertEquals(messages.get(i), response.get(i));
		}
	}

	@Test
	public void addMessage() {
		Message m1 = new Message(u1.getUserID(), "testtesttesttesttesttesttesttesttesttest");
		Connection.createConversation(conversation.getID(), conversation.getMemberIDs());
		assertTrue(Connection.addMessage(conversation.getID(), m1));
	}


	@Test
	public void createUser() {
		User u3 = new User("93049q345");
		assertTrue(Connection.createUser(u3.getUserID(), u3));
	}

	@Test
	public void readUser() {
		User u3 = new User("93049q345");
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

		assertEquals(u3, Connection.readUser(u3.getUserID()));
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
		assertTrue(Connection.updateUser(u3.getUserID(), u3));
	}

	@Test
	public void updateUserInvalid() {
		User u3 = new User("sekfjselöfs");
		u3.setUserName("Donald Trump");
		assertFalse(Connection.updateUser(u3.getUserID(), u3));
	}

	@Test
	public void deleteUserValid() {
		User u3 = new User("sekfjselöfs");
		Connection.createUser(u3.getUserID(), u3);
		assertTrue(Connection.deleteUser(u3.getUserID()));
	}

	@Test
	public void deleteUserInvalid() {
		User u3 = new User("Vladimir Putin");
		assertFalse(Connection.deleteUser(u3.getUserID()));
	}

}
