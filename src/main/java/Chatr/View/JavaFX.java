package Chatr.View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class JavaFX extends Application {
	private Stage primaryStage;
	private AnchorPane currentChat;
	private Logger log = LogManager.getLogger();

	public static void initGUI(String[] args) {
		launch(JavaFX.class, args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Chatr");
		initializeRootLayout();
		showCurrentChat();
	}

	private void initializeRootLayout() {

	}

	private void showCurrentChat() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/CurrentChat.fxml"));
			currentChat = loader.load();
			Scene scene = new Scene(currentChat);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			log.error("Java FX failed to initialize", e);
		}
	}
}
