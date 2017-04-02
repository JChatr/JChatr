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

	private Conversation(String conversationName, Collection<User> members, User localUser) {
		this.members.addAll(members);
		this.members.add(localUser);
		this.localUser = localUser.getUserID();
		this.conversationID = HashGen.getID(false);
		this.conversationName = conversationID;
	}


	private Conversation(User member, User localUser) {
		this.members.add(member);
		this.members.add(localUser);
		this.localUser = localUser.getUserID();
		this.conversationID = HashGen.getID(false);
		this.conversationName = conversationID;
		Connection.createConversation(conversationID, this.getMemberIDs());
	}

	private Conversation() {
		this.conversationID = HashGen.getID(false);
		this.conversationName = conversationID;
	}


	static public Conversation newConversation(User member, User localUser) {
		return new Conversation(member, localUser);
	}

	static public Conversation newConversation() {
		return new Conversation();
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

	public List<Message> getMessages() {
		return messages;
	}

	public Set<String> getMemberIDs() {
		Set<String> s = new HashSet<>();
		members.forEach(m -> s.add(m.getUserID()));
		return s;
	}

	public void addMessages(List<Message> messages) {
		this.messages.addAll(messages);
	}

	public void addMember(User member) {
		members.add(member);
		Connection.updateConversationUsers(conversationID, getMemberIDs());

	}

	public Conversation setMembers(Set<User> members) {
		this.members = members;
		return this;
	}

	public String getID() {
		return this.conversationID;
	}

	public Conversation setID(String conversationID) {
		this.conversationID = conversationID;
		return this;
	}

	public String getLocalUserID() {
		return localUser;
	}

	public Conversation setLocalUserID(String userID) {
		this.localUser = userID;
		return this;
	}
	/**
	 * @return
	 */
	public List<Message> update() {
		Message latest = messages.isEmpty() ? trash : messages.getLast();
		List<Message> messages =  Connection.readNewMessages(conversationID, latest);
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

