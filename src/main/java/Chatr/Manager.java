package Chatr;


import Chatr.Client.Connection;
import Chatr.Converstation.Conversation;
import Chatr.Converstation.Message;
import Chatr.Converstation.User;
import Chatr.Helper.CONFIG;
import Chatr.Helper.Terminal;
import Chatr.Server.Server;

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

	public static void main(String[] args) {
		startServer();
		initialize();
		userInteraction();
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

	/**
	 * this method is for testing purposes ONLY delete when manager gets properly implemented
	 */
	private static void startServer() {
		new Thread(new Server()).start();
	}

	private static void userInteraction() {
		Executors.newSingleThreadExecutor().execute(() -> {
			while (true) {
				String userInput = Terminal.getUserInput();
				if (userInput.toLowerCase().trim().equals("menu")) {
					menu();
				} else {
					Terminal.display(currentChat.newMessage(userInput), currentChat.getID());
				}
			}
		});
	}

	private static void menu() {
		blockOutput = true;
		System.out.println("MAIN MENU:");
		System.out.println("add    : Add a User to the current chat room\n" +
				"change : Change to another chat room\n" +
				"*      : continue posting in the current chat room"
		);
		switch (Terminal.getUserInput().toLowerCase()) {
			case "add":
				System.out.println("enter the username you want to invite: ");
				currentChat.addMember(findUser());
				break;
			case "change":
				System.out.println("enter the new chat rooms name: ");
				currentChat = changeChat();
				break;
		}
		System.out.println("exiting menu");
		blockOutput = false;
	}

	private static User findUser() {
		Terminal.display("USERS: ");
		users.forEach(u -> {
			if (!u.equals(localUser))
				Terminal.display(String.format("  - NAME: %5.5s | ID: %5.5s", u.getUserName(), u.getUserID()));
		});
		String userName = Terminal.getUserInput();
		for (User user : users) {
			if (user.getUserName().equals(userName)) {
				Terminal.display(String.format("added %5.5s to %5.5s", user.getUserName(), currentChat.getID()));
				return user;
			}
		}
		Terminal.display("could not find that user, please enter another username:  ");
		return findUser();
	}

	private static Conversation changeChat(){
		Terminal.display("CHATS");
		userChats.forEach(conversation -> Terminal.display(String.format("  - ID: %5.5s", conversation.getID())));
		String conversation = Terminal.getUserInput();
		for (Conversation c: userChats) {
			if (c.getID().startsWith(conversation.toLowerCase())) {
				Terminal.display(String.format("switched to Chat: %5.5s", c.getID()));
				return c;
			}
		}
		Terminal.display("could not find that chat, please try again");
		return changeChat();
	}

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

