package Chatr.Server;

import Chatr.Converstation.Message;
import Chatr.Converstation.User;
import Chatr.Helper.Enums.Crud;
import Chatr.Helper.Enums.Request;
import Chatr.Manager;

import java.util.List;

/**
 * Transmission wrapper used  as a datastore in client / server communication
 */
public class Transmission {
	private Request type;
	private Crud crud;
	private String conversationID;
	private Message message;
	private List<Message> messages;
	private User user;
	private String localUserID;
	private List<String> userIDs;
	private Boolean status;
	private byte[] img;
	private byte[] voice;

	public Transmission(Request type, Crud crud) {
		this.type = type;
		this.crud = crud;
		this.localUserID = Manager.localUser.getUserID();
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

	public List<String> getUserIDs() {
		return userIDs;
	}

	public Transmission setUserIDs(List<String> userIDs) {
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

	public boolean getStatus() {
		return status;
	}

	public Transmission setStatus(boolean status) {
		this.status = status;
		return this;
	}

	public Transmission reset() {
		this.message = null;
//		this.user = null;
		this.localUserID = null;
		this.userIDs = null;
		this.img = null;
		this.voice = null;
		this.status = null;
		return this;
	}

	@Override
	public String toString() {
		return String.format("%s -> %s @%s", type, crud, this.hashCode());
	}
}
