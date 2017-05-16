package Chatr.View.ChatList.ChatCell;

import Chatr.Converstation.Conversation;
import Chatr.View.Loader;
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
	private static Logger log = LogManager.getLogger(ChatCellController.class);

	ChatCellController() {
		load(this);
	}

	public void setInfo(Conversation conversation) {
		resetData();

	}

	private void resetData(){

	}
}
