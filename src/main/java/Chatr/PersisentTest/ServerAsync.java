package Chatr.PersisentTest;

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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

/**
 * Created by max on 26.05.17.
 */
public class ServerAsync {
	private URL url;

	public ServerAsync() {
		try {
			this.url = new URL(CONFIG.SERVER_ADDRESS);
		} catch (MalformedURLException e) {
		}
		start();
	}


	private void start() {
		try (ServerSocket serverSocket = new ServerSocket(url.getPort())) {
			System.out.println("Server started at: " + url);
			while (true) {
				new ServerAsyncThread(serverSocket.accept()).start();
			}
		} catch (IOException e) {
		}
	}

	private class ServerAsyncThread extends Thread {
		private Socket socket;

		ServerAsyncThread(Socket connection) {
			this.socket = connection;
		}

		@Override
		public void run() {
			try (
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
			) {
				System.out.println("Opened Connection with: " + socket.getRemoteSocketAddress());
//				 Receiving
//				String request;
//				while ((request = in.readLine()) != null) {
//					System.out.println("request = " + request);
//				}
				while (!socket.isClosed() && socket.isConnected() && socket.isBound()) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					System.out.println("sending from server...");
					Transmission t = new Transmission(Request.MESSAGE, Crud.READ)
							.setTimestamp(System.currentTimeMillis());
					String json = JSONTransformer.toJSON(t);
					out.println(json);
				}
			} catch (IOException e) {
			}
			System.out.println("socket closed");
		}
	}
}
