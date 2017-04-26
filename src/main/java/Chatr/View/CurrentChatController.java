package Chatr.View;

import Chatr.Controller.Manager;
import Chatr.Converstation.Message;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;

import java.util.Collection;

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

		textInput.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.ENTER)) {
				onSendButtonClick();
			}
		});
		setChatMembers(Manager.getChatMembers());
		setCurrentChatName(Manager.getChatName());
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

	private void setChatMembers(Collection<String> memberNames) {
		StringBuilder sb = new StringBuilder();
		memberNames.forEach(member -> {
			if (sb.length() < 50) {
				sb.append(member);
				sb.append(", ");
			}
		});
		currentChatUsers.setText(sb.toString());
	}

	private void setCurrentChatName(String chatName) {
		currentChatName.setText(chatName);
	}
}
