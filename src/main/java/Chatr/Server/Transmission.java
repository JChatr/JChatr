package Chatr.Server;

import Chatr.Converstation.Conversation;
import Chatr.Converstation.Message;
import Chatr.Converstation.User;
import Chatr.Helper.Enums.Crud;
import Chatr.Helper.Enums.Request;
import Chatr.Manager;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

/**
 * Transmission wrapper used  as a datastore in client / server communication
 */
public class Transmission {
	private Request type;
	private Crud crud;
	private String localUserID;
	private String conversationID;
	private Message message;
	private List<Message> messages;
	private User user;
	private Set<String> userIDs;
	private String userID;
	private Set<User> users;
	private Boolean status;
	private Conversation conversation;
	private Set<Conversation> conversations;
	private byte[] img;
	private byte[] voice;

	public Transmission(Request type, Crud crud) {
		this.type = type;
		this.crud = crud;
		this.localUserID = Manager.localUser == null ? null :Manager.localUser.getUserID();
	}

	public Request getRequestType() {
		return type;
	}

//	public Transmission setRequestType(Request type) {
//		this.type = type;
//		return this;
//	}

	public Crud getCRUD() {
		return crud;
	}

//	public Transmission setCRUD(Crud crud) {
//		this.crud = crud;
//		return this;
//	}

	public Message getMessage() {
		return message;
	}

	public Transmission setMessage(Message message) {
		this.message = message;
		return this;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public Transmission setMessages(List<Message> messages) {
		this.messages = messages;
		return this;
	}

	public String getConversationID() {
		return this.conversationID;
	}

	public Transmission setConversationID(String conversationID) {
		this.conversationID = conversationID;
		return this;
	}

	public User getUser() {
		return user;
	}

	public Transmission setUser(User user) {
		this.user = user;
		return this;
	}

	public Set<User> getUsers() {
		return this.users;
	}

	public Transmission setUsers(Set<User> users) {
		this.users = users;
		return this;
	}

	public Set<String> getUserIDs() {
		return userIDs;
	}

	public Transmission setUserIDs(Set<String> userIDs) {
		this.userIDs = userIDs;
		return this;
	}

	public String getLocalUserID() {
		return localUserID;
	}

	public Transmission setLocalUserID(String userID) {
		this.localUserID = userID;
		return this;
	}

	public Boolean getStatus() {
		return status;
	}

	public Transmission setStatus(Boolean status) {
		this.status = status;
		return this;
	}

	public Conversation getConversation() {
		return this.conversation;
	}

	public Transmission setConversation(Conversation conversation) {
		this.conversation = conversation;
		return this;
	}

	public String getUserID(){
		return this.userID;
	}

	public Transmission setUserID(String userID) {
		this.userID = userID;
		return this;
	}

	public Set<Conversation> getConversations(){
		return this.conversations;
	}

	public Transmission setConversations(Set<Conversation> conversations) {
		this.conversations = conversations;
		return this;
	}

	public Transmission reset() {
//		type = null;
//		crud = null;
//		localUserID = null;
//		conversationID = null;
		message = null;
		messages = null;
		user = null;
		userIDs = null;
		userID = null;
		users = null;
		status = null;
		conversation = null;
		conversations = null;
		img = null;
		voice = null;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		try {
			for (Field field : this.getClass().getDeclaredFields()) {
				if (field.get(this) != null) {
					sb.append(field.getName()).append(": ").append(field.get(this)).append(", ");
				}
			}
		} catch (IllegalAccessException e) {
		}
		return sb.toString();
	}
}
