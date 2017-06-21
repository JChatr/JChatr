package Chatr.Controller;


import Chatr.Client.Connection;
import Chatr.Helper.CONFIG;
import Chatr.Helper.Enums.ContentType;
import Chatr.Helper.UpdateService;
import Chatr.Model.Chat;
import Chatr.Model.Message;
import Chatr.Model.User;
import Chatr.Server.Server;
import Chatr.View.JavaFX;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

/**
 * starts the app
 */
public class Manager {
	public static ObjectProperty<User> localUser;
	static ListProperty<Chat> userChats;
	static ListProperty<User> users;
	static ObjectProperty<Chat> currentChat;

	private static Logger log = LogManager.getLogger(Manager.class);

	public static void main(String[] args) {
		initialize();

		log.info(String.format("Connecting to : %s", CONFIG.SERVER_ADDRESS));
		JavaFX.initGUI(args);
	}


	public static Message addMessage(String content, ContentType contentType) {
		return currentChat.get().newMessage(content, contentType);
	}

	public static ObjectProperty<Chat> getCurrentChat() {
		return currentChat;
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

	public static String getLocalUserID() {
		return localUser.get() == null ? null : localUser.get().getUserID();
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
			if (u.equals(new User(userID))) {
				log.trace("(getUserImage) User found!" + u.getUserID());
				return u.getImage();
			}
		}
			return localUser.get().getImage();
	}

		public static void setCurrentChat(Chat chat) {
		currentChat.setValue(chat);
	}

	private static void initialize() {
		localUser = new SimpleObjectProperty<>();
		ObservableList<Chat> chatSet = FXCollections.observableArrayList();
		userChats = new SimpleListProperty<>(chatSet);
		ObservableList<User> userSet = FXCollections.observableArrayList();
		users = new SimpleListProperty<>(userSet);
		currentChat = new SimpleObjectProperty<>();
	}

	public static void initialPull(){


		Set<Chat> readChats = Connection.readAllConversations(localUser.get().getUserID());
		readChats.forEach(readChat -> {
			if (!userChats.contains(readChat)) {
				userChats.add(readChat);
			}
		});

		Set<User> userSet = Connection.readUsers();
		users.addAll(userSet);
	}

	private static Chat resolveChatID(String chatID) {
		for (Chat c : userChats) {
			if (c.getID().get().equals(chatID)) return c;
		}
		throw new IllegalStateException("Chat ID could not be resolved");
	}
}
