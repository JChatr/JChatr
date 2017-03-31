package Chatr.Client;

import Chatr.Converstation.Conversation;
import Chatr.Converstation.Message;
import Chatr.Converstation.User;
import Chatr.Helper.Enums.CRUD;
import Chatr.Helper.Enums.RequestType;
import Chatr.Server.Transmission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


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
	public static void createConversation(String conversationID, Collection<String> userIDs) {
		Transmission request = build(RequestType.CONVERSATION, CRUD.UPDATE, conversationID, userIDs);
		Transmission response = new Client().get(request);
	}

	/**
	 * read new messages from the specified conversation
	 *
	 * @param conversationID ID of the conversation to update
	 * @param newest         newest message in the local copy of the conversation
	 * @return new Messages from the server
	 */
	public static List<Message> readConversation(String conversationID, Message newest) {
		Transmission request = build(RequestType.CONVERSATION, CRUD.READ, conversationID, newest);
		List<String> response = new Client().get(request);
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
		Transmission request = build(RequestType.CONVERSATION, CRUD.READ, userID, null);
		Transmission response = new Client().get(request);
		return parse(response);
	}

	/**
	 * Add a new message to a conversation
	 *
	 * @param conversationID ID of the conversation to post the message to
	 * @param message        Message to post
	 * @return if the operation was successful
	 */
	public static boolean updateConversation(String conversationID, Message message) {
		new Client().post(build(RequestType.CONVERSATION, CRUD.CREATE, conversationID, message));
		return true;
	}

	/**
	 * Deletes the conversation from the server
	 *
	 * @param conversationID ID of the conversation to delete
	 * @return if the operation was successful
	 */
	public static boolean deleteConversation(String conversationID) {
		new Client().post(build(RequestType.CONVERSATION, CRUD.DELETE, conversationID, null));
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
		new Client().post(build(RequestType.USER, CRUD.CREATE, userID, userData));
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
		Transmission request = build(RequestType.USER, CRUD.READ, userID, null);

		return new User();
	}

	/**
	 * gets all users known to the user
	 *
	 * @return all users known to the server
	 */
	public static List<User> readUsers() {
		return new ArrayList<>();
	}

	/**
	 * update the specified users data
	 *
	 * @param userID   ID of the user to update information for
	 * @param userData new data of the user
	 * @return if the operation was successful
	 */
	public static boolean updateUser(String userID, User userData) {
		return true;
	}

	/**
	 * deletes the specified user from the Server
	 *
	 * @param userID ID of the user
	 * @return if the operation was successful
	 */
	public static boolean deleteUser(String userID) {
		return true;
	}

	@SuppressWarnings("unchecked")
	private static Transmission build(RequestType type, CRUD operation, String ID, Object data) {
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
			case USER:
				request.setUser((User) data);
				break;
			case CONNECTED:
				request.setUser((User) data);
				break;
			case DISCONNECTED:
				request.setUser((User) data);
				break;
		}
		return request;
	}
	@SuppressWarnings("unchecked")
	private static <T> T parse(Transmission request) {
		return (T) new Object();
	}

	private static <T> List<T> parse(List<Transmission> responses) {
		List<T> out = new LinkedList<T>();
		responses.forEach(response -> out.add(parse(response)));
		return out;
	}


}