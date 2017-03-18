package Chatr.Client;

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
	private URL url;
	private List<String> inBuffer = new ArrayList<>();
	private List<String> outBuffer = new ArrayList<>();

	protected Client(URL url) {
		this.url = url;
	}

	@Override
	public void post(String json) {
		clearBuffers();
		outBuffer.add(json);
//		connect();
	}

	@Override
	public List<String> get() {
		connect();
		return inBuffer;
	}

	// There is always guaranteed to be at least one message that will be sent
	private void connect() {
		String remote = "";
		try (
				Socket socket = new Socket(url.getHost(), url.getPort());
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
		) {
			// Sending
			for (String json : outBuffer) {
				out.println(json);
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

	private void clearBuffers() {
		inBuffer.clear();
		if (outBuffer.size() != 0){
			outBuffer.subList(0, outBuffer.size() -1).clear();
		}
	}
}
