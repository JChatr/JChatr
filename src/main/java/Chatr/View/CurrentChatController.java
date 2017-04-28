package Chatr.View;

import Chatr.Controller.Manager;
import Chatr.Converstation.Message;
import Chatr.Helper.CONFIG;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class CurrentChatController {
	@FXML
	private ListView<String> currentChat;
	@FXML
	private Label currentChatName;
	@FXML
	private Label currentChatUsers;
	@FXML
	private TextArea textInput;

	@FXML
	private void initialize() {
		textInput.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.ENTER)) {
				onSendButtonClick();
			}
		});
		UpdateService u = new UpdateService();
		currentChat.itemsProperty().bindBidirectional(u.getMessageProperty());
		currentChatName.textProperty().bind(u.getChatNameProperty());
		currentChatUsers.textProperty().bind(u.getUsersProperty());
		u.setPeriod(Duration.millis(CONFIG.CLIENT_PULL_TIMER));
		u.start();
	}

	@FXML
	private void onSendButtonClick() {
		String userInput = textInput.getText();
		if (!userInput.isEmpty()) {
			Message m = Manager.addMessage(userInput);
			displayMessage(m);
			textInput.clear();
		}
	}

	private void displayMessage(Message message) {
		ObservableList<String> list = currentChat.getItems();
		list.add(message.toString());
		currentChat.setItems(list);
	}

	private class UpdateService extends ScheduledService<Void> {
		private ListProperty<String> property;
		private StringProperty chat = new SimpleStringProperty();
		private StringProperty users = new SimpleStringProperty();


		private UpdateService() {
			property = new SimpleListProperty<>();
			property.setValue(FXCollections.observableArrayList());
		}

		public ListProperty<String> getMessageProperty() {
			return property;
		}

		public StringProperty getChatNameProperty() {
			return chat;
		}

		public StringProperty getUsersProperty() {
			return users;
		}

		@Override
		protected Task<Void> createTask() {
			return new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					chat.setValue(Manager.getChatName());

					StringBuilder sb = new StringBuilder();
					Manager.getChatMembers().forEach(member -> {
						if (sb.length() < 50) {
							sb.append(member);
							sb.append(", ");
						}
					});
					users.setValue(sb.toString());

					Manager.getChatUpdates().forEach(message ->
							property.add(message.toString()));
					return null;
				}
			};
		}
	}
}
