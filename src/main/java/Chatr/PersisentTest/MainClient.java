package Chatr.PersisentTest;

import java.util.concurrent.Executors;


public class MainClient {

	public static void main(String[] args) {
		Executors.newSingleThreadExecutor().execute(() -> new ServerAsync());
		try {
			// just to make sure the server has started by the time the client tires to connect
			Thread.sleep(100) ;
		} catch (InterruptedException e) {
		}
		ClientAsync ca = new ClientAsync();
		ca.addListener((observable, oldValue, newValue) -> {
			System.out.println("read from server: " + newValue);
		});
	}
}
