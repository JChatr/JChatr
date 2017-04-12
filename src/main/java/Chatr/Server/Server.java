package Chatr.Server;

import Chatr.Helper.CONFIG;

import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Multithreaded server, spawns a new Thread for every connection
 */
public class Server implements Runnable {
	URL url;

	public Server() {
		try {
			this.url = new URL(CONFIG.SERVER_ADDRESS);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * start Server and listen for requests to connect
	 * on connection a new Thread will be started to handle the request
	 * TODO: change the Thread handling to use Executors
	 */
	@Override
	public void run() {
		Executor ex = Executors.newFixedThreadPool(10);
		try (ServerSocket serverSocket = new ServerSocket(url.getPort())) {
			System.out.println("Server started at: " + CONFIG.SERVER_ADDRESS);
			while (true) {
				ex.execute(new ServerThread(serverSocket.accept()));
			}
		} catch (Throwable e) {
			System.out.println("Server already started at: " + CONFIG.SERVER_ADDRESS);
		}
	}
}
