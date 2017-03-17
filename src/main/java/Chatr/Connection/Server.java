package Chatr.Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

/**
 * Created by max on 17.03.17.
 */
public class Server {
	public static void start(URL url) {
		try (
			ServerSocket sSocket = new ServerSocket(url.getPort());
			Socket cSocket = sSocket.accept();
			PrintWriter out = new PrintWriter(cSocket.getOutputStream(),true);
			BufferedReader in = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
		) {
			System.out.println("connected");
			String input;
			while ((input = in.readLine()) != null) {
				System.out.println(input);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
