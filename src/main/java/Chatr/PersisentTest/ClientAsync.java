package Chatr.PersisentTest;

import Chatr.Helper.CONFIG;
import Chatr.Helper.JSONTransformer;
import Chatr.Server.Transmission;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Connects to the server and sends / receives Requests
 */
public class ClientAsync {
	private URL url;
	private EventReader reader;
	private Socket socket;

	protected ClientAsync() {
		try {
			this.url = new URL(CONFIG.SERVER_ADDRESS);
		} catch (MalformedURLException e) {
		}
		establish();
	}


	private void establish() {
		try {
			socket = new Socket(url.getHost(), url.getPort());
			socket.setKeepAlive(true);
			socket.setTcpNoDelay(true);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			reader = new EventReader(in);
			Executors.newSingleThreadExecutor().execute(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void addListener(ChangeListener<Transmission> listener) {
		reader.addListener(listener);
	}

	public void stop() {
		reader.stop();
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class EventReader implements Runnable {
		private BufferedReader is;
		private List<ChangeListener<Transmission>> listeners;
		private ObjectProperty<Transmission> property;
		private Transmission oldTransmission;
		private boolean stopped;

		EventReader(BufferedReader is) {
			this.is = is;
			this.listeners = new ArrayList<>();
			this.property = new SimpleObjectProperty<>();
		}

		public void addListener(ChangeListener<Transmission> listener) {
			this.listeners.add(listener);
		}

		public void stop() {
			this.stopped = true;
		}

		@Override
		public void run() {
			while (!stopped) {
				try {
					String json = is.readLine();
					Transmission newTransmission = JSONTransformer.fromJSON(json, Transmission.class);
					property.setValue(newTransmission);
					for (ChangeListener<Transmission> listener : listeners) {
						listener.changed(property, oldTransmission, newTransmission);
					}
					oldTransmission = newTransmission;
				} catch (IOException e) {
				}
			}
		}
	}
}
