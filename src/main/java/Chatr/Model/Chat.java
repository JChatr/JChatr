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

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;


public class Chat {

	private StringProperty conversationID;
	private StringProperty conversationName;
	private ListProperty<User> members;
	private ListProperty<Message> messages;
	private String localUser;

	private static Logger log = LogManager.getLogger(Chat.class);

	private Chat(User localUser, User... otherUsers) {
		initialize();
		this.members.add(localUser);
		this.members.addAll(otherUsers);
		this.localUser = localUser.getUserID();
		conversationID.set(HashGen.getID(false));
		this.conversationName = conversationID;
		Connection.createConversation(conversationID.get(), this.getMemberIDs());
	}

	private Chat(String conversationID, String localUserID, Set<User> members, List<Message> messages) {
		initialize();
		this.members.addAll(members);
		this.localUser = localUserID;
		this.conversationID.set(conversationID);
		this.conversationName = this.conversationID;
		this.messages.addAll(messages);
	}

	static public Chat newConversation(User localUser, User... otherUsers) {
		return new Chat(localUser, otherUsers);
	}

	public static Chat preConfigServer(String conversationID, String localUserID,
	                                   Set<User> members, List<Message> messages) {
		return new Chat(conversationID, localUserID, members, messages);
	}

	private void initialize() {
		this.conversationID = new SimpleStringProperty();
		this.conversationName = new SimpleStringProperty();
		this.members = new SimpleListProperty<>(FXCollections.observableArrayList());
		this.messages = new SimpleListProperty<>(FXCollections.observableArrayList());
	}

	public Message newMessage(String content) {
		Message message = new Message(localUser, content);
		messages.add(message);
		Connection.addMessage(conversationID.get(), message);
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
		Connection.updateConversationUsers(conversationID.get(), getMemberIDs());

	}

	public void setLocalUser(String userID) {
		this.localUser = userID;
	}

	public StringProperty getID() {
		return this.conversationID;
	}

	public StringProperty getName() {
		return this.conversationName;
	}

	public ObservableList<Message> getMessages() {
		return this.messages;
	}

	/**
	 * @return
	 */
//	public void update() {
//		UpdateService.schedule(messages, m -> {
//			int lastMessageIndex = messages.getSize() - 1;
//			long newestMessageTime = messages.isEmpty() ?
//					0 : messages.get(lastMessageIndex).getTime(); //Get timestamp
//			final List<Message> newMessages = Connection.readNewMessages(conversationID.get(), newestMessageTime);
////			m.addAll(newMessages);
//			return newMessages;
//		});
//	}

	@Override
	public boolean equals(Object o) {
		return o != null &&
				getClass().equals(o.getClass()) &&
				Objects.equals(conversationID.get(), o.toString());
	}

	@Override
	public int hashCode() {
		return Objects.hash(conversationID.get());
	}

	@Override
	public String toString() {
		return this.conversationID.get();
	}
}
