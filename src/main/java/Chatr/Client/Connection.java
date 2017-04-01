package Chatr.Client;

import Chatr.Converstation.Conversation;
import Chatr.Converstation.Message;
import Chatr.Converstation.User;
import Chatr.Helper.Enums.Crud;
import Chatr.Helper.Enums.Request;
import Chatr.Server.Transmission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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
	public static boolean createConversation(String conversationID, Collection<String> userIDs) {
		Transmission request = build(CONVERSATION, CREATE, conversationID, userIDs);
		Transmission response = client.get(request);
		return true;
	}

	/**
	 * read new messages from the specified conversation
	 *
	 * @param conversationID ID of the conversation to update
	 * @param newest         newest message in the local copy of the conversation
	 * @return new Messages from the server
	 */
	public static List<Message> readNewMessages(String conversationID, Message newest) {
		Transmission request = build(MESSAGE, READ, conversationID, newest);
		List<Transmission> response = client.getMultiple(request);
		return parse(response);
	}

	/**
	 * TODO: change the method of getting new conversations. Really dirty right now
	 * read all conversations for that user
	 *
	 * @param userID User ID to get the conversations for
	 * @return the users conversations
	 */
	public static List<Conversation> readAllConversations(String userID) {
		Transmission request = build(CONVERSATION, READ, userID, null);
		Transmission response = client.get(request);
		return parse(response);
	}

	/**
	 * Add a new message to a conversation
	 *
	 * @param conversationID ID of the conversation to get the message to
	 * @param message        Message to get
	 * @return if the operation was successful
	 */
	public static boolean addMessage(String conversationID, Message message) {
		client.get(build(MESSAGE, UPDATE, conversationID, message));
		return true;
	}

	/**
	 * Deletes the conversation from the server
	 *
	 * @param conversationID ID of the conversation to delete
	 * @return if the operation was successful
	 */
	public static boolean deleteConversation(String conversationID) {
		client.get(build(CONVERSATION, DELETE, conversationID, null));
		return true;
	}

	/**
	 * creates a new user on the server
	 *
	 * @param userID   ID of the User to create
	 * @param userData the new Users data
	 * @return if the operation was successful
	 */
	public static boolean createUser(String userID, User userData) {
		client.get(build(USER, CREATE, userID, userData));
		return true;
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
		return parse(response);
	}

	/**
	 * gets all users known to the user
	 *
	 * @return all users known to the server
	 */
	public static List<User> readUsers() {
		Transmission request = build(USER, READ, null, null);
		Transmission response = client.get(request);
		return parse(response);
	}

	/**
	 * update the specified users data
	 *
	 * @param userID   ID of the user to update information for
	 * @param userData new data of the user
	 * @return if the operation was successful
	 */
	public static boolean updateUser(String userID, User userData) {
		Transmission request = build(USER, CREATE, userID, userData);
		Transmission response = client.get(request);
		return parse(response);
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
		return parse(response);
	}

	@SuppressWarnings("unchecked")
	private static Transmission build(Request type, Crud operation, String ID, Object data) {
		Transmission request = new Transmission(type, operation);
		switch (type) {
			case MESSAGE:
				request.setConversationID(ID);
				request.setMessage((Message) data);
				break;
			case CONVERSATION:
				request.setConversationID(ID);
				switch (operation) {
					case CREATE:
						request.setUserIDs((List<String>) data);
						break;
					case READ:
						List<String> list = new ArrayList<>();
						list.add((String) data);
						request.setUserIDs(list);
						break;
				}
				break;
			case USER:
				request.setUser((User) data);
				break;
		}
		return request;
	}
	@SuppressWarnings("unchecked")
	private static <T> T parse(Transmission request) {
		switch (request.getRequestType()){
			case CONVERSATION:
			case MESSAGE:
			case CONNECTED:
			case DISCONNECTED:
			case NOTIFICATION:
			case STATUS:
			case USER:
		}
		return (T) new Object();
	}

	private static <T> List<T> parse(List<Transmission> responses) {
		List<T> out = new LinkedList<T>();
		responses.forEach(response -> out.add(parse(response)));
		return out;
	}


}