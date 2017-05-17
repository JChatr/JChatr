package Chatr.View.Login;

import Chatr.View.JavaFX;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

//TODO implement logging
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

/**
 * Created by maximilianmerz on 12.05.17.
 */
public class LoginPresenter{

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
	private void onRegisterButtonClick(){
		String userIdInput = userId.getText();
		String eMailInput = eMail.getText();
		String usernameInput = username.getText();
		String passwordInput = password.getText();

		Chatr.Controller.Manager.initialize(userIdInput, eMailInput, usernameInput, passwordInput);
		}




}

