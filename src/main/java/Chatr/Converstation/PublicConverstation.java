package Chatr.Converstation;

import java.util.Random;

public class PublicConverstation {
	private String id;

	public PublicConverstation() {
		Random rand = new Random();
		this.id = "" + System.currentTimeMillis() + ":" + rand.nextInt(99999);
	}

	public String getId() {
		return this.id;
	}
}
