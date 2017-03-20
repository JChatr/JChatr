package Chatr.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread {
	private Socket socket;
	private DBCache dbCache;
	private String remote;
	private List<String> inCache;

	protected ServerThread(Socket socket, DBCache cache) {
		super("ServerTread");
		this.socket = socket;
		this.dbCache = cache;
		this.remote = socket.getRemoteSocketAddress().toString();
		this.inCache = new ArrayList<>();
	}

	@Override
	public void run() {
		try (
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
		) {
			// Receiving
			String fromClient;
			while ((fromClient = in.readLine()) != null) {
				inCache.add(fromClient);
			}
			socket.shutdownInput();
			// Processing
			// Figure out what messages to send to send to the client
			MessageParser parse = new MessageParser(dbCache, inCache);
			List<String> newerMessages = parse.getNewerMessages();
			// Sending
			// only send the required messages
			for (String obj: newerMessages) {
				out.println(obj);
			}
			socket.shutdownOutput();
			socket.close();
		} catch (IOException e) {
			System.err.println("ERROR");
			e.printStackTrace();
		}
	}
}
