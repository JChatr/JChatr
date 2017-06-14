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

public final class Connection{

	private static final Client client =new Client();

	/**
	 * creates a new conversation on the server and adds the specified users
	 *
	 * @param conversationID ID of the new conversation
	 * @param userIDs        users to add to the conversation
	 * @return if the operation was successful
	 */
	public static boolean createConversation(String conversationID, Set<String> userIDs) {
		Transmission request = build(CONVERSATION, CREATE, conversationID, userIDs);
		Transmission response = new Client().get(request);
		return response.getStatus();
	}

	/**
	 * TODO: change the method of getting new conversations. Really dirty right now
	 * read all conversations for that user
	 *
	 * @param userID User ID to get the conversations for
	 * @return the users conversations
	 */
	public static Set<Chat> readAllConversations(String userID) {
		Transmission request = build(CONNECT, READ, userID, null);
		TransmissionListener conversationListener= new TransmissionListener(Thread.currentThread());
		client.addListener(conversationListener);
		String json =JSONTransformer.toJSON(request);
		client.socketClient.send(json);
		try{
			Thread.sleep(2000);
		}
		catch(InterruptedException e){
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
	public static boolean deleteConversation(String conversationID) {
		Transmission request = build(CONVERSATION, DELETE, conversationID, null);
		Transmission response = new Client().get(request);
		return response.getStatus();
	}

	public static boolean updateConversationUsers(String conversationID, Set<String> userIDs) {
		Transmission request = build(CONVERSATION, UPDATE, conversationID, userIDs);
		Transmission response = new Client().get(request);
		return response.getStatus();
	}

	/**
	 * read new messages from the specified conversation
	 *
	 * @param conversationID ID of the conversation to forceUpdate
	 * @param newest         newest message in the local copy of the conversation
	 * @return new Messages from the server
	 */
	public static List<Message> readNewMessages(String conversationID, Long newest) {
		Transmission request = build(MESSAGE, READ, conversationID, newest);
		TransmissionListener messageListener= new TransmissionListener(Thread.currentThread());
		client.addListener(messageListener);
		String json= JSONTransformer.toJSON(request);
		client.socketClient.send(json);
		try{
			Thread.sleep(2000);
		}
		catch(InterruptedException e){
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
		String json= JSONTransformer.toJSON(request);
		client.socketClient.send(json);

	}


	/**
	 * creates a new user on the server
	 *
	 * @param userID   ID of the User to create
	 * @param userData the new Users data
	 * @return if the operation was successful
	 */
	public static boolean createUserLogin(String userID, User userData) {
		Transmission request = build(LOGIN, CREATE, userID, userData);
		Transmission response = new Client().get(request);
		return response.getStatus();
	}

	/**
	 * read the User with the specified ID from the server
	 * returns an empty user if there is no user with that ID
	 *
	 * @param userID ID of the User to fetch
	 * @return User Object from the server
	 */
	public static User readUserLogin(String userID) {
		Transmission request = build(LOGIN, READ, userID, null);
		TransmissionListener userListener= new TransmissionListener(Thread.currentThread());
		client.addListener(userListener);
		String json =JSONTransformer.toJSON(request);
		client.socketClient.send(json);
		try {
			Thread.sleep(2000);
		}
		catch (InterruptedException e){
			client.removeListener(userListener);
			return userListener.getResponse().getUser();

		}
		return null;
	}

	/**
	 * gets all users known to the user
	 *
	 * @return all users known to the server
	 */
	public static Set<User> readUsers() {
		Transmission request = build(USERS, READ, null, null);
		TransmissionListener userListener= new TransmissionListener(Thread.currentThread());
		client.addListener(userListener);
		String json =JSONTransformer.toJSON(request);
		client.socketClient.send(json);
		try {
			Thread.sleep(2000);
		}
		catch (InterruptedException e){
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
	public static boolean updateUser(String userID, User userData) {
		Transmission request = build(USER, UPDATE, userID, userData);
		Transmission response = new Client().get(request);
		return response.getStatus();
	}

	/**
	 * deletes the specified user from the Server
	 *
	 * @param userID ID of the user
	 * @return if the operation was successful
	 */
	public static boolean deleteUser(String userID) {
		Transmission request = build(USER, DELETE, userID, null);
		Transmission response = new Client().get(request);
		return response.getStatus();
	}

	@SuppressWarnings("unchecked")
	private static Transmission build(Request type, Crud operation, String ID, Object data) {
		Transmission request = new Transmission(type, operation);
		switch (type) {
			case MESSAGE:
				switch(operation){
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
						request.setConversationID(ID).setUserIDs((Set<String>) data);
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

	static class TransmissionListener implements ConnectionListener{
		Thread thread;
		Transmission response;

		TransmissionListener(Thread currentThread){
			this.thread= currentThread;
		}
		@Override
		public void notify(ConnectionEvent e) {
			response= e.getTransmission();

			thread.interrupt();
		}

		public Transmission getResponse(){
			return response;
		}
	}


}