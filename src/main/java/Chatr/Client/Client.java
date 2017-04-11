package Chatr.Client;

import Chatr.Helper.CONFIG;
import Chatr.Helper.Enums.Crud;
import Chatr.Helper.Enums.Request;
import Chatr.Helper.JSONTransformer;
import Chatr.Server.Transmission;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Executors;

/**
 * Connects to the server and sends / receives Requests
 */
public class Client {
	private URL url;
	private Socket socket;
	private PrintWriter outStream;
	private BufferedReader inStream;
	private List<Transmission> outBuffer = Collections.synchronizedList(new LinkedList<>());
	private List<Transmission> inBuffer = Collections.synchronizedList(new LinkedList<>());
	private int connectionRetries;

	Client() {
		try {
			this.url = new URL(CONFIG.SERVER_ADDRESS);
			this.socket = new Socket(url.getHost(), url.getPort());
			outStream = new PrintWriter(socket.getOutputStream(), true);
			inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			socket.setKeepAlive(true);
//			socket.setSoTimeout(500);

//			sendHeartbeat();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * POST request to the Server return response
	 *
	 * @return separated lines of the Server's response
	 */
	protected Transmission get(Transmission request) {
		outBuffer.add(request);
		send(request);
		return filterResponse(request);
	}

	protected List<Transmission> getMultiple(Transmission request) {
		outBuffer.add(request);
		send(request);
		return filterResponses(request);
	}


	//

	/**
	 * Protocol:
	 * 1. Send request
	 * 2. read all lines of the response from the server
	 * 3. close connection
	 */
	private void send(Transmission data) {
		try {
			// Sending
			outStream.println(JSONTransformer.encode(data));
			// Receiving
			String json;
			while ((json = inStream.readLine()) != null) {
				inBuffer.add(JSONTransformer.decode(json, Transmission.class));
			}
//		} catch (SocketTimeoutException ste) {
//			connectionRetries++;
//			ste.printStackTrace();
//			send(data);
		} catch (IOException ioe) {

		}
	}

	private Transmission filterResponse(Transmission request) {
		Iterator<Transmission> it = inBuffer.iterator();
		Transmission tran = null;
		while (it.hasNext()) {
			tran = it.next();
			if (tran.getRequestType() == request.getRequestType() &&
					tran.getCRUD() == request.getCRUD()) {
				it.remove();
				break;
			}
		}
		return tran;
	}

	private List<Transmission> filterResponses(Transmission request) {
		List<Transmission> filter = new ArrayList<>();
		Iterator<Transmission> it = inBuffer.iterator();
		while (it.hasNext()) {
			Transmission tran = it.next();
			if (tran.getRequestType() == request.getRequestType() &&
					tran.getCRUD() == request.getCRUD()) {
				filter.add(tran);
				it.remove();
			}
		}
		return filter;
	}

	private void sendHeartbeat() {
		Executors.newSingleThreadExecutor().execute(() -> {
			Transmission heartbeat = new Transmission(Request.STATUS, Crud.UPDATE);
			boolean shutdown = false;
			while (!shutdown) {
				heartbeat.setTimestamp(System.currentTimeMillis());
				send(heartbeat);
			}
		});
	}

	protected void shutdown() {
		try {
			inStream.close();
			outStream.close();
			socket.shutdownInput();
			socket.shutdownOutput();
			socket.close();
		} catch (IOException e) {
		}
	}
}
