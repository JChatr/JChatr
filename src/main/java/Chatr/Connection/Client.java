package Chatr.Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Created by max on 17.03.17.
 */
public class Client {
	Socket socket;
	PrintWriter out;
	BufferedReader in;

	protected Client(URL url) throws IOException {
		try {
			this.socket = new Socket(url.getHost(), url.getPort());
			this.out = new PrintWriter(socket.getOutputStream(), true);
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("connected");
		} catch (UnknownHostException e) {
			System.out.println("unknown host");
		}
	}

	protected void post(String json) throws IOException {
		String fromServer;
		while ((fromServer = in.readLine()) != null) {
			System.out.println(fromServer);
			out.println(json);
		}
		close();
		System.out.println("disconnected");
	}

	private void close() throws IOException {
		socket.close();
		out.close();
		in.close();
	}
}
