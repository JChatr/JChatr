package Chatr.Server;

import Chatr.Controller.Login;
import Chatr.Helper.Enums.Request;
import Chatr.Helper.JSONTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Multithreaded server, spawns a new Thread for every connection
 */
public class Server extends WebSocketServer{

	private Map<String, InetSocketAddress> socketIDLink= new ConcurrentHashMap<>();
	public static Logger log = LogManager.getLogger(Server.class);

	public Server(int port) throws UnknownHostException{

			super(new InetSocketAddress(port));

	}

	@Override
	public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
		log.info("New Connection: "+ webSocket.getResourceDescriptor());
	}

	@Override
	public void onClose(WebSocket webSocket, int i, String s, boolean b) {
		log.info("Connection closed: "+ webSocket.getResourceDescriptor());

		socketIDLink.entrySet().removeIf(
				entry-> entry.getValue().equals(webSocket.getRemoteSocketAddress()));
	}

	@Override
	public void onMessage(WebSocket webSocket, String s) {

		Transmission request = JSONTransformer.fromJSON(s, Transmission.class);
		log.info("Transmission received: "+ request.toString());
		if(request.getRequestType()== Request.CONNECT) {
			socketIDLink.put(request.getLocalUserID(), webSocket.getRemoteSocketAddress());
			handleRequest(request);
		}
		else if(request.getRequestType()==Request.LOGIN){
			HandlerFactory.LoginHandler handler= (HandlerFactory.LoginHandler) HandlerFactory.getInstance(request.getRequestType());
			Transmission response =handler.processTransmission(request);
			webSocket.send(JSONTransformer.toJSON(response));
		}
		else{
			handleRequest(request);
		}

	}

	@Override
	public void onError(WebSocket webSocket, Exception e) {
		log.error("Error on Server: "+ e);
	}

	@Override
	public void onStart() {

		log.info("Server started!");

	}




	static public void main(String args[]){

		try {
			WebSocketImpl.DEBUG = false;

			int port = 3456;

			Server s = new Server(port);
			s.start();

			BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				String in = sysin.readLine();

				if (in.equals("exit")) {
					s.stop();
					break;
				}
			}

		}
		catch(InterruptedException | IOException e){
			log.error(e);
		}
	}

	public void sendToUsers(Collection<Transmission> responses){

		responses.forEach(response ->  {
			InetSocketAddress socketAddress = socketIDLink.get(response.getLocalUserID());
			Collection<WebSocket> cons = this.connections();
			for(WebSocket ws: cons){
				if(ws.getRemoteSocketAddress().equals(socketAddress))
					ws.send(JSONTransformer.toJSON(response));
			}

		});
	}

	private void handleRequest(Transmission request){
		Handler handler = HandlerFactory.getInstance(request.getRequestType());
		Collection<Transmission> response= handler.process(request);
		sendToUsers(response);
	}
}
