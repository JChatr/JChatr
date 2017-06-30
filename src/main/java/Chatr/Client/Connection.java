package Chatr.Client;

import Chatr.Model.Chat;
import Chatr.Model.Message;
import Chatr.Model.User;
import Chatr.Server.Transmission;

import java.util.List;
import java.util.Set;

import static Chatr.Helper.Enums.Crud.*;
import static Chatr.Helper.Enums.RequestType.*;

public final class Connection {

	private static final Client client = new Client();

	/**
	 * creates a new conversation on the server and adds the specified users
	 *
	 * @param chat Chat to create on the server
	 * @return if the operation was successful
	 */
	public static void createChat(Chat chat) {
		Transmission request = new Transmission(CHAT, CREATE).setChat(chat);
		client.sendAsync(request);
	}

	/**
	 * Read all conversations for that user
	 * (Waits for server response)
	 *
	 * @param userID User ID to get the conversations for
	 * @return the users conversations
	 */
	public static Set<Chat> readAllUserChats(String userID) {
		Transmission request = new Transmission(CHAT, READ).setUserID(userID);
		return client.send(request).getChats();
	}

	/**
	 *
	 * @param chat the chat to overwrite on the database
	 */
	public static void updateChat(Chat chat) {
		Transmission request = new Transmission(CHAT, UPDATE)
				.setChat(chat);
		client.sendAsync(request);
	}

	/**
	 * Deletes the conversation from the server
	 *
	 * @param chatID ID of the conversation to delete
	 * @return if the operation was successful
	 */
	public static void deleteChat(String chatID) {
		Transmission request = new Transmission(CHAT, DELETE)
				.setChatID(chatID);
		client.sendAsync(request);
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
		Transmission request = new Transmission(MESSAGE, READ)
				.setChatID(conversationID)
				.setTimestamp(newest);
		return client.send(request).getMessages();
	}

	/**
	 * Add a new message to a conversation
	 *
	 * @param conversationID ID of the conversation to get the message to
	 * @param message        Message to get
	 * @return if the operation was successful
	 */
	public static void addMessage(String conversationID, Message message) {
		Transmission request = new Transmission(MESSAGE, CREATE)
				.setChatID(conversationID)
				.setMessage(message);
		client.sendAsync(request);
	}

	/**
	 * creates a new user on the server
	 *
	 * @param user the new Users data
	 * @return if the operation was successful
	 */
	public static void createUser(User user) {
		Transmission request = new Transmission(LOGIN, CREATE)
				.setUserID(user.getID())
				.setUser(user);
		client.sendAsync(request);
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
		Transmission request = new Transmission(LOGIN, READ)
				.setUserID(userID);
		return client.send(request).getUser();
	}

	/**
	 * gets all users known to the user
	 * (Waits for server response)
	 *
	 * @return all users known to the server
	 */
	public static Set<User> readUsers() {
		Transmission request = new Transmission(USERS, READ);
		return client.send(request).getUsers();
	}


	/**
	 * deletes the specified user from the Server
	 *
	 * @param userID ID of the user
	 * @return if the operation was successful
	 */
	public static void deleteUser(String userID) {
		Transmission request = new Transmission(USER, DELETE)
				.setUserID(userID);
		client.sendAsync(request);
	}
}