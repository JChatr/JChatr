package Chatr.Client;

import Chatr.Controller.Manager;
import Chatr.Helper.CONFIG;
import Chatr.Helper.JSONTransformer;
import Chatr.Model.Chat;
import Chatr.Model.Message;
import Chatr.Model.User;
import Chatr.Server.Transmission;
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

	private EventListenerList listeners= new EventListenerList();
	private Logger log = LogManager.getLogger(Client.class);
	WebSocketClient socketClient;



	public Client() {

		try {
			WebSocketClient webSocketClient= new WebSocketClient(new URI(CONFIG.SERVER_ADDRESS),new Draft_17()) {
				@Override
				public void onOpen(ServerHandshake serverHandshake) {
					log.info("Client open connection");
				}

				@Override
				public void onMessage(String s) {
					Transmission tin = JSONTransformer.fromJSON(s, Transmission.class);

					switch (tin.getRequestType()) {
						case MESSAGE:
							switch(tin.getCRUD()){
								case CREATE:
									for(ConnectionListener l: listeners.getListeners(ConnectionListener.class))
										l.notify(new ConnectionEvent(this,tin));
									break;
								case READ:
									for(ConnectionListener l: listeners.getListeners(ConnectionListener.class))
										l.notify(new ConnectionEvent(this,tin));
									break;
							}
							break;
						case CONVERSATION:
							switch (tin.getCRUD()) {
								case CREATE:
									//request.setConversationID(ID).setUserIDs((Set<String>) data);
									break;
								case READ:
									for(ConnectionListener l: listeners.getListeners(ConnectionListener.class))
										l.notify(new ConnectionEvent(this,tin));
									break;
								case UPDATE:
									//request.setConversationID(ID).setUserIDs((Set<String>) data);
									break;
								case DELETE:
									//request.setConversationID(ID);
									break;
							}
							break;
						case USER:
								for(ConnectionListener l: listeners.getListeners(ConnectionListener.class))
									l.notify(new ConnectionEvent(this,tin));
							break;
						case USERS:
							for(ConnectionListener l: listeners.getListeners(ConnectionListener.class))
								l.notify(new ConnectionEvent(this,tin));
							break;
					}

				}

				@Override
				public void onClose(int i, String s, boolean b) {

				}

				@Override
				public void onError(Exception e) {
					log.error("Problem with client: "+e);
				}
			};

			socketClient= webSocketClient;
			socketClient.connect();
		}

		catch (URISyntaxException e){
			log.error("Invalid URI: "+ CONFIG.SERVER_ADDRESS);
		}

	}

	public void addListener(ConnectionListener listener){
		listeners.add(ConnectionListener.class, listener);
	}

	public void removeListener(ConnectionListener listener){
		listeners.remove(ConnectionListener.class, listener);
	}

	Transmission get(Transmission t){
		return null;
	}
}
