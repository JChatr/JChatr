package Chatr.Client;

import Chatr.Converstation.Message;
import Chatr.Helper.JSONTransformer;
import Chatr.Helper.Terminal;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConnectionManager implements Runnable {
	private URL url;
	private Connection connection;

	public ConnectionManager(URL url) {
		this.url = url;
		this.connection = new Client(url);
	}

	// GET messages -> parse -> return as List
	public List<Message> getNewMessages() {
		List<Message> messages = new ArrayList<>();
		List<String> jsons = connection.get();
		for (String json : jsons) {
			if (!json.trim().isEmpty()) {
				messages.add(JSONTransformer.fromJSON(json, Message.class));
			}
		}
		return messages;
	}

	// parse -> POST message
	public void postMessage(Message message) {
		String json = JSONTransformer.toJSON(message);
		connection.post(json);
	}

	@Override
	public void run() {
		while (true) {
			List<Message> messages = getNewMessages();
			if (!messages.isEmpty()) {
				Terminal.display(messages);
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}