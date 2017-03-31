package Chatr.Client;

import Chatr.Converstation.Message;
import Chatr.Converstation.PrivateConversation;
import Chatr.Converstation.User;
import Chatr.Helper.CRUD;
import Chatr.Helper.TransmissionProtocol;

import java.util.ArrayList;
import java.util.List;

public class Connection {

	/**
	 * creates a new conversation on the server and adds the specified users
	 * TODO: clean tmp Data up!!!
	 * @param conversationID ID of the new conversation
	 * @param userIDs        users to add to the conversation
	 * @return if the operation was successful
	 */
	public static boolean createConversation(String conversationID, List<String> userIDs) {
		new Client().post(TransmissionProtocol.build(CRUD.UPDATE, conversationID, new Message()));
		return true;
	}

	/**
	 * read new messages from the specified conversation
	 *
	 * @param conversationID ID of the conversation to update
	 * @param newest         newest message in the local copy of the conversation
	 * @return new Messages from the server
	 */
	public static List<Message> readConversation(String conversationID, Message newest) {
		List<String> received = new Client().get(TransmissionProtocol.build(CRUD.READ, conversationID, newest));
		return TransmissionProtocol.parse(CRUD.READ, received);
	}

	/**
	 * TODO: change the method of getting new conversations. Really dirty right now
	 * read all conversations for that user
	 *
	 * @param userID User ID to get the conversations for
	 * @return the users conversations
	 */
	public static List<PrivateConversation> readConversations(String userID) {
		return new ArrayList<>();
	}

	/**
	 * Add a new message to a conversation
	 *
	 * @param conversationID ID of the conversation to post the message to
	 * @param message        Message to post
	 * @return if the operation was successful
	 */
	public static boolean updateConversation(String conversationID, Message message) {
		new Client().post(TransmissionProtocol.build(CRUD.CREATE, conversationID, message));
		return true;
	}

	/**
	 * Deletes the conversation from the server
	 *
	 * @param conversationID ID of the conversation to delete
	 * @return if the operation was successful
	 */
	public static boolean deleteConversation(String conversationID) {
		new Client().post(TransmissionProtocol.build(CRUD.DELETE, conversationID, null));
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
}