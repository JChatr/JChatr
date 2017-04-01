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
	private User user;
	private String localUserID;
	private List<String> userIDs;
	private byte[] img;
	private byte[] voice;

	public Transmission(Request type, Crud crud) {
		this.type = type;
		this.crud = crud;
		this.localUserID = Manager.localUser.getUserID();
	}

	public Request getRequestType(){
		return type;
	}

	public void setRequestType(Request type) {
		this.type = type;
	}

	public Crud getCRUD(){
		return crud;
	}

	public void setCRUD(Crud crud) {
		this.crud = crud;
	}

	public Message getMessage(){
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public String getConversationID() {
		return this.conversationID;
	}

	public void setConversationID(String conversationID) {
		this.conversationID = conversationID;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<String> getUserIDs (){
		return userIDs;
	}

	public void setUserIDs(List<String> userIDs) {
		this.userIDs = userIDs;
	}

	public String getLocalUserID() {
		return localUserID;
	}

	public void setLocalUserID(String userID) {
		this.localUserID = userID;
	}

	@Override
	public String toString() {
		return String.format("%s -> %s @%s", type, crud, this.hashCode());
	}
}
