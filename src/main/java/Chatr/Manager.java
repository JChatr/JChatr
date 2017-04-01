package Chatr;


import Chatr.Client.Connection;
import Chatr.Converstation.Conversation;
import Chatr.Converstation.Message;
import Chatr.Converstation.User;
import Chatr.Helper.CONFIG;
import Chatr.Helper.Terminal;
import Chatr.Server.Server;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * starts the app
 * TODO: NEEDS to be rewritten
 */
public class Manager {
	public static User localUser;
	private static Conversation currentChat;
	private static List<Conversation> userChats;
	private static boolean blockOutput = false;
	private static List<User> users;

	public static void main(String[] args) {
		startServer();
		initialPull();
		currentChat = initialize();
		userInteraction();
		System.out.printf("Connecting to  : %s \n\n", CONFIG.SERVER_ADDRESS);
		while (true) {
			List<Message> messages = currentChat.update();
			if (!blockOutput) {
				Terminal.display(messages);
			}
			try {
				Thread.sleep(CONFIG.CLIENT_PULL_TIMER);
			} catch (InterruptedException e) {
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
				if (userInput.toLowerCase().equals("menu")) {
					menu();
				}
				currentChat.newMessage(userInput);
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
				currentChat.addMember(findUser(Terminal.getUserInput()));
			case "change":
				System.out.println("enter the new chat rooms name: ");
		}
		System.out.println("exiting menu");
		blockOutput = false;
	}

	private static User findUser(final String userName) {
		for (User user : users) {
			if (user.getUserName().equals(userName)) return user;
		}
		System.out.println("could not find that user, please enter another username:  ");
		return findUser(Terminal.getUserInput());
	}

	private static Conversation initialize() {
		System.out.print("Enter your Username: ");
		String userName = Terminal.getUserInput();
		localUser = new User(userName);
		System.out.print("who do you want to chat with ? : ");
		String otherUser = Terminal.getUserInput();
		return Conversation.newConversation(new User(otherUser), localUser);
	}

	private static void initialPull(){
		Executors.newSingleThreadExecutor().execute(() -> {
			userChats = Connection.readAllConversations(localUser.getUserID());
			users = Connection.readUsers();
		});
	}
}

