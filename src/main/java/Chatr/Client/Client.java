package Chatr.Client;

import Chatr.Controller.HandlerClient;
import Chatr.Controller.HandlerFactoryClient;
import Chatr.Controller.Manager;
import Chatr.Helper.CONFIG;
import Chatr.Helper.Enums.Crud;
import Chatr.Helper.JSONTransformer;
import Chatr.Model.Chat;
import Chatr.Model.Message;
import Chatr.Model.User;
import Chatr.Server.Handler;
import Chatr.Server.Transmission;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import javax.swing.event.EventListenerList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.*;
import java.util.function.Function;

/**
 * Connects to the server and sends / receives Requests
 */
public final class Client {

	private EventListenerList listeners = new EventListenerList();
	private Logger log = LogManager.getLogger(Client.class);
	WebSocketClient socketClient;

	public Client() {
		try {
			WebSocketClient webSocketClient = new WebSocketClient(new URI(CONFIG.SERVER_ADDRESS), new Draft_17()) {
				@Override
				public void onOpen(ServerHandshake serverHandshake) {
					log.info("Client open connection");
				}

				@Override
				public void onMessage(String s) {
					Transmission tin = JSONTransformer.fromJSON(s, Transmission.class);
					log.info("Message received: " + tin.toString());
					switch (tin.getRequestType()) {
						case MESSAGE:
							if (tin.getCRUD() == Crud.READ) {
								for (ConnectionListener l : listeners.getListeners(ConnectionListener.class))
									l.notify(new ConnectionEvent(this, tin));
							} else {
								HandlerClient handlerClient = HandlerFactoryClient.getInstance(tin.getRequestType());
								handlerClient.processClient(tin);
							}
							break;
						case CONNECT:
							for (ConnectionListener l : listeners.getListeners(ConnectionListener.class))
								l.notify(new ConnectionEvent(this, tin));
							break;

						case CONVERSATION: {
							HandlerClient handlerClient = HandlerFactoryClient.getInstance(tin.getRequestType());
							handlerClient.processClient(tin);
						}
						break;
						case USER:
							HandlerClient handlerClient = HandlerFactoryClient.getInstance(tin.getRequestType());
							handlerClient.processClient(tin);
						case LOGIN:
							for (ConnectionListener l : listeners.getListeners(ConnectionListener.class))
								l.notify(new ConnectionEvent(this, tin));
							break;
						case USERS:
							for (ConnectionListener l : listeners.getListeners(ConnectionListener.class))
								l.notify(new ConnectionEvent(this, tin));
							break;
					}
				}

				@Override
				public void onClose(int i, String s, boolean b) {
				}

				@Override
				public void onError(Exception e) {
					log.error("Problem with client: ", e);
				}
			};

			socketClient = webSocketClient;
			socketClient.connect();
		} catch (URISyntaxException e) {
			log.error("Invalid URI: " + CONFIG.SERVER_ADDRESS);
		}
	}

	public void addListener(ConnectionListener listener) {
		listeners.add(ConnectionListener.class, listener);
	}

	public void removeListener(ConnectionListener listener) {
		listeners.remove(ConnectionListener.class, listener);
	}
}
