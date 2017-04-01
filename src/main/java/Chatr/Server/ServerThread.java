package Chatr.Server;

import Chatr.Helper.JSONTransformer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Thread of the server handling the connection
 */
public class ServerThread extends Thread {
	private Socket socket;
	private String remote;
	private List<Transmission> inCache;

	/**
	 * Instantiates the ServerThread
	 * @param socket open socket to use for connecting to the client
	 */
	protected ServerThread(Socket socket) {
		super("ServerTread");
		this.socket = socket;
		this.remote = socket.getRemoteSocketAddress().toString();
		this.inCache = new ArrayList<>();
	}

	/**
	 * starts the Thread
	 */
	@Override
	public void run() {
		try (
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
		) {
			// Receiving
			String JSON;
			while ((JSON = in.readLine()) != null) {
				Transmission data = JSONTransformer.fromJSON(JSON, Transmission.class);
				inCache.add(data);
			}
			socket.shutdownInput();
//			System.out.println("request" + inCache);
			// Processing
			// Figure out what messages to send to send to the client
			MessageHandler handler = new MessageHandler(inCache);
			handler.routeRequests();
			List<Transmission> response = handler.getResponse();
			// Sending
			// only send the required messages
			for (Transmission obj : response) {
				String outJSON = JSONTransformer.toJSON(obj);
				out.println(outJSON);
			}
			socket.shutdownOutput();
			socket.close();
		} catch (IOException e) {
			System.err.println("ERROR");
			e.printStackTrace();
		}
	}
}
