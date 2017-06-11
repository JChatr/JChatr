package Chatr.View.ChatList.NewChat;

import Chatr.Controller.Manager;
import Chatr.Model.User;
import Chatr.View.ChatList.NewChat.UserCellLarge.UserCellLarge;
import Chatr.View.Loader;
import com.jfoenix.controls.JFXButton;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NewChatController extends Loader {
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

	private AnchorPane parentNode;
	private static Logger log = LogManager.getLogger(NewChatController.class);

	public NewChatController(AnchorPane parentNode) {
		this.parentNode = parentNode;
		super.load(this);
	}

	@FXML
	private void initialize() {
		linkProperties();
		// set the panels up in the default configuration
		switchPanels(false);
		users.setCellFactory(param -> new UserCellLarge());
		UsersMultipleSelections();
	}

	public void setParent(AnchorPane parentNode) {
		this.parentNode = parentNode;
	}

	private void linkProperties() {
		namePanel.managedProperty().bind(namePanel.visibleProperty());
		usersPanel.managedProperty().bind(usersPanel.visibleProperty());
		Bindings.bindContent(users.itemsProperty().get(), Manager.getUsers());
	}

	private void switchPanels(boolean forward) {
		usersPanel.setVisible(forward);
		namePanel.setVisible(!forward);
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
			items.forEach(item -> {
				selectedUsers.getChildren().add(new Label(item.toString()));
			});
			memberCount.setText(String.format("%d / 256", items.size()));
		});
	}

	/**
	 * clones the given Mouse Event to simulate a pressed modifier key
	 *
	 * @param event
	 * @return
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

	@FXML
	private void onNextButtonClick() {
		switchPanels(true);
	}

	@FXML
	private void onCancelButtonClick() {
		parentNode.getChildren().remove(parent);
	}

	@FXML
	private void onBackButtonClick() {
		switchPanels(false);
	}

	@FXML
	private void onCreateButtonClick() {
		onCancelButtonClick();
	}
}
