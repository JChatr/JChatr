package Chatr.View;


import Chatr.View.Login.LoginController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JavaFX extends Application {
	public static Stage primaryStage;
	private AnchorPane rootLayout;
	private AnchorPane currentChat;
	private Logger log = LogManager.getLogger(JavaFX.class);

	public static void initGUI(String[] args) {
		launch(JavaFX.class, args);
	}

	@Override
	public void stop() throws Exception {
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
				getClass().getResource("/Chatr/View/chatr.css").toExternalForm()
		);
		primaryStage.setScene(scene);
	}
}