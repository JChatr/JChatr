package Chatr.Server;

import Chatr.Helper.CONFIG;
import Chatr.Helper.JSONTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Multithreaded server, spawns a new Thread for every connection
 */
public class Server extends WebSocketServer {
	private Map<String, InetSocketAddress> socketIDLink = new ConcurrentHashMap<>();
	public static Logger log = LogManager.getLogger(Server.class);

	public Server(int port) throws UnknownHostException {
		super(new InetSocketAddress(CONFIG.SERVER_ADDRESS, port));

	}

	static public void main(String args[]) throws UnknownHostException {
		WebSocketImpl.DEBUG = false;
		final Server server;
		try {
			server = new Server(CONFIG.SERVER_PORT);
			server.start();
		} catch (UnknownHostException e) {
			log.error("failed to getLinks server", e);
			throw e;
		}
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				server.stop();
				log.info("stopped sever");
			} catch (InterruptedException | IOException e) {
				log.error("failed to stop server", e);
			}
		}));
	}

	@Override
	public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
		log.info("New Connection: " + webSocket.getResourceDescriptor());
	}

	@Override
	public void onClose(WebSocket webSocket, int i, String s, boolean b) {
		socketIDLink.entrySet().removeIf(
				entry -> entry.getValue().equals(webSocket.getRemoteSocketAddress()));
		log.info("Connection closed: " + webSocket.getResourceDescriptor());
	}

	@Override
	public void onMessage(WebSocket webSocket, String s) {
		Transmission request = JSONTransformer.fromJSON(s, Transmission.class);
		log.debug("Transmission received: " + request.toString());
		socketIDLink.put(request.getLocalUserID(), webSocket.getRemoteSocketAddress());
		sendToUsers(handleRequest(request));
	}

	@Override
	public void onError(WebSocket webSocket, Exception e) {
		log.error("Error on Server: ", e);
	}

	@Override
	public void onStart() {
		log.info("Server started!");
	}

	/**
	 * sends the list of requests to the users specified within
	 *
	 * @param responses requests to sendAsync
	 */
	private void sendToUsers(Collection<Transmission> responses) {
		responses.forEach(response -> {
			InetSocketAddress socketAddress = socketIDLink.get(response.getLocalUserID());
			Collection<WebSocket> connections = this.connections();
			for (WebSocket ws : connections) {
				if (ws.getRemoteSocketAddress().equals(socketAddress))
					ws.send(JSONTransformer.toJSON(response));
			}
		});
	}

	/**
	 * gets the right Handler for the given request and processes it
	 *
	 * @param request request to process
	 * @return Collection of responses to the request
	 */
	private Collection<Transmission> handleRequest(Transmission request) {
		Handler handler = HandlerFactory.getInstance(request.getRequestType());
		return handler.process(request);
	}
}