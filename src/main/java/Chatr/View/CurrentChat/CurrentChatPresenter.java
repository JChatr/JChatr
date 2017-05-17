package Chatr.View.CurrentChat;

import Chatr.Controller.Manager;
import Chatr.Converstation.Conversation;
import Chatr.Converstation.Message;
import Chatr.View.CurrentChat.MessageCell.MessageCell;
import Chatr.View.UpdateService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
		addListeners();
		linkUpdateProperties();
		currentChat.setCellFactory(param -> new MessageCell());
	}

	private void addListeners() {
		textInput.textProperty().addListener((obs, oldText, newText) -> {
			if (newText.contains("\n"))
				onSendButtonClick();
		});
	}

	public void reload() {
		reset();
		UpdateService.forceUpdate();
	}

	private void reset() {
		currentChat.itemsProperty().getValue().clear();
	}

	/**
	 * Building Update links from UI properties to Manager methods
	 * all links are guaranteed to get updated at a specified interval
	 */
	private void linkUpdateProperties() {
		UpdateService.linkHighPriority(currentChat.itemsProperty(),
				Manager::getChatUpdates
		);
		UpdateService.linkLowPriority(currentChatName.textProperty(),
				Manager::getChatName
		);
		UpdateService.linkLowPriority(currentChatUsers.textProperty(),
				() -> {
					StringBuilder sb = new StringBuilder();
					Manager.getChatMembers().forEach(memberID -> {
						sb.append(memberID);
						sb.append(", ");
					});
					return sb.toString();
				}
		);
		UpdateService.forceUpdate();
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
			Platform.runLater(() ->
					textInput.clear());
		}
	}

	/**
	 * displays a Message in the List View
	 *
	 * @param message Message to be displayed
	 */
	private void displayMessage(Message message) {
		currentChat.getItems().add(message);
	}
}
