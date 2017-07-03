package Chatr.Controller;


import Chatr.Client.Connection;
import Chatr.Helper.CONFIG;
import Chatr.Helper.Enums.MessageType;
import Chatr.Model.Chat;
import Chatr.Model.Message;
import Chatr.Model.User;
import Chatr.View.JavaFX;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

/**
 * starts the app
 */
public class Manager {
	private static ObjectProperty<User> localUser;
	private static ListProperty<Chat> userChats;
	private static ListProperty<User> users;
	private static ObjectProperty<Chat> currentChat;

	private static Logger log = LogManager.getLogger(Manager.class);

	public static void main(String[] args) {
		initialize();

		JavaFX.initGUI(args);
	}


	public static void addMessage(String content, MessageType messageType) {
		currentChat.get().addMessage(content, messageType);
	}

	public static Message addMessage(String content, MessageType messageType, int width, int height, Image gifImg) {
		return currentChat.get().addMessage(content, messageType, width, height, gifImg);
	}

	public static ObjectProperty<Chat> getCurrentChat() {
		return currentChat;
	}

	public static void setCurrentChat(Chat chat) {
		currentChat.setValue(chat);
	}

	public static ObservableList<User> getUsers() {
		return users.get();
	}

	public static ObservableList<Chat> getUserChats() {
		return userChats.get();
	}

	public static StringProperty getLocalUserName() {
		return new SimpleStringProperty(localUser.get().getUserName());
	}

	public static ObjectProperty<User> getLocalUser() {
		return localUser;
	}

	public static String getLocalUserID() {
		return localUser.get() == null ? null : localUser.get().getID();
	}

	public static void setLocalUser(User user) {
		localUser.setValue(user);
	}

	public static ObservableList<User> getChatMembers(String chatID) {
		return resolveChatID(chatID).getMembers();
	}

	public static StringProperty getChatName(String chatID) {
		return resolveChatID(chatID).getName();
	}

	public static ObservableList<Message> getChatMessages(String chatID) {
		return resolveChatID(chatID).getMessages();
	}

	public static ObjectProperty<Image> getUserImage(String userID) {
		for (User u : users) {
			if (u.getID().equals(userID)) {
				log.trace("(getUserImage) User found!" + u.getID());
				return u.getImage();
			}
		}
		return localUser.get().getImage();
	}

	private static void initialize() {
		localUser = new SimpleObjectProperty<>();
		userChats = new SimpleListProperty<>(FXCollections.observableArrayList());
		users = new SimpleListProperty<>(FXCollections.observableArrayList());
		currentChat = new SimpleObjectProperty<>();
	}


	public static void initialPull() {
		Set<Chat> readChats = Connection.readAllUserChats(localUser.get().getID());
		readChats.forEach(readChat -> {
			if (!userChats.contains(readChat)) userChats.add(readChat);
		});
		Set<User> userSet = Connection.readUsers();
		users.addAll(userSet);
	}

	private static Chat resolveChatID(String chatID) {
		for (Chat c : userChats) {
			if (c.getID().equals(chatID)) return c;
		}
		throw new IllegalStateException("Chat ID could not be resolved");
	}
}
