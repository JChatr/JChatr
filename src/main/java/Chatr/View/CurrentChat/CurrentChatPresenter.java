package Chatr.View.CurrentChat;

import Chatr.Controller.Manager;
import Chatr.Converstation.Message;
import Chatr.View.CurrentChat.MessageCell.MessageCell;
import Chatr.View.UpdateService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;

public class CurrentChatPresenter {
	@FXML
	private ListView<Message> currentChat;
	@FXML
	private Label currentChatName;
	@FXML
	private Label currentChatUsers;
	@FXML
	private TextArea textInput;
	@FXML
	private Button emoticonButton;
	@FXML
	private Button sendButton;

	/**
	 * init UI links to Manager class and set up event Methods
	 */
	@FXML
	private void initialize() {
		textInput.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.ENTER)) {
				onSendButtonClick();
			}
		});
		currentChat.setCellFactory(messageListView -> new MessageCell());

		// Building Update links from UI properties to Manager methods
		// all links are guaranteed to get updated at a specified interval
		UpdateService.linkHighPriority(currentChat.itemsProperty(), FXCollections.observableArrayList(),
				list -> Manager.getChatUpdates().forEach(message ->
						list.add(message.toString()))
		);
		UpdateService.linkLowPriority(currentChatName.textProperty(), new SimpleStringProperty(),
				string -> string.setValue(Manager.getChatName())
		);
		UpdateService.linkLowPriority(currentChatUsers.textProperty(), new SimpleStringProperty(),
				string -> {
					StringBuilder sb = new StringBuilder();
					Manager.getChatMembers().forEach(memberID -> {
						sb.append(memberID);
						sb.append(", ");
					});
					string.setValue(sb.toString());
				}
		);
	}

	/**
	 * Method to be executed when the send button is clicked
	 */
	@FXML
	private void onSendButtonClick() {
		String userInput = textInput.getText();
		if (!userInput.trim().isEmpty()) {
			Message m = Manager.addMessage(userInput);
			displayMessage(m);
			textInput.clear();
		}
	}

	/**
	 * displays a Message in the List View
	 * @param message Message to be displayed
	 */
	private void displayMessage(Message message) {
		currentChat.getItems().add(message);
	}
}
