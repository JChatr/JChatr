package Chatr.View.Login;

import Chatr.View.ChatList.ChatListController;
import Chatr.View.Loader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginController extends Loader {

	@FXML
	private Button registrerButton;
	@FXML
	private Label userIdLabel;
	@FXML
	private TextField userId;
	@FXML
	private Label eMailLabel;
	@FXML
	private TextField eMail;
	@FXML
	private Label usernameLabel;
	@FXML
	private TextField username;
	@FXML
	private Label passwordLabel;
	@FXML
	private TextField password;
	@FXML
	private AnchorPane parent;
	private static Logger log = LogManager.getLogger(LoginController.class);

	@FXML
	private void onRegisterButtonClick() {
		String userIdInput = userId.getText();
		String eMailInput = eMail.getText();
		String usernameInput = username.getText();
		String passwordInput = password.getText();

		Chatr.Controller.Manager.initialize(userIdInput, eMailInput, usernameInput, passwordInput);
		changeScene();
	}


	private void changeScene() {
		ChatListController clc = new ChatListController();
		parent.getChildren().clear();
		parent.getChildren().add(clc.getView());
	}
}