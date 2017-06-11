package Chatr.View.ChatList.NewChat;

import Chatr.Controller.Manager;
import Chatr.Model.User;
import Chatr.View.ChatList.NewChat.UserCellLarge.UserCellLarge;
import Chatr.View.Loader;
import com.jfoenix.controls.JFXButton;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

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

	private AnchorPane parentNode;

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
