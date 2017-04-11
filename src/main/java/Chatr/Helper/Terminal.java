package Chatr.Helper;

import Chatr.Converstation.Message;

import java.util.List;
import java.util.Scanner;

public class Terminal {
	private static Scanner scan = new Scanner(System.in);

	public static String getUserInput() {
		try {
			return scan.nextLine().replaceAll("\\r|\\n", "");
		} catch (Exception e) {
//			e.printStackTrace();
			return getUserInput();
		}
	}

	public static void close() {
		scan.close();
	}

	public static void display(String... lines) {
		for (String line : lines) {
			display(line);
		}
	}

	public static void display(String line) {
		if (!line.trim().isEmpty()) {
			System.out.println(line);
		}
	}

	public static void display(List<Message> messages) {
		for (Message message : messages) {
			display(message);
		}
	}

	public static void display(Message message) {
		if (!message.isEmpty()) {
			System.out.printf("Name: %5.5s : %s\n",
					message.getSender(), message.getContent());
		}
	}

	public static void display(List<Message> messages, String conversationID) {
		for (Message message : messages) {
			display(message, conversationID);
		}
	}

	public static void display(Message message, String conversationID) {
		if (!message.isEmpty()) {
			System.out.printf("Chat: %5.5s Name: %5.5s : %s\n",
					conversationID, message.getSender(), message.getContent());
		}
	}
}