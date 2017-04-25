package Chatr.Controller;


import Chatr.Client.Connection;
import Chatr.Converstation.Conversation;
import Chatr.Converstation.Message;
import Chatr.Converstation.User;
import Chatr.Helper.CONFIG;
import Chatr.Helper.Terminal;
import Chatr.Server.Server;
import Chatr.View.JavaFX;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
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
	private static boolean blockOutput = false;
	private static Set<User> users;
	private static Logger log = LogManager.getLogger(Manager.class);

	public static void main(String[] args) {
		startServer();
		initialize();
		Executors.newSingleThreadExecutor().execute(() -> JavaFX.initGUI(args));
//		userInteraction();
		System.out.printf("Connecting to  : %s \n\n", CONFIG.SERVER_ADDRESS);
		int counter = 0;
		while (true) {
			// pull Messages
			for (Conversation c : userChats) {
				List<Message> messages = c.update();
				if (!blockOutput) Terminal.display(messages, c.getID());
				try {
					Thread.sleep(CONFIG.CLIENT_PULL_TIMER);
				} catch (InterruptedException e) {
				}
			}
			counter++;
			if (counter == 10) {
				userChats.addAll(Connection.readAllConversations(localUser.getUserID()));
				users = Connection.readUsers();
				counter = 0;
			}
		}
	}

	public static Message addMessage(String content) {
		return currentChat.newMessage(content);
	}

	public static String getUserName() {
		return localUser.getUserName();
	}

	/**
	 * this method is for testing purposes ONLY delete when manager gets properly implemented
	 */
	private static void startServer() {
		Executors.newSingleThreadExecutor().execute(new Server());
	}

//	private static void userInteraction() {
//		Executors.newSingleThreadExecutor().execute(() -> {
//			while (true) {
//				String userInput = Terminal.getUserInput();
//				if (userInput.toLowerCase().trim().equals("menu")) {
//					menu();
//				} else {
//					Terminal.display(currentChat.newMessage(userInput), currentChat.getID());
//				}
//			}
//		});
//	}

//	private static void menu() {
//		blockOutput = true;
//		boolean exit = false;
//		do {
//			System.out.println("MAIN MENU:");
//			System.out.println("add    : Add a User to the current chat room\n" +
//					"change : Change to another chat room\n" +
//					"*      : continue posting in the current chat room"
//			);
//			switch (Terminal.getUserInput().toLowerCase()) {
//				case "add":
//					exit = addUser();
//					break;
//				case "change":
//					System.out.println("* : back to main menu\n"
//							+ "enter the new chat rooms name: ");
//					exit = changeChat();
//					break;
//				default:
//					exit = true;
//					break;
//			}
//		} while (!exit);
//
//		System.out.println("exiting menu");
//		blockOutput = false;
//
//	}

//	private static boolean addUser() {
//		Terminal.display("* : back to main menu\n"
//				+ "USERS: ");
//		users.forEach(u -> {
//			if (!u.equals(localUser))
//				Terminal.display(String.format("  - NAME: %5.5s | ID: %5.5s", u.getUserName(), u.getUserID()));
//		});
//		String userName = Terminal.getUserInput();
//		if (userName.equals("*")) {
//			return false;
//		} else {
//			for (User user : users) {
//				if (user.getUserName().startsWith(userName)) {
//					currentChat.addMember(user);
//					Terminal.display(String.format("added %5.5s to %5.5s", user.getUserName(), currentChat.getID()));
//					return true;
//				}
//			}
//			Terminal.display("could not find that user, please enter another username:  ");
//			addUser();
//			return true;
//		}
//	}

//	private static boolean changeChat() {
//		Terminal.display("CHATS");
//		userChats.forEach(conversation -> Terminal.display(String.format("  - ID: %5.5s", conversation.getID())));
//		String conversation = Terminal.getUserInput();
//		if (conversation.equals("*")) {
//			return false;
//		} else {
//			for (Conversation c : userChats) {
//				if (c.getID().startsWith(conversation.toLowerCase())) {
//					Terminal.display(String.format("switched to Chat: %5.5s", c.getID()));
//					currentChat = c;
//					return true;
//				}
//			}
//			Terminal.display("could not find that chat, please try again");
//			changeChat();
//			return true;
//		}
//	}

	private static void initialize() {
		System.out.print("Enter your nickname (@Nickname): ");
		String userName = Terminal.getUserInput();
		localUser = Login.loginUser(userName);
		//localUser = new User(userName);
		System.out.print("who do you want to chat with ? : ");
		String otherUser = Terminal.getUserInput();
		Connection.createUser(localUser.getUserID(), localUser);
		currentChat = Conversation.newConversation(new User(otherUser), localUser);
		userChats = new HashSet<>();
		userChats.add(currentChat);
		users = Connection.readUsers();
	}
}

