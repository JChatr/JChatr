package Chatr.Server;

import Chatr.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
// Multithreaded server spawns a new Thread for each connection
public class Server implements Runnable {
	DBCache dbCache;
	URL url;

	public Server(URL url) {
		this.url = url;
		this.dbCache = new DBCache<Message>();
	}

	@Override
	public void run() {
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
