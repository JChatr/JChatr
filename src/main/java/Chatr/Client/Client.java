package Chatr.Client;

import Chatr.Helper.CONFIG;
import Chatr.Helper.Enums.Crud;
import Chatr.Helper.Enums.RequestType;
import Chatr.Helper.JSONTransformer;
import Chatr.Server.Transmission;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Connects to the server and sends / receives Requests
 */
public class Client {
	private WebSocketClient socketClient;
	private List<Listener> listeners = new ArrayList<>();
	private static Logger log = LogManager.getLogger(Client.class);

	public Client() {
		try {
			socketClient = new WebSocketClient(new URI(CONFIG.SERVER_ADDRESS), new Draft_17()) {
				@Override
				public void onOpen(ServerHandshake serverHandshake) {
					log.info("Client open connection");
				}

				@Override
				public void onMessage(String s) {
					final Transmission response = JSONTransformer.fromJSON(s, Transmission.class);
					log.debug("Message received: " + response);

					Iterator<Listener> it = listeners.iterator();
					while (it.hasNext()) {
						Listener listener = it.next();
						if (listener.isListenerFor(response.getRequestType(), response.getCRUD())) {
							listener.notify(response);
							it.remove();
							return;
						}
					}
					Handler handler = HandlerFactory.getInstance(response.getRequestType());
					handler.process(response);
				}

				@Override
				public void onClose(int i, String s, boolean b) {
					log.info("Connection to server closed");
				}

				@Override
				public void onError(Exception e) {
					log.error("Problem with client: ", e);
				}

			};
			socketClient.connect();
		} catch (URISyntaxException e) {
			log.error("Invalid URI: " + CONFIG.SERVER_ADDRESS);
		}
	}

	/**
	 * sends the Transmission to the server asynchronously i.e. non blocking
	 *
	 * @param request Transmission to send
	 */
	public void sendAsync(Transmission request) {
		socketClient.send(JSONTransformer.toJSON(request));
	}

	/**
	 * sends the Transmission to the server synchronously i.e. blocking
	 *
	 * @param request Transmission to send
	 * @return response from the server
	 */
	public Transmission send(Transmission request) {
		Listener listener = new Listener(request, Thread.currentThread());
		listeners.add(listener);
		sendAsync(request);
		try {
			Thread.sleep(CONFIG.TIMEOUT);
		} catch (InterruptedException e) {
			return listener.get();
		}
		log.error("request timeout... retrying");
		return send(request);
	}

	/**
	 * Listens for a response of the given type. Notifies the given Thread
	 */
	private class Listener {
		private Thread thread;
		private Crud crud;
		private RequestType type;
		private Transmission response;

		Listener(Transmission request, Thread currentThread) {
			this.thread = currentThread;
			this.crud = request.getCRUD();
			this.type = request.getRequestType();
		}

		/**
		 * check if this listener is for the given request Type
		 *
		 * @param type Request type of the incoming response
		 * @param crud CRUD Type of the incoming response
		 * @return if the Listener is waiting for this response
		 */
		public boolean isListenerFor(RequestType type, Crud crud) {
			return this.type.equals(type) && this.crud.equals(crud);
		}

		/**
		 * notifies the Thread of this listener that a response from the Server is available
		 *
		 * @param response response from the server
		 */
		public void notify(Transmission response) {
			this.response = response;
			thread.interrupt();
		}

		/**
		 * gets the response from the server
		 *
		 * @return Response from the server
		 */
		Transmission get() {
			return response;
		}
	}
}