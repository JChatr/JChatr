package Chatr.Client;

import Chatr.Message;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConnectionManager {
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
				messages.add(JSONConverter.fromJSON(json, Message.class));
			}
		}
		return messages;
	}

	// parse -> POST message
	public void postMessage(Message message) {
		String json = JSONConverter.toJSON(message);
		connection.post(json);
	}
}