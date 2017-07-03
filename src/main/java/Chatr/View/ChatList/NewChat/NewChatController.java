package Chatr.View.ChatList.NewChat;

import Chatr.Controller.Manager;
import Chatr.Model.Chat;
import Chatr.Model.User;
import Chatr.View.ChatList.NewChat.UserCellLarge.UserCellLarge;
import Chatr.View.ChatList.NewChat.UserCellSmall.UserCellSmallController;
import Chatr.View.Loader;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.sun.javafx.tk.Toolkit;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class NewChatController extends Loader {
	private static Logger log = LogManager.getLogger(NewChatController.class);
	@FXML
	private GridPane parent;
	@FXML
	private GridPane namePanel;
	@FXML
	private VBox usersPanel;
	@FXML
	private JFXButton nextButton;
	@FXML
	private ListView<User> users;
	@FXML
	private FlowPane selectedUsers;
	@FXML
	private Label memberCount;
	@FXML
	private JFXTextField chatNameField;
	private AnchorPane parentNode;

	/**
	 * loads the FXML and injects itself into the passed parent node
	 *
	 * @param parentNode node insert the new Chat view below
	 */
	public NewChatController(AnchorPane parentNode) {
		this.parentNode = parentNode;
		super.load(this);
	}

	@FXML
	private void initialize() {
		linkProperties();
		addListeners();
		// set the panels up in the default configuration
		switchPanels(false);
		users.setCellFactory(param -> new UserCellLarge());
		UsersMultipleSelections();
	}


	private void addListeners() {
		chatNameField.setOnKeyPressed((event) -> {
			if (event.getCode() == KeyCode.ENTER) {
				onCreateButtonClick();
			}
		});
	}

	/**
	 * links display properties to Manager properties to guarantee up to date information
	 */
	private void linkProperties() {
		namePanel.managedProperty().bind(namePanel.visibleProperty());
		usersPanel.managedProperty().bind(usersPanel.visibleProperty());
		Bindings.bindContent(users.itemsProperty().get(), Manager.getUsers().filtered(user ->
				!user.equals(Manager.getLocalUser().get())
		));
	}

	/**
	 * switches between the two panels
	 *
	 * @param forward weather to switch forward or not
	 */
	private void switchPanels(boolean forward) {
		namePanel.setVisible(forward);
		usersPanel.setVisible(!forward);
	}

	/**
	 * allows multiple in the users ListView to be selected
	 */
	private void UsersMultipleSelections() {
		EventHandler<MouseEvent> eventHandler = (event) -> {
			if (!event.isShortcutDown()) {
				Event.fireEvent(event.getTarget(), cloneMouseEvent(event));
				event.consume();
			}
		};
		// allow the selection of multiple items
		users.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		users.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
		users.addEventFilter(MouseEvent.MOUSE_RELEASED, eventHandler);
		// link the selected items to the items in the flow Pane
		users.getSelectionModel().getSelectedItems().addListener((ListChangeListener<User>) c -> {
			selectedUsers.getChildren().clear();
			ObservableList<User> items = users.getSelectionModel().getSelectedItems();
			for (int i = 0; i < items.size(); i++) {
				if (i >= 256) break;
				User item = items.get(i);
				final UserCellSmallController usc = new UserCellSmallController();
				usc.setInfo(item);
				selectedUsers.getChildren().add(usc.getView());
			}
			memberCount.setText(String.format("%d / 256", items.size()));
		});
	}

	/**
	 * clones the given Mouse Event to simulate a pressed modifier key
	 *
	 * @param event input event
	 * @return cloned event
	 */
	private MouseEvent cloneMouseEvent(MouseEvent event) {
		switch (Toolkit.getToolkit().getPlatformShortcutKey()) {
			case SHIFT:
				return new MouseEvent(
						event.getSource(),
						event.getTarget(),
						event.getEventType(),
						event.getX(),
						event.getY(),
						event.getScreenX(),
						event.getScreenY(),
						event.getButton(),
						event.getClickCount(),
						true,
						event.isControlDown(),
						event.isAltDown(),
						event.isMetaDown(),
						event.isPrimaryButtonDown(),
						event.isMiddleButtonDown(),
						event.isSecondaryButtonDown(),
						event.isSynthesized(),
						event.isPopupTrigger(),
						event.isStillSincePress(),
						event.getPickResult()
				);
			case CONTROL:
				return new MouseEvent(
						event.getSource(),
						event.getTarget(),
						event.getEventType(),
						event.getX(),
						event.getY(),
						event.getScreenX(),
						event.getScreenY(),
						event.getButton(),
						event.getClickCount(),
						event.isShiftDown(),
						true,
						event.isAltDown(),
						event.isMetaDown(),
						event.isPrimaryButtonDown(),
						event.isMiddleButtonDown(),
						event.isSecondaryButtonDown(),
						event.isSynthesized(),
						event.isPopupTrigger(),
						event.isStillSincePress(),
						event.getPickResult()
				);
			case ALT:
				return new MouseEvent(
						event.getSource(),
						event.getTarget(),
						event.getEventType(),
						event.getX(),
						event.getY(),
						event.getScreenX(),
						event.getScreenY(),
						event.getButton(),
						event.getClickCount(),
						event.isShiftDown(),
						event.isControlDown(),
						true,
						event.isMetaDown(),
						event.isPrimaryButtonDown(),
						event.isMiddleButtonDown(),
						event.isSecondaryButtonDown(),
						event.isSynthesized(),
						event.isPopupTrigger(),
						event.isStillSincePress(),
						event.getPickResult()
				);
			case META:
				return new MouseEvent(
						event.getSource(),
						event.getTarget(),
						event.getEventType(),
						event.getX(),
						event.getY(),
						event.getScreenX(),
						event.getScreenY(),
						event.getButton(),
						event.getClickCount(),
						event.isShiftDown(),
						event.isControlDown(),
						event.isAltDown(),
						true,
						event.isPrimaryButtonDown(),
						event.isMiddleButtonDown(),
						event.isSecondaryButtonDown(),
						event.isSynthesized(),
						event.isPopupTrigger(),
						event.isStillSincePress(),
						event.getPickResult()
				);
			default: // well return itself then
				return event;
		}
	}

	/**
	 * passes all info to create a chat on to the createChat Method
	 */
	@FXML
	private void onCreateButtonClick() {
		ObservableList<User> users = this.users.getSelectionModel().getSelectedItems();
		createChat(chatNameField.getText(), users);
		onCancelButtonClick();
	}

	/**
	 * closes the chat selection window
	 */
	@FXML
	private void onCancelButtonClick() {
		parentNode.getChildren().remove(parent);
	}

	/**
	 * switches back to the user selection panel
	 */
	@FXML
	private void onBackButtonClick() {
		switchPanels(false);
	}

	/**
	 * switch to name selection if required
	 */
	@FXML
	private void onNextButtonClick() {
		ObservableList<User> users = this.users.getSelectionModel().getSelectedItems();
		if (users.size() == 1) {
			createChat(users.get(0).getUserName(), users);
			onCancelButtonClick();
		} else {
			switchPanels(true);
		}
	}

	/**
	 * add the user defined chat to the data Model
	 *
	 * @param chatName Chat's name
	 * @param users    joined Users
	 * @return if the chat creation was successful
	 */
	private boolean createChat(String chatName, Collection<User> users) {
		Chat chat = Chat.newChat(
				chatName,
				Manager.getLocalUser().get(),
				new ArrayList<>(users));
		return Manager.getUserChats().add(chat);
	}
}
