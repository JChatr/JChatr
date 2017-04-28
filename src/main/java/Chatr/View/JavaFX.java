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
	private AnchorPane rootLayout;
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
//		showCurrentChat();
	}

	/**
	 * load list fxml and initialize the scene
	 * loads the Bootstrap 3 stylesheets as well
	 */
	private void initializeRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/Chatr/View/ChatList/ChatList.fxml"));
			rootLayout = loader.load();
		} catch (IOException e) {
			log.error("failed to load /fxml/ChatList.fxml", e);
		}
		Scene scene = new Scene(rootLayout);
		scene.getStylesheets().add(JavaFX.class.getResource("/jbootx/bootstrap3.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	private void showCurrentChat() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/Chatr/View/CurrentChat/CurrentChat.fxml"));
			currentChat = loader.load();
		} catch (IOException e) {
			log.error("failed to load /fxml/CurrentChat.fxml", e);
		}
	}
}
