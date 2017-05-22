package Chatr.View;

import Chatr.View.ChatList.ChatListController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
		primaryStage.show();
	}

	/**
	 * load list fxml and initialize the scene
	 * loads the Bootstrap 3 stylesheets as well
	 */
	private void initializeRootLayout() {
		ChatListController chatList = new ChatListController();
		Scene scene = new Scene(chatList.getView());
		final String cssUri = getClass().getResource("/jbootx/bootstrap3.css").toExternalForm();
		scene.getStylesheets().add(cssUri);
		primaryStage.setScene(scene);
	}
}
