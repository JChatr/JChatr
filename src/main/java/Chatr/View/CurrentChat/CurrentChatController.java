package Chatr.View.CurrentChat;

import Chatr.Controller.Manager;
import Chatr.Converstation.Message;
import Chatr.Helper.CONFIG;
import Chatr.View.UpdateService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class CurrentChatController {
	@FXML
	private ListView<String> currentChat;
	@FXML
	private Label currentChatName;
	@FXML
	private Label currentChatUsers;
	@FXML
	private TextArea textInput;

	@FXML
	private void initialize() {
		textInput.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.ENTER)) {
				onSendButtonClick();
			}
		});
		UpdateService u = new UpdateService();
		currentChat.itemsProperty().bindBidirectional(u.getMessageProperty());
		currentChatName.textProperty().bind(u.getChatNameProperty());
		currentChatUsers.textProperty().bind(u.getUsersProperty());
		u.setPeriod(Duration.millis(CONFIG.CLIENT_PULL_TIMER));
		u.start();
	}

	@FXML
	private void onSendButtonClick() {
		String userInput = textInput.getText();
		if (!userInput.isEmpty()) {
			Message m = Manager.addMessage(userInput);
			displayMessage(m);
			textInput.clear();
		}
	}

	private void displayMessage(Message message) {
		ObservableList<String> list = currentChat.getItems();
		list.add(message.toString());
		currentChat.setItems(list);
	}
}
