package Chatr;

import Chatr.Client.ConnectionManager;
import Chatr.Server.Server;

import java.net.URL;

public class Main {
	public static void main(String[] args) throws Exception {
		// parse command line arguments
		URL url = new URL(args[0]);
		boolean server = false;
		if (args.length > 1 && "server".equals(args[1])) {
			server = true;
		}
		// set up server
		if (server) {
			System.out.printf("Setting up Server at : %s \n\n", url.toURI());
			Server s1 = new Server(url);
			s1.start();
		} else {
			System.out.printf("Connecting to  : %s \n\n", url.toURI());
		}

		ConnectionManager connection = new ConnectionManager(url);
		System.out.println("Enter your Username:");
		String userName = Terminal.getUserInput();
		// app loop
		// if message is getting sent GET set to update
		while (true) {
			Terminal.display(connection.getNewMessages());
			// read message from user and print to Terminal
			String text = Terminal.getUserInput();
			Message message = new Message(userName, "02", text);
			Terminal.display(message);
			connection.postMessage(message);
		}
	}
}
