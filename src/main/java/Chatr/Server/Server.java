package Chatr.Server;

import Chatr.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;

/**
 * Created by max on 17.03.17.
 */
public class Server {
	DBCache dbCache;
	URL url;

	public Server(URL url) {
		this.url = url;
		this.dbCache = new DBCache<Message>();
	}

	public void start() {
		System.out.println("server running");
		try (ServerSocket serverSocket = new ServerSocket(url.getPort())){
			while(true) {
				new ServerThread(serverSocket.accept(), dbCache).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
