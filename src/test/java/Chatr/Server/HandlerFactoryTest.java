package Chatr.Server;

import Chatr.Model.Chat;
import Chatr.Model.Message;
import Chatr.Model.User;
import Chatr.Server.Database.Database;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static Chatr.Helper.Enums.RequestType.*;
import static Chatr.Helper.Enums.Crud.*;
import static Chatr.Helper.Enums.MessageType.*;

public class HandlerFactoryTest {

	private static Database database;

	@BeforeClass
	public static void before() {
		database = Database.getInstance();
	}

	@Test
	public void testConnectHandler() {
		Transmission transmission = new Transmission(CONNECT, READ);

		Handler handler = HandlerFactory.getInstance(transmission.getRequestType());
		Collection<Transmission> responses = handler.process(transmission);

		assertEquals(1, responses.size());
	}

	@Test
	public void testMessageHandlerCREATE() {
		User merkel = database.readUser("@aMerkel");
		Set<Chat> chats = database.readAllChats(merkel.getID());
		Chat chat = null;
		for (Chat c : chats) {
			if (c.getName().get().equals("FINANCIAL POWER")) {
				chat = c;
				break;
			}
		}
		Transmission transmission = new Transmission(MESSAGE, CREATE)
				.setLocalUserID(merkel.getID())
				.setChatID(chat.getID())
				.setMessage(new Message(merkel.getID(), "lasdflsajdf", TEXT));

		Handler handler = HandlerFactory.getInstance(transmission.getRequestType());
		Collection<Transmission> responses = handler.process(transmission);
		assertEquals(chat.getMembers().size() - 1, responses.size());
	}

	@Test
	public void testMessageHandlerREAD() {
		User merkel = database.readUser("@aMerkel");
		Set<Chat> chats = database.readAllChats(merkel.getID());
		Chat chat = null;
		for (Chat c : chats) {
			chat = c;
		}
		Transmission transmission = new Transmission(MESSAGE, READ)
				.setLocalUserID(merkel.getID())
				.setChatID(chat.getID())
				.setTimestamp(0L);

		Handler handler = HandlerFactory.getInstance(transmission.getRequestType());
		Collection<Transmission> responses = handler.process(transmission);

		assertEquals(1, responses.size());
		for (Transmission response : responses) {
			assertEquals(chat.getMessages().size(), response.getMessages().size());
		}
	}

	@Test
	public void testMessageHandlerUPDATE() {
		User merkel = database.readUser("@aMerkel");
		Set<Chat> chats = database.readAllChats(merkel.getID());
		Chat chat = null;
		for (Chat c : chats) {
			if (c.getName().get().equals("FINANCIAL POWER")) {
				chat = c;
				break;
			}
		}
		Message message = new Message(merkel.getID(), "sldjfsdf", TEXT);
		Transmission transmission = new Transmission(MESSAGE, UPDATE)
				.setLocalUserID(merkel.getID())
				.setChatID(chat.getID())
				.setMessage(message);

		Handler handler = HandlerFactory.getInstance(transmission.getRequestType());
		handler.process(transmission.clone());
		handler.responses.clear();
		Collection<Transmission> responses = handler.process(transmission.clone());

		assertEquals(chat.getMembers().size() - 1, responses.size());
		for (Transmission response : responses) {
			assertTrue(response.getStatus());
		}
	}

	@Test
	public void testMessageHandlerDELETE() {
		User merkel = database.readUser("@aMerkel");
		Set<Chat> chats = database.readAllChats(merkel.getID());
		Chat chat = null;
		for (Chat c : chats) {
			if (c.getName().get().equals("FINANCIAL POWER")) {
				chat = c;
				break;
			}
		}
		Message message = new Message(merkel.getID(), "sldjfsdf", TEXT);
		Transmission t1 = new Transmission(MESSAGE, CREATE)
				.setLocalUserID(merkel.getID())
				.setChatID(chat.getID())
				.setMessage(message);
		Transmission t2 = new Transmission(MESSAGE, DELETE)
				.setLocalUserID(merkel.getID())
				.setChatID(chat.getID())
				.setMessage(message);


		Handler handler = HandlerFactory.getInstance(t2.getRequestType());
		handler.process(t1);
		handler.responses.clear();
		Collection<Transmission> responses = handler.process(t2);
		assertEquals(chat.getMembers().size() - 1, responses.size());
		for (Transmission response : responses) {
			assertTrue(response.getStatus());
		}
	}

	@Test
	public void testChatHandlerCREATE() {
		User merkel = database.readUser("@aMerkel");
		Chat chat = Chat.newChat("testChatHandlerCREATE", merkel, database.readUsers());

		Transmission transmission = new Transmission(CHAT, CREATE)
				.setLocalUserID(merkel.getID())
				.setChat(chat);

		Handler handler = HandlerFactory.getInstance(transmission.getRequestType());
		Collection<Transmission> responses = handler.process(transmission);
		assertEquals(chat.getMembers().size() - 1, responses.size());
		for (Transmission response : responses) {
			assertTrue(response.getStatus());
		}
	}

	@Test
	public void testChatHandlerREAD() {
		User merkel = database.readUser("@aMerkel");
		Set<Chat> chats = database.readAllChats(merkel.getID());
		Transmission transmission = new Transmission(CHAT, READ)
				.setLocalUserID(merkel.getID())
				.setUserID(merkel.getID());

		Handler handler = HandlerFactory.getInstance(transmission.getRequestType());
		Collection<Transmission> responses = handler.process(transmission);
		assertEquals(1, responses.size());
	}

	@Test
	public void testChatHandlerUPDATE() {
		User merkel = database.readUser("@aMerkel");
		Set<Chat> chats = database.readAllChats(merkel.getID());
		Chat chat = null;
		for (Chat c : chats) {
			if (c.getName().get().equals("FINANCIAL POWER")) {
				chat = c;
				break;
			}
		}
		chat = Chat.preConfigServer("this is a test",
				chat.getID(),
				chat.getMembers().get(0).getID(),
				new HashSet<>(chat.getMembers()),
				chat.getMessages());
		Transmission transmission = new Transmission(CHAT, UPDATE)
				.setLocalUserID(merkel.getID())
				.setChat(chat);

		Handler handler = HandlerFactory.getInstance(transmission.getRequestType());
		Collection<Transmission> responses = handler.process(transmission);

		assertEquals(chat.getMembers().size() - 1, responses.size());
		for (Transmission response : responses) {
			assertTrue(response.getStatus());
		}
	}

	@Test
	public void testChatHandlerDELETE() {
		User merkel = database.readUser("@aMerkel");
		Chat chat = Chat.newChat("testChatHandlerDELETE", merkel, database.readUsers());

		Transmission transmission1 = new Transmission(CHAT, CREATE)
				.setLocalUserID(merkel.getID())
				.setChat(chat);
		Transmission transmission2 = new Transmission(CHAT, DELETE)
				.setLocalUserID(merkel.getID())
				.setChatID(chat.getID());

		Handler handler = HandlerFactory.getInstance(transmission1.getRequestType());
		handler.process(transmission1);
		handler.responses.clear();
		Collection<Transmission> responses = handler.process(transmission2);
		assertEquals(chat.getMembers().size() - 1, responses.size());
		for (Transmission response : responses) {
			assertTrue(response.getStatus());
		}
	}
}