package Chatr;

/**
 * Created by max on 17.03.17.
 */
public class Connection {

	public Message getNewMessage() {
		return new Message();
	}


	public void postMessage (Message message) {
		Terminal.display(message);
	}
}
