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
import Chatr.View.CurrentChat.GIFCell.GIFCellController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import java.util.Set;
import java.util.concurrent.Executors;


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
	@FXML
	private Tab gifTab;
	@FXML
	private Tab emojiTab;
	@FXML
	private Tab stickerTab;
	@FXML
	private FlowPane gifPane;
	@FXML
	private Button gifButton;
	@FXML
	private TextField gifText;


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
		sidebar.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> {
					if(newValue.getId().equals("gifTab")){
						showGIFs("");
					}
				});
	}

	public void showGIFs(String searchstring){
		int limit = 25;
		for (int i = 0; i < limit; i++) {
			gifPane.getChildren().add(new ImageView(GIFCellController.getGIFs(searchstring, i).getValue()));
		}


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
	@FXML
	private void onGIFButtonClick(){
		gifPane.getChildren().clear();
		String gifSearch = gifText.getText();
		showGIFs(gifSearch);
	}

}
