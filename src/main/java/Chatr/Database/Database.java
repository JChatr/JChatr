package Chatr.Database;

import Chatr.Converstation.Message;
import Chatr.Converstation.PrivateConversation;
import Chatr.Converstation.User;

import java.util.List;

public class Database {
	private L1Cache<User> users;
	private L1Cache<String> link;
	private L1Cache<PrivateConversation> conversations;

	private Database() {

	}

	public static Database newSQLDatabase() {

	}


	public static Database newCachedDatabase() {

	}

	public static Database newCache() {

	}

	public User readUser() {

	}

	public boolean addUser() {
		return true;
	}

	public boolean deleteUser() {
		return true;
	}

	public boolean updateUser() {

	}

	public void readUserConversations(String userId) {

	}

	public void readConversation(String conversationID) {

	}

	public void readNewerMessages(String conversationID, Long timestamp) {

	}

	public void readMessage(String conversationID, Long timestamp) {

	}

	public void addMessage(String conversationID, List<Message> messages) {

	}

	public void addMessage(String conversationID, Message message) {

	}

	public boolean deleteMessage(String conversationID, Long timestamp) {
		return true;
	}

	public boolean updateMessage(String conversationID, Long timestamp) {
		return true;
	}
}
