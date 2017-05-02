package Chatr.View.CurrentChat.MessageCell;

import Chatr.Converstation.Message;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * renders the Message items in the current Chat box
 */
public class MessageCellPresenter {
	@FXML
	private AnchorPane parent;
	@FXML
	private Label userName;
	@FXML
	private Label content;
	@FXML
	private Label timestamp;
	private Message message;

	MessageCellPresenter() {
		FXMLLoader load = new FXMLLoader(getClass().getResource("MessageCell.fxml"));
		load.setController(this);
		try {
			load.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setInfo(Message message) {
		userName.setText(message.getSender());
		content.setText(message.getContent());
		timestamp.setText(Long.toString(message.getTime()));
	}

	AnchorPane getView() {
		return parent;
	}

}
