package Chatr.Server;

import Chatr.Converstation.Message;
import Chatr.Helper.CONFIG;

public class Initializer {
	public static L1Cache<Message> l1Cache = new L1Cache<>();

	public static void main(String[] args) {
		System.out.printf("Setting up Server at : %s \n\n", CONFIG.SERVER_ADDRESS);
		// not actually starting a new Thread as it is just not needed here
		new Server().run();
	}
}
