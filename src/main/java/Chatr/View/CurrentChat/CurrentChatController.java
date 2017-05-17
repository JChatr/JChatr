package Chatr.View.CurrentChat;

import Chatr.Controller.Manager;
import Chatr.Model.Message;
import Chatr.View.CurrentChat.MessageCell.MessageCell;
import Chatr.View.Loader;
import Chatr.View.UpdateService;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CurrentChatController extends Loader {
	@FXML
	private ListView<Message> currentMessages;
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

	private String chatID;

	/**
	 * init UI links to Manager class and set up event Methods
	 */
	@FXML
	private void initialize() {
		addListeners();
		currentMessages.setCellFactory(param -> new MessageCell());
	}

	private void addListeners() {
		textInput.textProperty().addListener((obs, oldText, newText) -> {
			if (newText.contains("\n"))
				onSendButtonClick();
		});
	}

	public void swtich(String chatID) {
		reset();
		this.chatID = chatID;
		linkUpdateProperties();
	}

	private void reset() {
		currentMessages.itemsProperty().unbind();
		currentMessages.itemsProperty().getValue().clear();
		currentChatName.textProperty().unbind();
		currentChatName.textProperty().set("");
		currentChatUsers.textProperty().unbind();
		currentChatUsers.textProperty().set("");
		chatID = "";
	}

	/**
	 * Building Update links from UI properties to Manager methods
	 * all links are guaranteed to get updated at a specified interval
	 */
	private void linkUpdateProperties() {
		Bindings.bindContent(
				currentMessages.itemsProperty().get(), Manager.getChatMessages(chatID)
		);
		currentChatName.textProperty().bind(Manager.getChatName(chatID));
		currentChatUsers.textProperty().bind(Bindings.concat(
				Manager.getChatMembers(chatID)
		));

//		UpdateService.linkLowPriority(currentChatUsers.textProperty(),
//				() -> {
//					StringBuilder sb = new StringBuilder();
//					Manager.getChatMembers().forEach(memberID -> {
//						sb.append(memberID);
//						sb.append(", ");
//					});
//					return sb.toString();
//				}
//		);
	}

	/**
	 * Method to be executed when the send button is clicked
	 */
	@FXML
	private void onSendButtonClick() {
		String userInput = textInput.getText();
		if (!userInput.trim().isEmpty()) {
			Message m = Manager.addMessage(userInput, chatID);
			displayMessage(m);
			Platform.runLater(() -> textInput.clear());
		}
	}

	/**
	 * displays a Message in the List View
	 *
	 * @param message Message to be displayed
	 */
	private void displayMessage(Message message) {
		currentMessages.getItems().add(message);
	}
}
