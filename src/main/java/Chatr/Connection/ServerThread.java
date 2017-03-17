package Chatr.Connection;

import Chatr.Message;
import Chatr.Terminal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by max on 17.03.17.
 */
public class ServerThread extends Thread {
	private Socket socket;

	protected ServerThread(Socket socket) {
		super("ServerTread");
		this.socket = socket;
	}

	@Override
	public void run() {
		try (
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
		) {
			System.out.println("connected to: " + socket.getRemoteSocketAddress());
			// the server has to always talk first
			out.println(JSONConverter.toJSON(new Message()));
			socket.shutdownOutput();

			String input;
			while ((input = in.readLine()) != null) {
				System.out.println(input);
				Message m = JSONConverter.fromJSON(input, Message.class);
				if (m != null) {
					Terminal.display(m);
				}
			}
			socket.shutdownInput();

			System.out.println("disconnected from: " + socket.getRemoteSocketAddress());
		} catch (IOException e) {
			System.err.println("ERROR");
			e.printStackTrace();
		}
	}

}
