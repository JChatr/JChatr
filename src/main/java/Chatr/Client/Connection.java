package Chatr.Client;

import Chatr.Helper.Enums.Crud;
import Chatr.Helper.Enums.Request;
import Chatr.Helper.JSONTransformer;
import Chatr.Model.Chat;
import Chatr.Model.Message;
import Chatr.Model.User;
import Chatr.Server.Transmission;
import org.java_websocket.client.WebSocketClient;

import java.util.List;
import java.util.Set;

import static Chatr.Helper.Enums.Crud.*;
import static Chatr.Helper.Enums.Request.*;

public final class Connection {

	private static final Client client = new Client();

	/**
	 * creates a new conversation on the server and adds the specified users
	 *
	 * @param chat Chat to create on the server
	 * @return if the operation was successful
	 */
	public static void createChat(Chat chat) {
		Transmission request = build(CONVERSATION, CREATE, chat.getID(), chat);
		sendJSON(request);
	}

	/**
	 * Read all conversations for that user
	 * (Waits for server response)
	 *
	 * @param userID User ID to get the conversations for
	 * @return the users conversations
	 */
	public static Set<Chat> readAllUserChats(String userID) {
		Transmission request = build(CONNECT, READ, userID, null);
		TransmissionListener conversationListener = new TransmissionListener(Thread.currentThread());
		client.addListener(conversationListener);
		sendJSON(request);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			client.removeListener(conversationListener);
			return conversationListener.getResponse().getChats();
		}
		return null;
	}

	/**
	 * Deletes the conversation from the server
	 *
	 * @param conversationID ID of the conversation to delete
	 * @return if the operation was successful
	 */
	public static void deleteChat(String conversationID) {
		Transmission request = build(CONVERSATION, DELETE, conversationID, null);
	}

	public static void updateChatUsers(String conversationID, Set<String> userIDs) {
		Transmission request = build(CONVERSATION, UPDATE, conversationID, userIDs);
		sendJSON(request);
	}

	/**
	 * read new messages from the specified conversation
	 * (Waits for server response)
	 *
	 * @param conversationID ID of the conversation to forceUpdate
	 * @param newest         newest message in the local copy of the conversation
	 * @return new Messages from the server
	 */
	public static List<Message> readNewMessages(String conversationID, Long newest) {
		Transmission request = build(MESSAGE, READ, conversationID, newest);
		TransmissionListener messageListener = new TransmissionListener(Thread.currentThread());
		client.addListener(messageListener);
		sendJSON(request);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			client.removeListener(messageListener);
			return messageListener.getResponse().getMessages();
		}
		return null;
	}

	/**
	 * Add a new message to a conversation
	 *
	 * @param conversationID ID of the conversation to get the message to
	 * @param message        Message to get
	 * @return if the operation was successful
	 */
	public static void addMessage(String conversationID, Message message) {
		Transmission request = build(MESSAGE, CREATE, conversationID, message);
		sendJSON(request);
	}

	/**
	 * creates a new user on the server
	 *
	 * @param user the new Users data
	 * @return if the operation was successful
	 */
	public static void createUser(User user) {
		Transmission request = build(LOGIN, CREATE, user.getUserID(), user);
		sendJSON(request);
	}

	/**
	 * read the User with the specified ID from the server
	 * returns an empty user if there is no user with that ID
	 * (Waits for server response)
	 *
	 * @param userID ID of the User to fetch
	 * @return User Object from the server
	 */
	public static User readUserLogin(String userID) {
		Transmission request = build(LOGIN, READ, userID, null);
		TransmissionListener userListener = new TransmissionListener(Thread.currentThread());
		client.addListener(userListener);
		String json = JSONTransformer.toJSON(request);
		client.socketClient.send(json);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			client.removeListener(userListener);
			return userListener.getResponse().getUser();
		}
		return null;
	}

	/**
	 * gets all users known to the user
	 * (Waits for server response)
	 *
	 * @return all users known to the server
	 */
	public static Set<User> readUsers() {
		Transmission request = build(USERS, READ, null, null);
		TransmissionListener userListener = new TransmissionListener(Thread.currentThread());
		client.addListener(userListener);
		String json = JSONTransformer.toJSON(request);
		client.socketClient.send(json);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			client.removeListener(userListener);
			return userListener.getResponse().getUsers();
		}
		return null;
	}

	/**
	 * forceUpdate the specified users data
	 *
	 * @param userID   ID of the user to forceUpdate information for
	 * @param userData new data of the user
	 * @return if the operation was successful
	 */
	public static void updateUser(String userID, User userData) {
		Transmission request = build(USER, UPDATE, userID, userData);
		sendJSON(request);
	}

	/**
	 * deletes the specified user from the Server
	 *
	 * @param userID ID of the user
	 * @return if the operation was successful
	 */
	public static void deleteUser(String userID) {
		Transmission request = build(USER, DELETE, userID, null);
		sendJSON(request);
	}

	@SuppressWarnings("unchecked")
	private static Transmission build(Request type, Crud operation, String ID, Object data) {
		Transmission request = new Transmission(type, operation);
		switch (type) {
			case MESSAGE:
				switch (operation) {
					case CREATE:
						request.setConversationID(ID).setMessage((Message) data);
						break;
					case READ:
						request.setConversationID(ID).setTimestamp((Long) data);
						break;
				}
				break;
			case CONVERSATION:
				switch (operation) {
					case CREATE:
						request.setChat((Chat) data);
						break;
					case READ:
						request.setUserID(ID);
						break;
					case UPDATE:
						request.setConversationID(ID).setUserIDs((Set<String>) data);
						break;
					case DELETE:
						request.setConversationID(ID);
						break;
				}
				break;
			case USER:
				request.setUserID(ID).setUser((User) data);
				break;
			case USERS:
				request.setUserID(ID);
				break;
			case LOGIN:
				request.setUserID(ID).setUser((User) data);
				break;
			case CONNECT:
				request.setUserID(ID);
		}
		return request;
	}

	private static void sendJSON(Transmission t) {
		String json = JSONTransformer.toJSON(t);
		client.socketClient.send(json);
	}

	static class TransmissionListener implements ConnectionListener {
		Thread thread;
		Transmission response;

		TransmissionListener(Thread currentThread) {
			this.thread = currentThread;
		}

		@Override
		public void notify(ConnectionEvent e) {
			response = e.getTransmission();
			thread.interrupt();
		}

		public Transmission getResponse() {
			return response;
		}
	}
}