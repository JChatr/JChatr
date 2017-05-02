package Chatr.View.ChatList;

import Chatr.Controller.Manager;
import Chatr.View.CurrentChat.CurrentChatView;
import Chatr.View.UpdateService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class ChatListPresenter {
	@FXML
	private Button newChatButton;
	@FXML
	private Button searchButton;
	@FXML
	private Label userName;
	@FXML
	private ListView chatsList;
	@FXML
	private AnchorPane currentChat;

	@FXML
	private void initialize() {
		UpdateService.linkLowPriority(userName.textProperty(), new SimpleStringProperty(),
				stringProperty -> stringProperty.setValue(Manager.getUserName())
		);
		UpdateService.linkHighPriority(chatsList.itemsProperty(), FXCollections.observableArrayList(),
				observableList -> observableList.setAll(Manager.getUserChats())
		);
//		CurrentChatView chatView = new CurrentChatView();
//		Parent view = chatView.getView();
//		currentChat.getChildren().add(view);
	}
}
