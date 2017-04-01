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
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Connects to the server and sends / receives Requests
 */
public class Client {
	private URL url;
	private List<Transmission> outBuffer = new LinkedList<>();
	private List<Transmission> inBuffer = new LinkedList<>();

	protected Client() {
		try {
			this.url = new URL(CONFIG.SERVER_ADDRESS);
		} catch (MalformedURLException e) {
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
		connect();
		return filterResponse(request);
	}

	protected List<Transmission> getMultiple(Transmission request) {
		outBuffer.add(request);
		connect();
		return filterResponses(request);
	}

	protected List<Transmission> getNotifications() {
		return inBuffer;
	}

	//

	/**
	 * Protocol:
	 * 1. Send request
	 * 2. read all lines of the response from the server
	 * 3. close connection
	 */
	private void connect() {
		try (
				Socket socket = new Socket(url.getHost(), url.getPort());
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
		) {
			// Sending
			for (Transmission data : outBuffer) {
				String json = JSONTransformer.toJSON(data);
				out.println(json);
			}
			outBuffer.clear();
			socket.shutdownOutput();

			// Receiving
			String json;
			while ((json = in.readLine()) != null) {
				Transmission data = JSONTransformer.fromJSON(json, Transmission.class);
				inBuffer.add(data);
			}
			socket.shutdownInput();
		} catch (IOException e) {
			e.printStackTrace();
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
}
