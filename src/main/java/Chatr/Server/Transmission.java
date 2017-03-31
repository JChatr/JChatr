package Chatr.Server;

import Chatr.Converstation.Message;
import Chatr.Converstation.User;
import Chatr.Helper.Enums.CRUD;
import Chatr.Helper.Enums.RequestType;

import java.util.List;

/**
 * Transmission wrapper internal to the server
 */
public class Transmission {
	private RequestType type;
	private CRUD crud;
	private String conversationID;
	private Message message;
	private User user;
	private String userID;
	private List<String> userIDs;

	public Transmission(RequestType type, CRUD crud) {
		this.type = type;
		this.crud = crud;
	}

	public RequestType getRequestType(){
		return type;
	}

	public void setRequestType(RequestType type) {
		this.type = type;
	}

	public CRUD getCRUD(){
		return crud;
	}

	public void setCRUD(CRUD crud) {
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
}
