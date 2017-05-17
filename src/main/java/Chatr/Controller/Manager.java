package Chatr.Controller;


import Chatr.Client.Connection;
import Chatr.Model.Conversation;
import Chatr.Model.Message;
import Chatr.Model.User;
import Chatr.Helper.CONFIG;
import Chatr.Helper.Terminal;
import Chatr.Server.Server;
import Chatr.View.JavaFX;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

/**
 * starts the app
 * TODO: NEEDS to be rewritten
 */
public class Manager {
	public static User localUser;
	private static Conversation currentChat;
	private static Set<Conversation> userChats;
	private static Set<User> users;
	private static Logger log = LogManager.getLogger(Manager.class);

	public static void main(String[] args) {
		startServer();
		initialize();
		Executors.newSingleThreadExecutor().execute(() -> JavaFX.initGUI(args));
		log.info(String.format("Connecting to  : %s", CONFIG.SERVER_ADDRESS));
	}

	public static Message addMessage(String content) {
		return currentChat.newMessage(content);
	}

	public static String getLocalUserName() {
		return localUser.getUserName();
	}

	public static String getLocalUserID(){
		return localUser.getUserID();
	}

	public static String getUserImagePath(String userID){
		for(Iterator<User> it = users.iterator(); it.hasNext();) {
			User u = it.next();
			if (u.equals(new User(userID))) {
				return u.getPicturePath();
			}
		}
		return localUser.getPicturePath();
	}

	public static BufferedImage getUserImage(String userID){
		for(Iterator<User> it = users.iterator(); it.hasNext();) {
			User u = it.next();
			if (u.equals(new User(userID))) {
				return u.getPicture();
			}
		}
		return localUser.getPicture();
	}

	public static Set<String> getChatMembers() {
		return currentChat.getMemberIDs();
	}

	public static String getChatName() {
		return currentChat.getID();
	}

	public static List<Message> getChatUpdates() {
		return currentChat.update();
	}

	public static Set<Conversation> getUserChats() {
		Set<Conversation> newChats = new HashSet<>();
		Connection.readAllConversations(localUser.getUserID()).forEach(chat -> {
			if (!userChats.contains(chat)) newChats.add(chat);
		});
		userChats.addAll(newChats);
		return newChats;
	}

	public static void setCurrentChat(Conversation current) {
		currentChat = current;
		currentChat.resetSentMessages();
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
		localUser = Login.loginUser(userName);
//		setCurrentChat(selectLatestConversation(localUser));
		userChats = new HashSet<>();
		users = Connection.readUsers();
	}

//	private static Conversation selectLatestConversation(User user) {
//		Set<Conversation> conversations = Connection.readAllConversations(localUser.getUserID());
//		Conversation latest = null;
//		for (Conversation c : conversations) {
//			latest = c;
//			break;
//		}
//		if (latest == null) latest = createConversation();
//		return latest;
//	}
//
//	private static Conversation createConversation() {
//		System.out.print("who do you want to chat with ? : ");
//		String otherUser = Terminal.getUserInput();
//		Connection.createUser(localUser.getUserID(), localUser);
//		return Conversation.newConversation(new User(otherUser), localUser);
//	}
}

