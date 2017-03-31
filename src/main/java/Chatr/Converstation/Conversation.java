package Chatr.Converstation;

import Chatr.Helper.HashGen;

import java.util.*;


public class Conversation {

	private String conversationID;
	private String conversationName;
	private Set<User> members = new HashSet<>();
	private List<Message> messages = new ArrayList<>();
	private User localUser;

	private Conversation(String conversationName, Collection<User> members, User localUSer) {
		this.members.addAll(members);
		this.members.add(localUSer);
		this.localUser = localUser;
		this.conversationName = conversationName;
		this.conversationID = HashGen.getID(false);
	}


	private Conversation(User member, User localUser) {
		this.members.add(member);
		this.members.add(localUser);
		this.localUser = localUser;
		this.conversationName = member.getUserName();
		this.conversationID = HashGen.getID(false);
	}


	static public Conversation newConversation(User member, User localUser) {
		Conversation pCon = new Conversation(member, localUser);
		return pCon;
	}

	public void newMessage(String content) throws Exception {
		if (content.trim().isEmpty()) {
		} else {
			messages.add(new Message(localUser.getUserID(), content));
		}
	}

	public List<Message> getMessages() {
		return messages;
	}

	public List<User> getMembers() {
		List<User> us = new ArrayList<>();
		members.forEach(us::add);
		return us;
	}

	public void addMessages(List<Message> messages) {
		this.messages.addAll(messages);
	}

	public void addMembers(Set<User> members) {
		this.members.addAll(members);
	}

	public String getID() {
		return this.conversationID;
	}

}

