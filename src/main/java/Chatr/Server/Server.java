package Chatr.Server;

import Chatr.Helper.CONFIG;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.BindException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;

/**
 * Multithreaded server, spawns a new Thread for every connection
 */
public class Server implements Runnable {
	URL url;
	private Logger log = LogManager.getLogger(Server.class);

	public Server() {
		try {
			this.url = new URL(CONFIG.SERVER_ADDRESS);
		} catch (MalformedURLException e) {
			log.error(e);
		}
	}

	/**
	 * start Server and listen for requests to connect
	 * on connection a new Thread will be started to handle the request
	 * TODO: change the Thread handling to use Executors
	 */
	@Override
	public void run() {
		try (ServerSocket serverSocket = new ServerSocket(url.getPort())) {
			log.info("Server started at: " + url);
			while (true) {
				new ServerThread(serverSocket.accept()).start();
			}
		} catch (BindException e) {
			log.info("Server already started at: " + url);
		} catch (IOException e) {
			log.error(e);
		}
	}
}
