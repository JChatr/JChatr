package Chatr.Server;

import Chatr.Helper.CONFIG;
import Chatr.Helper.JSONTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import sun.plugin2.message.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Collection;

/**
 * Multithreaded server, spawns a new Thread for every connection
 */
public class Server extends WebSocketServer {

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
	}

	@Override
	public void onMessage(WebSocket webSocket, String s) {

		Transmission request = JSONTransformer.fromJSON(s, Transmission.class);
		MessageHandler handler = new MessageHandler(request);

		Transmission response= handler.process();

		webSocket.send(JSONTransformer.toJSON(response));

	}

	@Override
	public void onError(WebSocket webSocket, Exception e) {
		log.error("Error on Server: "+ e);
	}

	@Override
	public void onStart() {

		log.info("Server started!");

	}

	public static void main(String[] args ) throws InterruptedException, IOException{

		WebSocketImpl.DEBUG= true;

		int port = 3456;

		Server s= new Server(port);
		s.start();

		BufferedReader sysin = new BufferedReader( new InputStreamReader(System.in));
		while(true){
			String in = sysin.readLine();

			if(in.equals("exit")){
				s.stop();
				break;
			}
		}


	}

	void sendToAll(String s){

		Collection<WebSocket> con = connections();
		synchronized ( con ) {
			for( WebSocket c : con ) {
				c.send( s );
			}
		}

	}
}
