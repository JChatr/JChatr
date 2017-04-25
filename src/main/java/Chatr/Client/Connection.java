package Chatr.Client;

import Chatr.Converstation.Conversation;
import Chatr.Converstation.Message;
import Chatr.Converstation.User;
import Chatr.Helper.Enums.Crud;
import Chatr.Helper.Enums.Request;
import Chatr.Server.Transmission;

import java.util.*;

import static Chatr.Helper.Enums.Crud.*;
import static Chatr.Helper.Enums.Request.*;

public class Connection {
	private static Client client = new Client();

	private Connection() {
	}

	/**
	 * creates a new conversation on the server and adds the specified users
	 *
	 * @param conversationID ID of the new conversation
	 * @param userIDs        users to add to the conversation
	 * @return if the operation was successful
	 */
	public static boolean createConversation(String conversationID, Set<String> userIDs) {
		Transmission request = build(CONVERSATION, CREATE, conversationID, userIDs);
		Transmission response = client.get(request);
		return response.getStatus();
	}

	/**
	 * TODO: change the method of getting new conversations. Really dirty right now
	 * read all conversations for that user
	 *
	 * @param userID User ID to get the conversations for
	 * @return the users conversations
	 */
	public static Set<Conversation> readAllConversations(String userID) {
		Transmission request = build(CONVERSATION, READ, userID, null);
		Transmission response = client.get(request);
		return response.getConversations();
	}

	/**
	 * Deletes the conversation from the server
	 *
	 * @param conversationID ID of the conversation to delete
	 * @return if the operation was successful
	 */
	public static boolean deleteConversation(String conversationID) {
		Transmission request = build(CONVERSATION, DELETE, conversationID, null);
		Transmission response = client.get(request);
		return response.getStatus();
	}

	public static boolean updateConversationUsers(String conversationID, Set<String> userIDs) {
		Transmission request = build(CONVERSATION, UPDATE, conversationID, userIDs);
		Transmission response = client.get(request);
		return response.getStatus();
	}

	/**
	 * read new messages from the specified conversation
	 *
	 * @param conversationID ID of the conversation to update
	 * @param newest         newest message in the local copy of the conversation
	 * @return new Messages from the server
	 */
	public static List<Message> readNewMessages(String conversationID, Long newest) {
		Transmission request = build(MESSAGE, READ, conversationID, newest);
		Transmission response = client.get(request);
		return response.getMessages();
	}

	/**
	 * Add a new message to a conversation
	 *
	 * @param conversationID ID of the conversation to get the message to
	 * @param message        Message to get
	 * @return if the operation was successful
	 */
	public static boolean addMessage(String conversationID, Message message) {
		Transmission request = build(MESSAGE, CREATE, conversationID, message);
		Transmission response = client.get(request);
		return response.getStatus();
	}


	/**
	 * creates a new user on the server
	 *
	 * @param userID   ID of the User to create
	 * @param userData the new Users data
	 * @return if the operation was successful
	 */
	public static boolean createUser(String userID, User userData) {
		Transmission request = build(USER, CREATE, userID, userData);
		Transmission response = client.get(request);
		return response.getStatus();
	}

	/**
	 * read the User with the specified ID from the server
	 * returns an empty user if there is no user with that ID
	 *
	 * @param userID ID of the User to fetch
	 * @return User Object from the server
	 */
	public static User readUser(String userID) {
		Transmission request = build(USER, READ, userID, null);
		Transmission response = client.get(request);
		return response.getUser();
	}

	/**
	 * gets all users known to the user
	 *
	 * @return all users known to the server
	 */
	public static Set<User> readUsers() {
		Transmission request = build(USERS, READ, null, null);
		Transmission response = client.get(request);
		return response.getUsers();
	}

	/**
	 * update the specified users data
	 *
	 * @param userID   ID of the user to update information for
	 * @param userData new data of the user
	 * @return if the operation was successful
	 */
	public static boolean updateUser(String userID, User userData) {
		Transmission request = build(USER, UPDATE, userID, userData);
		Transmission response = client.get(request);
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
		Transmission response = client.get(request);
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
		}
		return request;
	}
}