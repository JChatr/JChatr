package Chatr.View.CurrentChat;

import Chatr.Controller.Manager;
import Chatr.Model.Message;
import Chatr.View.CurrentChat.MessageCell.MessageCell;
import Chatr.View.Loader;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

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
	private Button emojiButton;
	@FXML
	private Button sendButton;
	@FXML
	private TabPane sidebar;
	@FXML
	private VBox chatBox;

	private String chatID;
	private boolean sidebarVisible;
	private Node test;

	/**
	 * init UI links to Manager class and set up event Methods
	 */
	@FXML
	private void initialize() {
		addListeners();
		currentMessages.setCellFactory(param -> new MessageCell());
		sidebar.setVisible(sidebarVisible);

	}

	private void addListeners() {
		// if \n in the text field send the message
		textInput.textProperty().addListener((obs, oldText, newText) -> {
			if (newText.contains("\n"))
				onSendButtonClick();
		});
		// if visible property of sidebar is changed update to managed property to match
		sidebar.managedProperty().bind(sidebar.visibleProperty());
		chatBox.widthProperty().addListener((observable, oldValue, newValue) -> {
			if (sidebarVisible && chatBox.getPrefWidth() + 10 > newValue.doubleValue()) {
				sidebar.setVisible(false);
				sidebarVisible = false;
			}
		});

	}

	public void switchChat(String chatID) {
		reset();
		this.chatID = chatID;
		linkProperties();
	}

	private void reset() {
		currentMessages.itemsProperty().unbind();
		currentMessages.itemsProperty().getValue().clear();
		currentChatName.textProperty().unbind();
		currentChatName.textProperty().set("");
		currentChatUsers.textProperty().unbind();
		currentChatUsers.textProperty().set("");
		textInput.clear();
		chatID = "";
	}

	/**
	 * Building Update links from UI properties to Manager methods
	 * all links are guaranteed to get updated at a specified interval
	 */
	private void linkProperties() {
		Bindings.bindContent(
				currentMessages.itemsProperty().get(), Manager.getChatMessages(chatID)
		);
		currentChatName.textProperty().bind(Manager.getChatName(chatID));
		currentChatUsers.textProperty().bind(Bindings.concat(
				Manager.getChatMembers(chatID)
		));
	}

	/**
	 * Method to be executed when the send button is clicked
	 */
	@FXML
	private void onSendButtonClick() {
		String userInput = textInput.getText();
		if (!userInput.trim().isEmpty()) {
			Manager.addMessage(userInput);
			Platform.runLater(() -> textInput.clear());
		}
	}

	@FXML
	private void onEmojiButtonClick() {
		sidebarVisible = !sidebarVisible;
		sidebar.setVisible(sidebarVisible);
	}
}
