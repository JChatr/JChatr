package Chatr.Connection;

import Chatr.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by max on 17.03.17.
 */
public class Client implements Connection {
	List<String> inBuffer = new ArrayList<>();
	List<String> outBuffer = new ArrayList<>();
	URL url;

	protected Client(URL url) {
		this.url = url;
	}

	@Override
	public void post(String json) {
		outBuffer.clear();
		outBuffer.add(json);
		connect();
	}

	@Override
	public List<String> get() {
		inBuffer.clear();
		outBuffer.clear();
		connect();
		return inBuffer;
	}

	private void connect() {
		String socketName = "";
		try (
				Socket socket = new Socket(url.getHost(), url.getPort());
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
		) {
			System.out.println("connected to: " + (socketName = socket.getRemoteSocketAddress().toString()));
			String fromServer;
			while ((fromServer = in.readLine()) != null) {
				inBuffer.add(fromServer);
			}
			String defaultMessage = JSONConverter.toJSON(new Message());
			out.println(defaultMessage);
			for (String json : outBuffer) {
				out.println(json);
			}
			socket.shutdownInput();
			socket.shutdownOutput();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("disconnected from: " + socketName);
	}
}
