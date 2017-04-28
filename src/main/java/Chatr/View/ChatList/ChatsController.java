package Chatr.View.ChatList;

import Chatr.Controller.Manager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class ChatsController {
	@FXML
	private AnchorPane currentChat;

	@FXML
	private Button newChatButton;

	@FXML
	private Button searchButton;

	@FXML
	private Label userName;

	@FXML
	private ListView chatsList;

	@FXML
	private void initialize() {
		userName.setText(Manager.getUserName());
	}
}
