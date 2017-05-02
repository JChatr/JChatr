package Chatr.View;

import Chatr.View.ChatList.ChatListView;
import Chatr.View.CurrentChat.CurrentChatView;
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
//		initializeRootLayout();
		showCurrentChat();
		primaryStage.show();
	}

	/**
	 * load list fxml and initialize the scene
	 * loads the Bootstrap 3 stylesheets as well
	 */
	private void initializeRootLayout() {
		ChatListView chatList = new ChatListView();
		Scene scene = new Scene(chatList.getView());
		final String cssUri = getClass().getResource("/jbootx/bootstrap3.css").toExternalForm();
		scene.getStylesheets().add(cssUri);
		primaryStage.setScene(scene);
	}

	private void showCurrentChat() {
		CurrentChatView chatList = new CurrentChatView();
		Scene scene = new Scene(chatList.getView());
		final String cssUri = getClass().getResource("/jbootx/bootstrap3.css").toExternalForm();
		scene.getStylesheets().add(cssUri);
		primaryStage.setScene(scene);
	}
}
