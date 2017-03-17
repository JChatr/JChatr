package Chatr.Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by max on 17.03.17.
 */
public class Server implements Connection {
	List<String> inBuffer = new ArrayList<>();
	List<String> outBuffer = new ArrayList<>();
	URL url;

	protected Server(URL url) {
		this.url = url;
	}

	@Override
	public void post(String json) {
		outBuffer.add(json);
	}

	@Override
	public List<String> get() {
		start();
		return inBuffer;
	}

	private void start() {
		System.out.println("server running");
		try (ServerSocket serverSocket = new ServerSocket(url.getPort())){
			while(true) {
				new ServerThread(serverSocket.accept()).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
