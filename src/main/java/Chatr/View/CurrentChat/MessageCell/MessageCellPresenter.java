package Chatr.View.CurrentChat.MessageCell;

import Chatr.Controller.Manager;
import Chatr.Converstation.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Collections;

/**
 * renders the Message items in the current Chat box
 */
public class MessageCellPresenter {
	@FXML
	private HBox parent;
	@FXML
	private Label userName;
	@FXML
	private Label content;
	@FXML
	private Label timestamp;
	@FXML
	private Pane spacer;
	private static Logger log = LogManager.getLogger(MessageCellPresenter.class);


	MessageCellPresenter() {
		FXMLLoader load = new FXMLLoader(getClass().getResource("MessageCell.fxml"));
		load.setController(this);
		try {
			load.load();
		} catch (IOException e) {
			log.error("unable to load MessageCell.fxml", e);
		}
	}

	void setInfo(Message message) {
		userName.setText(message.getSender());
		content.setText(message.getContent());
		timestamp.setText(Long.toString(message.getTime()));
		if (!Manager.getUserName().equals(message.getSender())) {
			spacer.toFront();
			userName.toBack();
//			ObservableList<Node> swap = FXCollections.observableArrayList(parent.getChildren());
//			Collections.swap(swap, 0, 2);
//
//			parent.getChildren().setAll(swap);
		}
	}

	Parent getView() {
		return parent;
	}
}
