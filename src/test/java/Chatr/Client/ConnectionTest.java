package Chatr.Client;

import Chatr.Helper.CONFIG;
import Chatr.Helper.Enums.MessageType;
import Chatr.Model.Chat;
import Chatr.Model.Message;
import Chatr.Model.User;
import Chatr.Server.Database.Database;
import Chatr.Server.Server;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.UnknownHostException;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ConnectionTest {

	private Database database = Database.getInstance();

	/**
	 * guarantees that the server is started when a test is run
	 */
	@BeforeClass
	public static void before() {
		try {
			new Server(CONFIG.SERVER_PORT).start();
			Thread.sleep(500);
		} catch (UnknownHostException | InterruptedException e) {
		}
	}

	@Test
	public void testCreateChat() {
		User u1 = new User("testCreateChat1", "testCreateChat1", "", "");
		User u2 = new User("testCreateChat1", "testCreateChat1", "", "");
		Chat chat = Chat.newChat("testCreateChat", u1, u2);
		Connection.createUser(u1);
		Connection.createUser(u2);
		Connection.createChat(chat);
		sleep(100);
		assertEquals(chat, database.readChat(chat.getID(), u1.getID()));
	}

	@Test
	public void testReadAllUserChats() {
		Set<Chat> c1 = Connection.readAllUserChats("@aMerkel");
		assertEquals(database.readAllChats("@aMerkel").size(), c1.size());
		Set<Chat> c2 = Connection.readAllUserChats("@jDoe");
		assertEquals(database.readAllChats("@jDoe").size(), c2.size());
		Set<Chat> c3 = Connection.readAllUserChats("@dTrump");
		assertEquals(database.readAllChats("@dTrump").size(), c3.size());
		Set<Chat> c4 = Connection.readAllUserChats("@bJohnson");
		assertEquals(database.readAllChats("@bJohnson").size(), c4.size());
	}

	@Test
	public void testUpdateChat() {
		User u1 = new User("testUpdateChat1", "testUpdateChat1", "", "");
		User u2 = new User("testUpdateChat2", "testUpdateChat2", "", "");
		User u3 = new User("testUpdateChat3", "testUpdateChat3", "", "");
		Chat chat = Chat.newChat("testUpdateChat", u1, u2);
		Connection.createUser(u1);
		Connection.createUser(u2);
		Connection.createUser(u3);
		Connection.createChat(chat);
		chat.addMessage(new Message(u1.getID(), "skdlfkjasdöfasdjfkjasdjgkhadhkjfgaeiu", MessageType.TEXT));
		chat.addMember(u3);
		sleep(100);
		assertEquals(chat, database.readChat(chat.getID(), u1.getID()));
	}

	@Test(expected = NullPointerException.class)
	public void testDeleteChat() {
		User u1 = new User("testDeleteChat1", "testDeleteChat1", "", "");
		User u2 = new User("testDeleteChat2", "testDeleteChat2", "", "");
		Chat chat = Chat.newChat("testDeleteChat", u1, u2);
		Connection.createUser(u1);
		Connection.createUser(u2);
		Connection.createChat(chat);
		sleep(100);
		assertEquals(chat, database.readChat(chat.getID(), u2.getID()));
		Connection.deleteChat(chat.getID());
		sleep(100);
		assertNull(database.readChat(chat.getID(), u1.getID()));
		assertNull(database.readChat(chat.getID(), u2.getID()));
	}

	@Test
	public void testReadNewMessages() {
		User u1 = new User("testReadNewNessages1", "testReadNewNessages1", "", "");
		User u2 = new User("testReadNewNessages2", "testReadNewNessages2", "", "");
		Chat chat = Chat.newChat("testDeleteChat", u1, u2);
		Connection.createUser(u1);
		Connection.createUser(u2);
		Connection.createChat(chat);
		chat.addMessage("skldfkjsdf", MessageType.TEXT);
		sleep(2);
		chat.addMessage("123123", MessageType.TEXT);
		sleep(2);
		chat.addMessage("daijoasdvi", MessageType.TEXT);
		sleep(2);
		chat.addMessage("qw095q3jiadlkvdaörglköadkjh", MessageType.TEXT);
		sleep(100);
		assertEquals(4, Connection.readNewMessages(chat.getID(), 0L).size());
	}

	@Test
	public void testAddMessage() {
		User merkel = database.readUser("@aMerkel");
		User trump = database.readUser("@dTrump");
		Chat chat = Chat.newChat("testAddMessage", merkel, trump);
		Message message = new Message(merkel.getID(), "testAddMessage", MessageType.TEXT);
		Connection.createChat(chat);
		Connection.addMessage(chat.getID(), message);
		sleep(100);
		assertEquals(message, database.readMessage(chat.getID(), message.getTime()));
	}

	@Test
	public void testCreateUser() {
		User u1 = new User("testCreateUser", "testCreateUser", "", "");
		Connection.createUser(u1);
		sleep(100);
		assertEquals(u1, database.readUser(u1.getID()));
	}

	@Test
	public void testReadUserLogin() {
		User merkel = Connection.readUserLogin("@aMerkel");
		assertEquals(database.readUser("@aMerkel"), merkel);
	}

	@Test
	public void testReadUskers() {
		assertEquals(database.readUsers(), Connection.readUsers());
	}


	@Test
	public void testDeleteUser() {
		User u1 = new User("testDeleteUser", "testDeleteUser", "", "");
		Connection.createUser(u1);
		sleep(100);
		Connection.deleteUser(u1.getID());
		sleep(100);
		assertNull(database.readUser(u1.getID()));
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}
}