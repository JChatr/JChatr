package Chatr.View.CurrentChat.MessageCell;

import Chatr.Controller.Manager;
import Chatr.Converstation.Message;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;

/**
 * renders the Message items in the current Chat box
 */
public class MessageCellPresenter {
	@FXML
	private HBox parent;
	@FXML
	private Label userName;
	@FXML
	private Label text;
	@FXML
	private GridPane textBox;
	@FXML
	private Label timestamp;
	@FXML
	private Pane spacer;
	private static Logger log = LogManager.getLogger(MessageCellPresenter.class);
	private final static int MAX_WIDTH = 600;
	private final static int MIN_WIDTH = 50;
	private final static int MAX_HEIGHT = Integer.MAX_VALUE;
	private final static int MIN_HEIGHT = 40;
	private final static int WIDTH_PADDING = 20;

	MessageCellPresenter() {
		FXMLLoader load = new FXMLLoader(getClass().getResource("MessageCell.fxml"));
		load.setController(this);
		try {
			load.load();
		} catch (IOException e) {
			log.error("unable to load MessageCell.fxml", e);
		}
		addListeners();
	}

	void setInfo(Message message) {
		resetData();
		userName.setText(message.getSender());
		text.setText(message.getContent());
		timestamp.setText(Long.toString(message.getTime()));
		boolean align = false;
		if (!Manager.getUserName().contentEquals(message.getSender())) {
			alignRight();
			align = true;
		}
		log.trace("aligned: " + align + " rendered Message: " + message.toString());
	}

	Parent getView() {
		return parent;
	}

	/**
	 * resets all internal data to allow for object reuse
	 */
	private void resetData() {
		userName.setText("");
		text.setText("");
		timestamp.setText("");
		alignLeft();
		textBox.setPrefWidth(MIN_WIDTH);
		textBox.setMaxWidth(MAX_WIDTH);
		parent.setPrefHeight(MIN_HEIGHT);
		parent.setMaxHeight(MAX_HEIGHT);
	}

	private void alignRight() {
		spacer.toFront();
		userName.toBack();
	}

	private void alignLeft() {
		spacer.toBack();
		userName.toFront();
	}

	private void addListeners() {
		// adjusts cell size to match the text in the label
		// gets called when the text is updated
		text.textProperty().addListener((observable, oldValue, newValue) -> {
			Text text = new Text(newValue);
			text.applyCss();
			double width = text.getLayoutBounds().getWidth();
			width += WIDTH_PADDING;
			width = clamp(width, MIN_WIDTH, MAX_WIDTH);
			text.setWrappingWidth(width);
			double height = text.getLayoutBounds().getHeight();
			height = clamp(height, MIN_HEIGHT, MAX_HEIGHT);
			textBox.setPrefWidth(width);
			textBox.setMaxWidth(width);
			parent.setPrefHeight(height);
		});
	}

	/**
	 * clamps a value to within the specified range
	 * @param value value to clamp
	 * @param min min output value
	 * @param max max output value
	 * @return value clamped to the range
	 */
	private double clamp(double value, double min, double max) {
		return Math.min(Math.max(value, min), max);
	}
}
