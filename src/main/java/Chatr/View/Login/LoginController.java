package Chatr.View.Login;

import Chatr.Controller.Login;
import Chatr.Model.Exceptions.EmailException;
import Chatr.Model.Exceptions.PasswordException;
import Chatr.Model.Exceptions.UserIDException;
import Chatr.Model.Exceptions.UserNameException;
import Chatr.View.ChatList.ChatListController;
import Chatr.View.Loader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
	private BorderPane parent;
	@FXML
	private AnchorPane aparent;
	@FXML
	private HBox passwordHBox;

	private static Logger log = LogManager.getLogger(LoginController.class);

	@FXML
	private void intitialize() {

	}

	@FXML
	private void onRegisterButtonClick() {
		String userID = this.userId.getText();
		String email = this.eMail.getText();
		String userName = this.username.getText();
		String password = this.password.getText();


		try {
			Login.registerUser(userID, password, userName, email);
			changeScene();
		} catch (UserIDException e) {
			log.error(e);
		} catch (UserNameException e) {
			log.error(e);
		} catch (EmailException e) {
			log.error(e);
		} catch (PasswordException e) {
			log.error(e);
		}
	}

	@FXML
	private void onLoginButtonClick(){
		passwordHBox.setVisible(false);
	}


	private void changeScene() {
		ChatListController clc = new ChatListController();
		aparent.getChildren().clear();
		aparent.getChildren().add(clc.getView());
	}
}