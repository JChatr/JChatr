package Chatr.Controller;


import Chatr.Client.Connection;
import Chatr.Model.Chat;
import Chatr.Model.Message;
import Chatr.Model.User;
import Chatr.Helper.CONFIG;
import Chatr.Helper.Terminal;
import Chatr.Server.Server;
import Chatr.View.JavaFX;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;
import java.util.concurrent.Executors;

/**
 * starts the app
 * TODO: NEEDS to be rewritten
 */
public class Manager {
	public static ObjectProperty<User> localUser;
	private static ListProperty<Chat> userChats;
	private static ListProperty<User> users;

	private static Logger log = LogManager.getLogger(Manager.class);


	public static void main(String[] args) {
		startServer();
		initialize();
		Executors.newSingleThreadExecutor().execute(() -> JavaFX.initGUI(args));
		log.info(String.format("Connecting to  : %s", CONFIG.SERVER_ADDRESS));
	}

	public static Message addMessage(String content, String chatID) {
		return resolveChatID(chatID).newMessage(content);
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

	public static ObservableList<User> getUsers() {
		return users.get();
	}

	public static ObservableList<Chat> getUserChats() {
		Set<Chat> readChats = Connection.readAllConversations(localUser.get().getUserID());
		readChats.forEach(chat -> {
			if (!userChats.contains(chat)) {
				userChats.add(chat);
			}
		});
		return userChats.get();
	}

	public static StringProperty getLocalUserName() {
		return new SimpleStringProperty(localUser.get().getUserName());
	}

	public static String getLocalUserID() {
		return localUser.get() == null ? null : localUser.get().getUserID();
	}
	/**
	 * this method is for testing purposes ONLY delete when manager gets properly implemented
	 */
	private static void startServer() {
		Executors.newSingleThreadExecutor().execute(new Server());
	}

	private static void initialize() {
		System.out.print("Enter your nickname (@Nickname): ");
		String userName = Terminal.getUserInput();
		localUser = new SimpleObjectProperty<>();
		localUser.setValue(Login.loginUser(userName));
		ObservableList<Chat> chatSet = FXCollections.observableArrayList();
		userChats = new SimpleListProperty<>(chatSet);
		ObservableList<User> userSet = FXCollections.observableArrayList();
		users = new SimpleListProperty<>(userSet);
	}

	private static Chat resolveChatID(String chatID) {
		if (chatID == null) throw new IllegalStateException("chat ID cannot be null");
		for (Chat c : userChats) {
			if (c.getID().get().equals(chatID)) return c;
		}
		return null;
	}
}