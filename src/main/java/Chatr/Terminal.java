package Chatr;

import java.util.Scanner;

/**
 * Created by max on 17.03.17.
 */
public class Terminal {

	private static Scanner scan = new Scanner(System.in);

	public static String getUserInput() {
		try {
			return scan.nextLine();
		} catch (Exception e) {
			e.printStackTrace();
			return getUserInput();
		}
	}

	public static void close() {
		scan.close();
	}

	public static void display(String... lines) {
		for (String line : lines) {
			if (!line.isEmpty()){
				System.out.println(line);
			}
		}
	}

	public static void display(Message message) {
		if (!message.isEmpty()) {
			System.out.printf("START\n%5s : %s\nEND\n", message.getSender(), message.getContent());
		}
	}
}