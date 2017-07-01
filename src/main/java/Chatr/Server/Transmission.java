package Chatr.Server;


import Chatr.Controller.Manager;
import Chatr.Helper.Enums.Crud;
import Chatr.Helper.Enums.RequestType;
import Chatr.Model.Chat;
import Chatr.Model.Message;
import Chatr.Model.User;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

/**
 * Transmission wrapper used  as a datastore in client / server communication
 */
public class Transmission implements Cloneable {
	private RequestType type;
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
	private Chat chat;
	private Set<Chat> chats;
	private Long timestamp;
	private byte[] img;
	private byte[] voice;

	public Transmission(RequestType type, Crud crud) {
		this.type = type;
		this.crud = crud;
		try {
			this.localUserID = Manager.getLocalUser().get().getID();
		} catch (NullPointerException e) {
			this.localUserID = "DEFAULT042b9135b65cc71d9c94df01add70cbf";
		}
	}

	public RequestType getRequestType() {
		return type;
	}

	public Crud getCRUD() {
		return crud;
	}

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

	public String getChatID() {
		return this.conversationID;
	}

	public Transmission setChatID(String conversationID) {
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

	public Boolean getStatus() {
		return status;
	}

	public Transmission setStatus(Boolean status) {
		this.status = status;
		return this;
	}

	public Chat getChat() {
		return this.chat;
	}

	public Transmission setChat(Chat chat) {
		this.chat = chat;
		return this;
	}

	public String getUserID() {
		return this.userID;
	}

	public Transmission setUserID(String userID) {
		this.userID = userID;
		return this;
	}

	public Set<Chat> getChats() {
		return this.chats;
	}

	public Transmission setChats(Set<Chat> chats) {
		this.chats = chats;
		return this;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public Transmission setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	public String getLocalUserID() {
		return this.localUserID;
	}

	public Transmission setLocalUserID(String userID) {
		this.localUserID = userID;
		return this;
	}

	public Transmission reset() {
		message = null;
		messages = null;
		user = null;
		userIDs = null;
		userID = null;
		users = null;
		status = null;
		timestamp = null;
		chat = null;
		chats = null;
		img = null;
		voice = null;
		return this;
	}

	@Override
	protected Transmission clone() {
		try {
			return (Transmission) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
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
			return sb.toString();
		}
		return sb.toString();
	}
}