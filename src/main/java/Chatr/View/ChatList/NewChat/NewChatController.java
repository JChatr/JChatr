package Chatr.View.ChatList.NewChat;

import Chatr.View.Loader;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
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

	private AnchorPane parentNode;

	public NewChatController(AnchorPane parentNode) {
		this.parentNode = parentNode;
		super.load(this);
	}

	@FXML
	private void initialize() {
		linkProperties();
		usersPanel.setVisible(false);
		namePanel.setVisible(true);
	}

	public void setParent(AnchorPane parentNode) {
		this.parentNode = parentNode;
	}

	private void linkProperties() {
		namePanel.managedProperty().bind(namePanel.visibleProperty());
		usersPanel.managedProperty().bind(usersPanel.visibleProperty());
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
