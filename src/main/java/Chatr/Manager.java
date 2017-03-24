package Chatr;


import Chatr.Client.Connection;
import Chatr.Converstation.Message;
import Chatr.Helper.Terminal;
import Chatr.Server.Server;

import java.net.URL;

public class Manager {
	public static void main(String[] args) throws Exception {
		// parse command line arguments
//		URL url = new URL(args[0]);
//		boolean server = false;
//		if (args.length > 1 && "server".equals(args[1])) {
//			server = true;
//		}
//		// set up server
//		if (server) {
//			System.out.printf("Setting up Server at : %s \n\n", url.toURI());
//			(new Thread(new Server(url))).start();
//		} else {
//			System.out.printf("Connecting to  : %s \n\n", url.toURI());
//		}
//		System.out.println("Enter your Username:");
//		String userName = Terminal.getUserInput();
//		// start the client as a new Thread and GET every 500 ms
//		Connection connection = new Connection(url);
//		(new Thread(connection)).start();
//		// app loop
//		// if message is getting sent GET set to update
//		while (true) {
//			// read message from user and print to Terminal
//			String text = Terminal.getUserInput();
//			Message message = new Message(userName, "default", text);
//			Terminal.display(message);
//			connection.postMessage(message);
		}
	}
}
