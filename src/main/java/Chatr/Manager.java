package Chatr;


import Chatr.Client.Connection;
import Chatr.Converstation.Message;
import Chatr.Helper.CONFIG;
import Chatr.Helper.Terminal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class Manager {
	public static void main(String[] args) throws Exception {
		List<Message> messages = new ArrayList<>();

		System.out.printf("Connecting to  : %s \n\n", CONFIG.SERVER_ADDRESS);
		System.out.println("Enter your Username:");
		String userName = Terminal.getUserInput();
		System.out.println("Enter the chat room you want to connect to:");
		String chatroom = Terminal.getUserInput();
		messages.add(new Message(userName, "02", "DEFAULT SENT CONTENT"));

		// Start the client pulling in a specified interval
		// print messages to the Terminal if there are new ones
		Executors.newSingleThreadExecutor().execute(() -> {
			while (true) {
				if (!messages.isEmpty()) {
					messages.subList(0, messages.size() - 1).clear();
				}
				List<Message> m = Connection.readConversation(chatroom, messages.get(0));
				Terminal.display(m);
				messages.addAll(m);
				try {
					Thread.sleep(CONFIG.CLIENT_PULL_TIMER);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		// app loop
		// if message is set, post to server
		while (true) {
			// read message from user and print to Terminal
			String text = Terminal.getUserInput();
			Message message = new Message(userName, "default", text);
			Terminal.display(message);
			Connection.updateConversation(chatroom, message);
			messages.add(message);
		}
	}
}

