package Chatr.View.Login;

import Chatr.Controller.Login;
import Chatr.Controller.Manager;
import Chatr.Model.ErrorMessagesValidation;
import Chatr.Model.Exceptions.*;
import Chatr.Model.User;
import Chatr.View.ChatList.ChatListController;
import Chatr.View.Loader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends Loader implements Initializable{

	@FXML
	private TextField userId;
	@FXML
	private TextField eMail;
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private AnchorPane parent;
	@FXML
	private HBox eMailBox;
	@FXML
	private HBox userNameBox;
	@FXML
	private HBox registerButtonBox;
	@FXML
	private HBox signInLoginButtonBox;
	@FXML
	private Label userIdLabel;
	@FXML
	private Label passwordLabel;
	@FXML
	private Label eMailLabel;
	@FXML
	private Label usernameLabel;

	private static Logger log = LogManager.getLogger(LoginController.class);


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		bindings();
		eMailBox.setVisible(false);
		userNameBox.setVisible(false);
		registerButtonBox.setVisible(false);
		signInLoginButtonBox.setVisible(true);
	}

	/**
	 * Method makes it easier to show GUI elements
	 */
	private void bindings(){
		eMailBox.managedProperty().bind(eMailBox.visibleProperty());
		userNameBox.managedProperty().bind(userNameBox.visibleProperty());
		registerButtonBox.managedProperty().bind(registerButtonBox.visibleProperty());
		signInLoginButtonBox.managedProperty().bind(signInLoginButtonBox.visibleProperty());
	}

	/**
	 * Shows input fields to register
	 */
	@FXML
	private void onSignInButtonClick(){
		log.info(String.format("Sign in Button clicked"));
		eMailBox.setVisible(true);
		userNameBox.setVisible(true);
		registerButtonBox.setVisible(true);
		signInLoginButtonBox.setVisible(false);
		password.clear();
	}

	/**
	 * Method calls methods to register the user if he presses the register-Button in the GUI
	 */
	@FXML
	private void onRegisterButtonClick() {
		log.info(String.format("Register Button clicked"));
		String userID = this.userId.getText();
		String email = this.eMail.getText();
		String userName = this.username.getText();
		String password = this.password.getText();

		userIdLabel.setText("");
		usernameLabel.setText("");
		eMailLabel.setText("");
		passwordLabel.setText("");


		ErrorMessagesValidation errorMessagesValidation = Login.validateUser(userID, email, password, userName);
		boolean existingError = errorMessagesValidation.isErrorexisting();
		String[] errorMessages = errorMessagesValidation.getErrormessages();

		if (existingError == false){
			Login.registerUser(userID, email, userName, password);
			User user = Login.loginUser(userID, password);
			Manager.setLocalUser(user);
			Manager.startUpdateLoop();
			changeScene();
		}else{
			userIdLabel.setText(errorMessages[0]);
			eMailLabel.setText(errorMessages[1]);
			passwordLabel.setText(errorMessages[2]);
			usernameLabel.setText(errorMessages[3]);
		}
	}

	/**
	 * Method calls methods to login the user if he presses the login-Button in the GUI
	 */
	@FXML
	private void onLoginButtonClick(){
		log.info(String.format("Login Button pressed"));
		String userID = this.userId.getText();
		String password = this.password.getText();
		userIdLabel.setText("");
		passwordLabel.setText("");
		try{
			User user = Login.loginUser(userID, password);
			Manager.setLocalUser(user);
			Manager.startUpdateLoop();
			changeScene();
		} catch(UserIDException e){
			userIdLabel.setText(e.getErrorMessage());
			log.error(e);
		} catch(PasswordException e) {
			passwordLabel.setText(e.getErrorMessage());
			log.error(e);
		}
	}

	/**
	 * Method changes the Login-Scene to Chat-Scene
	 */
	private void changeScene() {
		log.info(String.format("Change scene to chat"));
		ChatListController clc = new ChatListController();
		parent.getChildren().clear();
		parent.getChildren().add(clc.getView());
	}


}