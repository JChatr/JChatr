package Chatr.View;

import Chatr.Controller.Manager;
import Chatr.Converstation.Message;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

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
		currentChatName.setText("with @aMerkel");
		currentChatUsers.setText("@dTrump, @aMerkel");
	}

	@FXML
	private void onSendButtonClick() {
		String userInput = textInput.getText();
		if (!userInput.isEmpty()) {
			Message m = Manager.addMessage(userInput);
			displayMessage(m);
			textInput.setText("");
		}
	}

	private void displayMessage(Message message) {
		ObservableList<String> list = currentChat.getItems();
		list.add(message.toString());
		currentChat.setItems(list);
	}
}
