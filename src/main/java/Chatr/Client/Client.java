package Chatr.Client;

import Chatr.Helper.CONFIG;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Client {
	private URL url;
	private List<String> inBuffer = new ArrayList<>();
	private List<String> unifiedBuffer = new ArrayList<>();

	protected Client() {
		try {
			this.url = new URL(CONFIG.SERVER_ADDRESS);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * POSTs the request to the Server
	 *
	 * @param request request for the server respond to
	 */
	public void post(String request) {
//		primeBuffers();
		unifiedBuffer.add(request);
		connect();
	}

	/**
	 * POST request to the Server return response
	 *
	 * @return separated lines of the Server's response
	 */
	public List<String> get(String request) {
		post(request);
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
		String remote = "";
		try (
				Socket socket = new Socket(url.getHost(), url.getPort());
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
		) {
			// after writing to the in- / output the connection has to get shutdown
			// Sending
			for (String data : unifiedBuffer) {
				out.println(data);
			}
			socket.shutdownOutput();

			// Receiving
			String fromServer;
			while ((fromServer = in.readLine()) != null) {
				inBuffer.add(fromServer);
			}
			socket.shutdownInput();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * clear inBuffer, always keep the last element from unifiedBuffer
	 * there always has to be the last sent message in the unifiedBuffer
	 */
//	private void primeBuffers() {
//
//		inBuffer.clear();
//		if (!unifiedBuffer.isEmpty()) {
//			unifiedBuffer.subList(0, unifiedBuffer.size() - 1).clear();
//		}
//	}
}
