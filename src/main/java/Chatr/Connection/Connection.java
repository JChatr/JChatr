package Chatr.Connection;

import Chatr.Message;
import Chatr.Terminal;

import java.io.IOException;
import java.net.URL;

/**
 * Created by max on 17.03.17.
 */
public class Connection {
	private URL url;
	private boolean server;
	public Connection (URL url, boolean server) {
		this.url = url;
		this.server = server;
		if (server) {
			Server.start(url);
		}
	}

	public Message getNewMessages() {
		return new Message();
	}


	public void postMessage (Message message) {
		String json = JSONConverter.toJSON(message);
		try {
			Client client = new Client(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
