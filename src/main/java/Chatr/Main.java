package Chatr;

import Chatr.Connection.ConnectionManager;

import java.net.URL;

/**
 * Created by max on 17.03.17.
 */
public class Main {
	public static void main(String[] args) throws Exception {
		for (String s : args) System.out.println(s);
		URL url = new URL(args[0]);
		boolean server = false;
		if (args.length > 1 && "server".equals(args[1])) {
			server = true;
		}

		if (server) {
			System.out.printf("Setting up Server at : %s \n\n", url.toURI());
		} else {
			System.out.printf("Connecting to  : %s \n\n", url.toURI());
		}
		ConnectionManager connection = new ConnectionManager(url, server);

		System.out.println("Enter your Username:");
		String userName = server ? "server" : Terminal.getUserInput();

		while (true) {
//			Terminal.close();
			Terminal.display(connection.getNewMessages());
			System.out.println("Enter your Message:");
			String text = Terminal.getUserInput();
			Message message = new Message(userName, "02", text);
			Terminal.display(message);
			connection.postMessage(message);
		}
	}
}
