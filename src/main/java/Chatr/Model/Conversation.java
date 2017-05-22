package Chatr.Model;

import Chatr.Client.Connection;
import Chatr.Helper.HashGen;

import java.util.*;


public class Conversation {

	private String conversationID;
	private String conversationName;
	private Set<User> members = new HashSet<>();
	private LinkedList<Message> messages = new LinkedList<>();
	private String localUserID;
	private Long newestMessageTime = 0L;

	/*

		private Conversation(String conversationName, Collection<User> members, User localUserID) {
		this.members.addAll(members);
		this.members.add(localUserID);
		this.localUserID = localUserID.getUserID();
		this.conversationID = HashGen.getID(false);
		this.conversationName = conversationID;
	}*/


	private Conversation(User member, User localUserID) {
		this.members.add(member);
		this.members.add(localUserID);
		this.localUserID = localUserID.getUserID();
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
			Message message = new Message(localUserID, content);
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

	public void setLocalUserID(String userID) {
		this.localUserID = userID;
	}


	public String getID() {
		return this.conversationID;
	}

	public void resetSentMessages() {
		this.newestMessageTime = -1L;
	}

	/**
	 * @return
	 */
	public List<Message> update() {
		newestMessageTime = messages.isEmpty() || newestMessageTime == -1
				? 0 : messages.getLast().getTime(); //Get timestamp
		List<Message> messages = Connection.readNewMessages(conversationID, newestMessageTime);
		this.messages.addAll(messages);
		return messages;
	}

	@Override
	public boolean equals(Object o) {
		return o != null && Objects.equals(conversationID, o.toString());
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