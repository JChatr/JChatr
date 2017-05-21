package Chatr.Controller;


import Chatr.Client.Connection;
import Chatr.Helper.UpdateService;
import Chatr.Model.Chat;
import Chatr.Model.Message;
import Chatr.Model.User;
import Chatr.Helper.CONFIG;
import Chatr.Helper.Terminal;
import Chatr.Server.Server;
import Chatr.View.JavaFX;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

/**
 * starts the app
 */
public class Manager {
	public static ObjectProperty<User> localUser;
	private static ListProperty<Chat> userChats;
	private static ListProperty<User> users;
	private static ObjectProperty<Chat> currentChat;

	private static Logger log = LogManager.getLogger(Manager.class);

	public static void main(String[] args) {
		startServer();
		initialize();
		Executors.newSingleThreadExecutor().execute(() -> JavaFX.initGUI(args));

		log.info(String.format("Connecting to  : %s", CONFIG.SERVER_ADDRESS));
	}

	public static Message addMessage(String content) {
		return currentChat.get().newMessage(content);
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

	public static void setCurrentChat(Chat chat) {
		currentChat.setValue(chat);
		final String chatID = chat.getID().get();

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
		currentChat = new SimpleObjectProperty<>();
		updateLoop();
	}

	private static void updateLoop() {
		userChats.addListener((ListChangeListener<Chat>) c -> {
			c.next();
			c.getAddedSubList().forEach(chat ->
					UpdateService.schedule(chat.getMessages(), messages -> {
						Long newestMessage = messages.isEmpty() ? 0 :
								messages.get(messages.size() - 1).getTime();
						String chatID = chat.getID().get();
						List<Message> newMessages = Connection.readNewMessages(chatID, newestMessage);
						messages.addAll(newMessages);
						return messages;
					})
			);
		});
		UpdateService.schedule(userChats, userChats -> {
			Set<Chat> readChats = Connection.readAllConversations(localUser.get().getUserID());
			readChats.forEach(readChat -> {
				if (!userChats.contains(readChat)) {
					userChats.add(readChat);
				}
			});
			return userChats;
		});
		UpdateService.schedule(users, users -> {
			Set<User> user = Connection.readUsers();
			user.forEach(readUser -> {
				if (!users.contains(readUser)) {
					users.add(readUser);
				}
			});
			return users;
		});
	}

	private static Chat resolveChatID(String chatID) {
		for (Chat c : userChats) {
			if (c.getID().get().equals(chatID)) return c;
		}
		throw new IllegalStateException("Chat ID could not be resolved");
	}
}