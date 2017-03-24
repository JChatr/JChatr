package Chatr.Server;

import Chatr.Converstation.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
// Multithreaded server spawns a new Thread for each connection
public class Server implements Runnable {
	L1Cache l1Cache;
	URL url;

	public Server(URL url) {
		this.url = url;
		this.l1Cache = new L1Cache<Message>();
	}

	@Override
	public void run() {
		System.out.println("server running");
		try (ServerSocket serverSocket = new ServerSocket(url.getPort())){
			while(true) {
				new ServerThread(serverSocket.accept(), l1Cache).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
