package Chatr.Model;

import Chatr.Client.Connection;
import Chatr.Helper.HashGen;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;


public class Chat {
	private StringProperty chatID;
	private StringProperty chatName;
	private ListProperty<User> members;
	private ListProperty<Message> messages;
	private String localUserID;
	private static Logger log = LogManager.getLogger(Chat.class);

	private Chat(String chatName, User localUserID, Collection<User> otherUsers) {
		initProperties();
		this.members.add(localUserID);
		this.members.addAll(otherUsers);
		this.localUserID = localUserID.getUserID();
		chatID.set(HashGen.getID(false));
		this.chatName.set(chatName);
		Connection.createConversation(chatID.get(), this.getMemberIDs());
		log.info("New Chat created " + this);
	}

	private Chat(String chatName, String chatID, String localUserID, Set<User> members, List<Message> messages) {
		initProperties();
		this.members.addAll(members);
		this.localUserID = localUserID;
		this.chatID.set(chatID);
		this.chatName.set(chatName);
		this.messages.addAll(messages);
	}

	private void initProperties() {
		this.chatID = new SimpleStringProperty();
		this.chatName = new SimpleStringProperty();
		this.members = new SimpleListProperty<>(FXCollections.observableArrayList());
		this.messages = new SimpleListProperty<>(FXCollections.observableArrayList());
	}

	public static Chat newChat(String chatName, User localUser, User... otherUsers) {
		return new Chat(chatName, localUser, Arrays.asList(otherUsers));
	}

	public static Chat newChat(String chatName, User localUser, Collection<User> otherUsers) {
		return new Chat(chatName, localUser, otherUsers);
	}

	public static Chat preConfigServer(String chatName, String chatID, String localUserID,
	                                   Set<User> members, List<Message> messages) {
		return new Chat(chatName, chatID, localUserID, members, messages);
	}


	public Message newMessage(String content) {
		Message message = new Message(localUserID, content);
		messages.add(message);
		Connection.addMessage(chatID.get(), message);
		return message;
	}

	public ObservableList<User> getMembers() {
		return members.get();
	}

	public Set<String> getMemberIDs() {
		HashSet<String> out = new HashSet<>();
		members.forEach(m -> out.add(m.getUserID()));
		return out;
	}

	public void addMember(User member) {
		members.add(member);
		Connection.updateConversationUsers(chatID.get(), getMemberIDs());
	}

	public Chat setLocalUserID(String userID) {
		this.localUserID = userID;
		return this;
	}

	public StringProperty getID() {
		return this.chatID;
	}

	public StringProperty getName() {
		return this.chatName;
	}

	public ObservableList<Message> getMessages() {
		return this.messages;
	}

	@Override
	public boolean equals(Object o) {
		return o != null &&
				getClass().equals(o.getClass()) &&
				Objects.equals(chatID.get(), o.toString());
	}

	@Override
	public int hashCode() {
		return Objects.hash(chatID.get());
	}

	@Override
	public String toString() {
		return this.chatID.get();
	}
}
