package Chatr.Client;

import Chatr.Message;
import Chatr.Terminal;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by max on 17.03.17.
 */
public class ConnectionManager {
	private URL url;
	private Connection connection;

	public ConnectionManager(URL url) {
		this.url = url;
		this.connection = new Client(url);
	}

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

	public void postMessage(Message message) {
		String json = JSONConverter.toJSON(message);
		connection.post(json);
	}
}
