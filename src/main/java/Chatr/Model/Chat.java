package Chatr.Model;

import Chatr.Client.Connection;
import Chatr.Helper.Enums.ContentType;
import Chatr.Controller.Manager;
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

	private Chat(String chatName, User localUser, Collection<User> otherUsers) {
		initProperties();
		this.members.add(localUser);
		this.members.addAll(otherUsers);
		this.localUserID = localUser.getUserID();
		chatID.set(HashGen.getID(false));
		this.chatName.set(chatName);
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

	private void initProperties() {
		this.chatID = new SimpleStringProperty();
		this.chatName = new SimpleStringProperty();
		this.members = new SimpleListProperty<>(FXCollections.observableArrayList());
		this.messages = new SimpleListProperty<>(FXCollections.observableArrayList());
	}

	public void addMessage(String content, ContentType contentType) {
		Message message = new Message(localUserID, content, contentType);
		messages.add(message);
		Connection.addMessage(chatID.get(), message);
	}

	public void addMessage(Message message) {
		messages.add(message);
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
		Connection.updateChatUsers(chatID.get(), getMemberIDs());
	}

	public Chat setLocalUserID(String userID) {
		this.localUserID = userID;
		return this;
	}

	public String getID() {
		return this.chatID.get();
	}

	public StringProperty getName() {
		return this.chatName;
	}

	public ObservableList<Message> getMessages() {
		return this.messages;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (!(o instanceof Chat)) return false;
		Chat c = (Chat) o;
		return Objects.equals(chatName.get(), c.getName().get()) &&
				Objects.equals(chatID.get(), c.getID()) &&
				Objects.equals(members, c.getMembers()) &&
				Objects.equals(messages, c.getMessages());
	}

	@Override
	public int hashCode() {
		return Objects.hash(chatID.get());
	}

	@Override
	public String toString() {
		return String.format("ID: %s Name: %s Members: %s", chatID.get(), chatName.get(), members.get().toString());
	}
}
