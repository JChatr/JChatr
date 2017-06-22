package Chatr.View;

import Chatr.Client.Client;
import Chatr.Client.Connection;
import Chatr.Controller.Manager;
import Chatr.View.ChatList.ChatListController;
import Chatr.View.Login.LoginController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.CleanableThreadContextMap;

import java.lang.reflect.Field;

public class JavaFX extends Application {
	private Stage primaryStage;
	private AnchorPane rootLayout;
	private AnchorPane currentChat;
	private Logger log = LogManager.getLogger(JavaFX.class);

	public static void initGUI(String[] args) {
		launch(JavaFX.class, args);
	}

	@Override
	public void stop() throws Exception {
		System.out.println("stopped");
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Chatr");
		initializeLoginLayout();
		primaryStage.setMinWidth(400);
		primaryStage.setMinHeight(600);
		primaryStage.show();
	}

	/**
	 * Method initializes the Login GUI layout
	 */
	private void initializeLoginLayout() {
		log.info("Initialize Login-Layout");
		LoginController login = new LoginController();
		Scene scene = new Scene(login.getView());
		scene.getStylesheets().addAll(
				getClass().getResource("/jbootx/bootstrap3.css").toExternalForm(),
				getClass().getResource("/css/chatr.css").toExternalForm()
		);
		primaryStage.setScene(scene);
	}
}