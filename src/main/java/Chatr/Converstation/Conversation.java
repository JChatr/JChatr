package Chatr.Converstation;

import Chatr.Client.Connection;
import Chatr.Helper.HashGen;

import java.util.*;


public class Conversation {

	private String conversationID;
	private String conversationName;
	private Set<User> members = new HashSet<>();
	private LinkedList<Message> messages = new LinkedList<>();
	private String localUser;
	private Message trash = new Message();

	/*

		private Conversation(String conversationName, Collection<User> members, User localUser) {
		this.members.addAll(members);
		this.members.add(localUser);
		this.localUser = localUser.getUserID();
		this.conversationID = HashGen.getID(false);
		this.conversationName = conversationID;
	}*/


	private Conversation(User member, User localUser) {
		this.members.add(member);
		this.members.add(localUser);
		this.localUser = localUser.getUserID();
		this.conversationID = HashGen.getID(false);
		this.conversationName = conversationID;
		Connection.createConversation(conversationID, this.getMemberIDs());
	}

	private Conversation(String conversationID, String localUserID, Set<User> members, LinkedList<Message> messages) {
		this.conversationID = conversationID;
		this.conversationName = conversationID; //!!!
		this.members = members;
		this.messages = messages;
	}

	static public Conversation newConversation(User member, User localUser) {
		return new Conversation(member, localUser);
	}

	public static Conversation preConfigServer(String conversationID, String localUserID,
	                                           Set<User> members, LinkedList<Message> messages) {
		return new Conversation(conversationID, localUserID, members, messages);

	}

	public Message newMessage(String content) {
		if (!content.trim().isEmpty()) {
			Message message = new Message(localUser, content);
			messages.add(message);
			Connection.addMessage(conversationID, message);
			return message;
		}
		return new Message();
	}


	public Set<String> getMemberIDs() {
		Set<String> s = new HashSet<>();
		members.forEach(m -> s.add(m.getUserID()));
		return s;
	}


	public void addMember(User member) {
		members.add(member);
		Connection.updateConversationUsers(conversationID, getMemberIDs());

	}

	public void setLocalUser(String userID) {
		this.localUser = userID;
	}


	public String getID() {
		return this.conversationID;
	}



/*	public String getLocalUserID() {
		return localUser;
	}*/

	/**
	 * @return
	 */
	public List<Message> update() {
		Long latest = messages.isEmpty() ? 0 : messages.getLast().getTime(); //Get timestamp
		List<Message> messages = Connection.readNewMessages(conversationID, latest);
		this.messages.addAll(messages);
		return messages;
	}

	@Override
	public boolean equals(Object o) {
		return Objects.equals(conversationID, o.toString());
	}

	@Override
	public int hashCode() {
		return Objects.hash(conversationID);
	}

	@Override
	public String toString() {
		return this.conversationID;
	}
}
