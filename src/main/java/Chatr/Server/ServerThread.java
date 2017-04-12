package Chatr.Server;

import Chatr.Helper.JSONTransformer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Thread of the server handling the connection
 */
public class ServerThread implements Runnable {
	private Socket socket;
	private String remote;
	private BlockingQueue<Transmission> queue;
	private PrintWriter outStream;
	private BufferedReader inStream;
	private boolean shutdown;

	/**
	 * Instantiates the ServerThread
	 *
	 * @param socket open socket to use for connecting to the client
	 */
	ServerThread(Socket socket) {
		this.queue = new LinkedBlockingDeque<>();
		try {
			outStream = new PrintWriter(socket.getOutputStream(), true);
			inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			socket.setKeepAlive(true);
			socket.setSoTimeout(200);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.socket = socket;
	}

	/**
	 * starts the Thread
	 */
	@Override
	public void run() {
		while (!shutdown) {
			try {
				// Receiving
				try {
					String JSON;
					while ((JSON = inStream.readLine()) != null) {
						Transmission data = JSONTransformer.decode(JSON, Transmission.class);
						queue.add(data);
					}
				} catch (SocketTimeoutException ste) {
					System.err.println("server read timeout");
				}
				System.out.println("request  = " + queue);

				// Processing
				MessageHandler handler = new MessageHandler(queue.take());
				List<Transmission> response = handler.process();

				System.out.println("response = " + queue);
				// Sending
				for (Transmission obj : response) {
					String outJSON = JSONTransformer.encode(obj);
					outStream.println(outJSON);
				}
			} catch (IOException | InterruptedException e) {
				System.err.println("ERROR");
				e.printStackTrace();
			}
		}
	}

	private void shutdown() {
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
