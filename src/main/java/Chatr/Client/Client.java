package Chatr.Client;

import Chatr.Helper.CONFIG;
import Chatr.Helper.Enums.Crud;
import Chatr.Helper.Enums.Request;
import Chatr.Helper.JSONTransformer;
import Chatr.Server.Transmission;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Connects to the server and sends / receives Requests
 */
class Client implements Runnable {
	private URL url;
	private Socket socket;
	private PrintWriter outStream;
	private BufferedReader inStream;
	private BlockingQueue<Transmission> queue;
	private int connectionRetries;
	private boolean shutdown;

	Client() {
		this.queue = new LinkedBlockingQueue<>();
		try {
			this.url = new URL(CONFIG.SERVER_ADDRESS);
			this.socket = new Socket(url.getHost(), url.getPort());
			outStream = new PrintWriter(socket.getOutputStream(), true);
			inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			socket.setKeepAlive(true);
			socket.setTcpNoDelay(true);
			socket.setSoTimeout(500);

		} catch (IOException e) {
			e.printStackTrace();
		}
		startThread();
//		sendHeartbeat();
	}

	private void startThread() {
//		Executors.newSingleThreadExecutor().execute(this);
		Thread t = new Thread(this, "Connection Thread " + this.toString());
		t.start();
	}

	/**
	 * POST request to the Server return response
	 *
	 * @return separated lines of the Server's response
	 */
	protected Transmission get(Transmission request) {
		long startTime = System.currentTimeMillis();
		queue.add(request);
		Transmission response = null;
		try {
			Thread.sleep(10);
			response = queue.take();
			if (response == null) {
				reconnect();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			get(request);
		}
		System.out.println(String.format("time: %d ms", (System.currentTimeMillis() - startTime)));
		System.out.println("returned response = " + response);
		return response;
	}


	@Override
	public void run() {
		while (!shutdown) {
			try {
				// Sending head of queue
				outStream.println(JSONTransformer.encode(queue.take()));
				// Receiving response until timeout
				String json;
				try {
					while ((json = inStream.readLine()) != null) {
						queue.add(JSONTransformer.decode(json, Transmission.class));
					}
				} catch (SocketTimeoutException ste) {
					System.err.println("client read timeout");
				}
			} catch (IOException | InterruptedException ioe) {
			}
		}
	}

//	private Transmission filterResponse(Transmission request) {
//		Iterator<Transmission> it = queue.iterator();
//		Transmission tran = null;
//		while (it.hasNext()) {
//			tran = it.next();
//			if (tran.getRequestType() == request.getRequestType() &&
//					tran.getCRUD() == request.getCRUD()) {
//				it.remove();
//				break;
//			}
//		}
//		return tran;
//	}
//
//	private List<Transmission> filterResponses(Transmission request) {
//		List<Transmission> filter = new ArrayList<>();
//		Iterator<Transmission> it = queue.iterator();
//		while (it.hasNext()) {
//			Transmission tran = it.next();
//			if (tran.getRequestType() == request.getRequestType() &&
//					tran.getCRUD() == request.getCRUD()) {
//				filter.add(tran);
//				it.remove();
//			}
//		}
//		return filter;
//	}

	private void sendHeartbeat() {
		Executors.newSingleThreadExecutor().execute(() -> {
			Transmission heartbeat = new Transmission(Request.STATUS, Crud.UPDATE);
			boolean shutdown = false;
			while (!shutdown) {
				heartbeat.setTimestamp(System.currentTimeMillis());
				run();
			}
		});
	}

	private void reconnect() {
		System.err.println("response was null");
	}

	protected void shutdown() {
		this.shutdown = true;
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
