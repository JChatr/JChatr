package Chatr.Converstation;

import Chatr.Client.Connection;
import Chatr.Helper.HashGen;

import java.util.*;


public class Conversation {

	private String conversationID;
	private String conversationName;
	private Set<User> members = new HashSet<>();
	private LinkedList<Message> messages = new LinkedList<>();
	private User localUser;
	private Message trash = new Message();

	private Conversation(String conversationName, Collection<User> members, User localUSer) {
		this.members.addAll(members);
		this.members.add(localUSer);
		this.localUser = localUser;
		this.conversationID = HashGen.getID(false);
		this.conversationName = conversationID;
	}


	private Conversation(User member, User localUser) {
		this.members.add(member);
		this.members.add(localUser);
		this.localUser = localUser;
		this.conversationID = HashGen.getID(false);
		this.conversationName = conversationID;
		Connection.createConversation(conversationID, this.getMemberIDs());
	}

	private Conversation() {
		this.localUser = localUser;
		this.conversationID = HashGen.getID(false);
		this.conversationName = conversationID;
		Connection.createConversation(conversationID, this.getMemberIDs());
	}


	static public Conversation newConversation(User member, User localUser) {
		return new Conversation(member, localUser);
	}

	static public Conversation newConversation() {
		return new Conversation();
	}

	public Message newMessage(String content) {
		if (!content.trim().isEmpty()) {
			Message message = new Message(localUser.getUserID(), content);
			messages.add(message);
			Connection.addMessage(conversationID, message);
			return message;
		}
		return new Message();
	}

	public List<Message> getMessages() {
		return messages;
	}

	public List<String> getMemberIDs() {
		List<String> us = new ArrayList<>();
		members.forEach(m -> us.add(m.getUserID()));
		return us;
	}

	public void addMessages(List<Message> messages) {
		this.messages.addAll(messages);
	}

	public void addMember(User member) {
		members.add(member);

	}

	public void addMembers(Set<User> members) {
		this.members.addAll(members);
	}

	public String getID() {
		return this.conversationID;
	}

	public Conversation setID(String conversationID) {
		this.conversationID = conversationID;
		return this;
	}
	/**
	 * @return
	 */
	public List<Message> update() {
		Message latest = messages.isEmpty() ? trash : messages.getLast();
		return Connection.readNewMessages(conversationID, latest);
	}

}

