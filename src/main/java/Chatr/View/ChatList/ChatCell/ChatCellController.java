package Chatr.View.ChatList.ChatCell;

import Chatr.Converstation.Conversation;
import Chatr.Converstation.Message;
import Chatr.Helper.DateFormatter;
import Chatr.View.Loader;
import Chatr.View.UpdateService;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	private static Logger log = LogManager.getLogger(ChatCellController.class);

	ChatCellController() {
		load(this);
		linkUpdateProperties();
	}

	private void linkUpdateProperties() {
		ObjectProperty<Message> m = new SimpleObjectProperty<>();
		UpdateService.forceUpdate();
	}

	private void setLatestMessage(Message message) {
		latestMessage.textProperty().setValue(message.getContent());
		String time = DateFormatter.convertTimestamp(message.getTime());
		timestamp.textProperty().setValue(time);
	}

	public void setUnreadMessages(int unreadMessages) {
		if (unreadMessages == 0) {
			notification.setVisible(false);
		}
		notification.textProperty().setValue(Integer.toString(unreadMessages));
		this.unreadMessages = unreadMessages;
	}

	public int getUnreadMessages() {
		return unreadMessages;
	}

	public void setInfo(Conversation conversation) {
		resetData();
		title.textProperty().set(conversation.getID());
	}

	private void resetData() {
		title.textProperty().unbind();
		title.textProperty().setValue("");
	}
}
