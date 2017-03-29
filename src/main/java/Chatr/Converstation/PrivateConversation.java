package Chatr.Converstation;

import java.util.Random;

public class PrivateConversation {
	private String id;

	public PrivateConversation() {
		Random rand = new Random();
		this.id = "" + System.currentTimeMillis() + ":" + rand.nextInt(99999);
	}

	public String getId() {
		return this.id;
	}
}
