package Chatr.View.CurrentChat;

import Chatr.Controller.Manager;
import Chatr.Helper.Enums.MessageType;
import Chatr.Helper.GIFLoader;
import Chatr.Helper.GifImage;
import Chatr.Model.Message;
import Chatr.View.CurrentChat.MessageCell.MessageCell;
import Chatr.View.Loader;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CurrentChatController extends Loader {
	private static Logger log = LogManager.getLogger(CurrentChatController.class);
	@FXML
	private ListView<Message> currentMessages;
	@FXML
	private AnchorPane chatHeader;
	@FXML
	private GridPane startMessage;
	@FXML
	private HBox bottomHBox;
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
	private TextField gifText;
	@FXML
	private ScrollPane gifScroll;

	private String chatID;
	private boolean sidebarVisible;
	private GIFLoader gifLoader;
	private int gifOffset = 25;

	/**
	 * init UI links to Manager class and set up event Methods
	 */
	@FXML
	private void initialize() {
		gifLoader = new GIFLoader();
		addListeners();
		currentMessages.setCellFactory(param -> new MessageCell());
		sidebar.setVisible(sidebarVisible);
		bindings();
		chatHeader.setVisible(false);
		currentMessages.setVisible(false);
		bottomHBox.setVisible(false);
		startMessage.setVisible(true);
	}

	/**
	 *
	 */
	private void bindings() {
		chatHeader.managedProperty().bind(chatHeader.visibleProperty());
		currentMessages.managedProperty().bind(currentMessages.visibleProperty());
		startMessage.managedProperty().bind(startMessage.visibleProperty());
		bottomHBox.managedProperty().bind(bottomHBox.visibleProperty());
		gifPane.setVgap(2);
		gifPane.setHgap(2);
		gifPane.setPrefWrapLength(gifPane.getWidth());
	}

	/**
	 *
	 */
	private void addListeners() {
		textInput.setOnKeyPressed((event) -> {
			if (event.getCode() == KeyCode.ENTER) {
				onSendButtonClick();
			}
		});
		// if \n in the text field sendAsync the message
		// if visible property of sidebar is changed update to managed property to match
		sidebar.managedProperty().bind(sidebar.visibleProperty());
		chatBox.widthProperty().addListener((observable, oldValue, newValue) -> {
			if (sidebarVisible && chatBox.getPrefWidth() + 10 > newValue.doubleValue()) {
				sidebar.setVisible(false);
				sidebarVisible = false;
			}
		});
		gifScroll.vvalueProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.doubleValue() > 0.5) {
				gifOffset += 25;
				showGIFs(gifText.getText(), gifOffset, gifOffset - 25);
				log.trace("sent request for more gifs");
			}
		});
	}

	/**
	 * Searches GIFs on giphy and displays them
	 *
	 * @param searchString Search gifs with this string, if empty diaplays trending gifs
	 * @param limit        Limits how many gifs are shown
	 * @param offset       Offset from the giflist
	 */
	private void showGIFs(String searchString, int limit, int offset) {
		ObservableList<GifImage> images = gifLoader.getGIFs(searchString, limit, offset);
		images.addListener((ListChangeListener<GifImage>) c -> {
			c.next();
			c.getAddedSubList().forEach(image -> {
				image.setOnMouseClicked(event ->
						sendGIF(image.getId(), image.getWidth(), image.getHeight()));
				Platform.runLater(() -> gifPane.getChildren().add(image));
			});
		});
	}

	/**
	 * @param chatID
	 */
	public void switchChat(String chatID) {
		reset();
		this.chatID = chatID;
		linkProperties();
	}

	/**
	 * method manages the visibility of GUI elements
	 */
	private void reset() {
		currentMessages.itemsProperty().unbind();
		currentMessages.itemsProperty().getValue().clear();
		currentChatName.textProperty().unbind();
		currentChatName.textProperty().set("");
		currentChatUsers.textProperty().unbind();
		currentChatUsers.textProperty().set("");
		textInput.clear();
		chatID = "";
		chatHeader.setVisible(true);
		currentMessages.setVisible(true);
		bottomHBox.setVisible(true);
		startMessage.setVisible(false);
		gifOffset = 0;
	}

	/**
	 * Building Update links from UI properties to Manager methods
	 * all links are guaranteed to get updated at a specified interval
	 */
	private void linkProperties() {
		Bindings.bindContent(currentMessages.itemsProperty().get(),
				Manager.getChatMessages(chatID));
		currentChatName.textProperty().bind(Manager.getChatName(chatID));
		currentChatUsers.textProperty().bind(Bindings.concat(
				Manager.getChatMembers(chatID)
		));
	}

	/**
	 * Sends the gif to the chat
	 *
	 * @param url Giphy url
	 */
	private void sendGIF(String url, int width, int height) {
		if (width > 500) {
			width = 500;
		}
		Manager.addMessage(url, MessageType.GIF, width, height, null);
		log.trace(String.format("GIF sent with width: %d, height: %d, URL: %s",
				width, height, url));
	}

	/**
	 * Method to be executed when the sendAsync button is clicked
	 */
	@FXML
	private void onSendButtonClick() {
		String userInput = textInput.getText();
		if (!userInput.trim().isEmpty()) {
			Manager.addMessage(userInput, MessageType.TEXT);
		}
		Platform.runLater(() -> textInput.clear());
	}

	/**
	 * Method to be executed when the emoji button is clicked
	 */
	@FXML
	private void onEmojiButtonClick() {
		sidebarVisible = !sidebarVisible;
		sidebar.setVisible(sidebarVisible);
		if (sidebarVisible) {
			showGIFs(gifText.getText(), 25, 0);
		} else {
			gifPane.getChildren().clear();
			gifText.setText("");
			gifScroll.setVvalue(0);
			gifLoader.resetTrending();
		}
		gifOffset = 25;
	}

	/**
	 * Method to be executed when the gif search button is clicked
	 */
	@FXML
	private void onGIFButtonClick() {
		gifPane.getChildren().clear();
		String gifSearch = gifText.getText();
		gifOffset = 25;
		showGIFs(gifSearch, 25, 0);
	}
}
