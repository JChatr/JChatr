package Chatr.View.ChatList.ChatCell;

import Chatr.Controller.Manager;
import Chatr.Helper.DateFormatter;
import Chatr.Model.Chat;
import Chatr.Model.Message;
import Chatr.View.Loader;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.function.Function;

class ChatCellController extends Loader {

	@FXML
	private ImageView thumbnail;
	@FXML
	private Pane background;
	@FXML
	private Label title;
	@FXML
	private Label timestamp;
	@FXML
	private Label latestMessage;
	@FXML
	private Label notification;
	@FXML
	private HBox parent;
	private int unreadMessages;
	private Chat chat;
	private static Logger log = LogManager.getLogger(ChatCellController.class);

	ChatCellController() {
		load(this);
	}

	private void linkUpdateProperties() {
		title.textProperty().bind(chat.getName());
		timestamp.textProperty().bind(latestMessageTimeProperty());
		latestMessage.textProperty().bind(latestMessageContentProperty());
		notification.textProperty().bind(unreadMessagesProperty());
		Manager.getCurrentChat().addListener((observable, oldValue, newValue) -> {
			if (chat.equals(newValue)) {
				setUnreadMessages(0);
			}
		});
	}

	private StringProperty latestMessageContentProperty() {
		return latestMessageProperty(message ->
				String.format("%s: %s", message.getSender(), message.getContent()));
	}

	private StringProperty latestMessageTimeProperty() {
		return latestMessageProperty(message -> DateFormatter.convertTimestamp(message.getTime()));
	}

	private StringProperty latestMessageProperty(final Function<Message, String> onChange) {
		ObservableList<Message> messages = chat.getMessages();
		final StringProperty newProperty = new SimpleStringProperty();
		messages.addListener((ListChangeListener<Message>) c -> {
			c.next();
			List<? extends Message> m = c.getAddedSubList();
			Message latest = m.get(m.isEmpty() ? 0 : m.size() - 1);
			newProperty.set(onChange.apply(latest));
		});
		// guarantees that the function is run on the latest message on method call
		// not just when a message gets added
		if (!messages.isEmpty()) {
			int last = messages.size() - 1;
			newProperty.set(onChange.apply(messages.get(last)));
		}
		return newProperty;
	}

	private StringProperty unreadMessagesProperty() {
		final StringProperty newProperty = new SimpleStringProperty();
		chat.getMessages().addListener((ListChangeListener<Message>) c -> {
			c.next();
			if (!chat.equals(Manager.getCurrentChat().get())) {
				unreadMessages += c.getAddedSize();
				newProperty.set(setUnreadMessages(unreadMessages));
			}
		});
		newProperty.set(setUnreadMessages(unreadMessages));
		return newProperty;
	}

	private String setUnreadMessages(int unreadMessages) {
		this.unreadMessages = unreadMessages;
		notification.setVisible(unreadMessages != 0);
		return Integer.toString(unreadMessages);
	}

	public void setInfo(Chat chat) {
		resetData();
		setUnreadMessages(0);
		this.chat = chat;
		linkUpdateProperties();
	}

	private void resetData() {
		title.textProperty().unbind();
		title.textProperty().setValue("");
		timestamp.textProperty().unbind();
		timestamp.textProperty().set("");
		latestMessage.textProperty().unbind();
		latestMessage.textProperty().set("");
		notification.textProperty().unbind();
		notification.textProperty().set("");
		this.chat = null;
	}
}
