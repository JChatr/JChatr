package Chatr;

/**
 * Created by max on 17.03.17.
 */
public class Main {
	public static void main(String[] args) {
		System.out.println("Enter your Username:");
		String userName = Terminal.getUserInput();

		System.out.println("Enter your Message:");
		while (true) {
			String text = Terminal.getUserInput();
			Message message = new Message(userName, "02", text);
			Connection server = new Connection();
			Terminal.display(message);
			server.postMessage(message);
			Terminal.display(server.getNewMessage());
		}
	}
}
