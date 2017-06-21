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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends Loader implements Initializable {

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
	private VBox registerButtonBox;
	@FXML
	private VBox signInLoginButtonBox;
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
		addListenerLogin();
		bindings();
		eMailBox.setVisible(false);
		userNameBox.setVisible(false);
		registerButtonBox.setVisible(false);
		signInLoginButtonBox.setVisible(true);
		userIdLabel.setVisible(false);
		passwordLabel.setVisible(false);
		eMailLabel.setVisible(false);
		usernameLabel.setVisible(false);
	}

	/**
	 * logs user in if he presses Enter
	 */
	private void addListenerLogin() {
		userId.setOnKeyPressed((event) -> {
			if (event.getCode() == KeyCode.ENTER) {
				onLoginButtonClick();
			}
		});
		password.setOnKeyPressed((event) -> {
			if (event.getCode() == KeyCode.ENTER) {
				onLoginButtonClick();
			}
		});
	}

	/**
	 * registers user if he presses enter
	 */
	private void addListenerRegister() {
		userId.setOnKeyPressed((event) -> {
			if (event.getCode() == KeyCode.ENTER) {
				onRegisterButtonClick();
			}
		});
		password.setOnKeyPressed((event) -> {
			if (event.getCode() == KeyCode.ENTER) {
				onRegisterButtonClick();
			}
		});
		eMail.setOnKeyPressed((event) -> {
			if (event.getCode() == KeyCode.ENTER) {
				onRegisterButtonClick();
			}
		});
		username.setOnKeyPressed((event) -> {
			if (event.getCode() == KeyCode.ENTER) {
				onRegisterButtonClick();
			}
		});
	}

	/**
	 * Method changes GUI from register-window to login-window
	 */
	@FXML
	private void backButtonPressed() {
		eMailBox.setVisible(false);
		userNameBox.setVisible(false);
		registerButtonBox.setVisible(false);
		signInLoginButtonBox.setVisible(true);
		userIdLabel.setVisible(false);
		passwordLabel.setVisible(false);
		eMailLabel.setVisible(false);
		usernameLabel.setVisible(false);
		password.clear();
		addListenerLogin();
	}

	/**
	 * Method makes it easier to show GUI elements
	 */
	private void bindings() {
		eMailBox.managedProperty().bind(eMailBox.visibleProperty());
		userNameBox.managedProperty().bind(userNameBox.visibleProperty());
		registerButtonBox.managedProperty().bind(registerButtonBox.visibleProperty());
		signInLoginButtonBox.managedProperty().bind(signInLoginButtonBox.visibleProperty());
		userIdLabel.managedProperty().bind(userIdLabel.visibleProperty());
		passwordLabel.managedProperty().bind(passwordLabel.visibleProperty());
		eMailLabel.managedProperty().bind(eMailLabel.visibleProperty());
		usernameLabel.managedProperty().bind(usernameLabel.visibleProperty());
	}

	/**
	 * Shows input fields to register
	 */
	@FXML
	private void onSignInButtonClick() {
		log.debug("Sign in Button clicked");
		eMailBox.setVisible(true);
		userNameBox.setVisible(true);
		registerButtonBox.setVisible(true);
		signInLoginButtonBox.setVisible(false);
		password.clear();
		addListenerRegister();
		resetPasswordErrorMessage();
	}

	/**
	 * Method calls methods to register the user if he presses the register-Button in the GUI
	 */
	@FXML
	private void onRegisterButtonClick() {
		log.debug("Register Button clicked");
		String userID = this.userId.getText();
		String email = this.eMail.getText();
		String userName = this.username.getText();
		String password = this.password.getText();

		resetErrorMessages();

		ErrorMessagesValidation errorMessagesValidation = Login.validateUser(userID, email, password, userName);

		if (!errorMessagesValidation.isErrorexisting()) {
			Login.registerUser(userID, email, userName, password);
			User user = Login.loginUser(userID, password);
			Manager.setLocalUser(user);
			//Manager.startUpdateLoop();
			changeScene();
		} else {
			if (errorMessagesValidation.getUserIdErrorMessage() != null) {
				userIdLabel.setVisible(true);
				userIdLabel.setText(errorMessagesValidation.getUserIdErrorMessage());
			}
			if (errorMessagesValidation.getEmailErrorMessage() != null) {
				eMailLabel.setVisible(true);
				eMailLabel.setText(errorMessagesValidation.getEmailErrorMessage());
			}
			if (errorMessagesValidation.getPasswordErrorMessage() != null) {
				passwordLabel.setVisible(true);
				passwordLabel.setText(errorMessagesValidation.getPasswordErrorMessage());
			}
			if (errorMessagesValidation.getUsernameErrorMessage() != null) {
				usernameLabel.setVisible(true);
				usernameLabel.setText(errorMessagesValidation.getUsernameErrorMessage());
			}
		}
	}

	private void resetErrorMessages() {
		userIdLabel.setVisible(false);
		eMailLabel.setVisible(false);
		usernameLabel.setVisible(false);
		userIdLabel.setText("");
		usernameLabel.setText("");
		eMailLabel.setText("");
		resetPasswordErrorMessage();
	}

	private void resetPasswordErrorMessage() {
		passwordLabel.setVisible(false);
		passwordLabel.setText("");
	}

	/**
	 * Method calls methods to login the user if he presses the login-Button in the GUI
	 */
	@FXML
	private void onLoginButtonClick() {
		log.info("Login Button pressed");
		String userID = this.userId.getText();
		String password = this.password.getText();
		resetErrorMessages();
		try {
			User user = Login.loginUser(userID, password);
			Manager.setLocalUser(user);
			Manager.initialPull();
			changeScene();
		} catch (UserIDException e) {
			userIdLabel.setVisible(true);
			userIdLabel.setText(e.getErrorMessage());
			log.error(e);
		} catch (PasswordException e) {
			passwordLabel.setVisible(true);
			passwordLabel.setText(e.getErrorMessage());
			log.error(e);
		}
	}

	/**
	 * Method changes the Login-Scene to Chat-Scene
	 */
	private void changeScene() {
		log.info("Change scene to chat");
		ChatListController clc = new ChatListController();
		parent.getChildren().clear();
		parent.getChildren().add(clc.getView());
	}
}