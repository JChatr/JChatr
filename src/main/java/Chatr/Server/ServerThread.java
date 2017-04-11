package Chatr.Server;

import Chatr.Helper.JSONTransformer;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

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
	 *
	 * @param socket open socket to use for connecting to the client
	 */
	protected ServerThread(Socket socket) {
		super("ServerTread");
		this.socket = socket;
		try {
			socket.setKeepAlive(true);
			socket.setSoTimeout(100);
		} catch (IOException e) {
		}

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
				Transmission data = JSONTransformer.decode(JSON, Transmission.class);
				System.out.println(data);
				inCache.add(data);
			}
			System.out.println("request  = " + inCache);

			// Processing
			MessageHandler handler = new MessageHandler(inCache);
			List<Transmission> response = handler.process();
			System.out.println("response = " + response);
			// Sending
			for (Transmission obj : response) {
				String outJSON = JSONTransformer.encode(obj);
				out.println(outJSON);
			}
			socket.close();
		} catch (IOException e) {
			System.err.println("ERROR");
			e.printStackTrace();
		}
	}
}
