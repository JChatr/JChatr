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
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

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
	@FXML
	private Pane background;
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
		final String css = getClass().getResource("MessageCell.css").toExternalForm();
		parent.getStylesheets().add(css);
		addListeners();
	}

	void setInfo(Message message) {
		resetData();
		userName.setText(message.getSender());
		text.setText(message.getContent());
		timestamp.setText(convertTimestamp(message.getTime()));
		boolean align = false;
		if (!Manager.getUserName().contentEquals(message.getSender())) {
			alignLeft();
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
		alignRight();
		textBox.setPrefWidth(MIN_WIDTH);
		textBox.setMaxWidth(MAX_WIDTH);
		parent.setPrefHeight(MIN_HEIGHT);
		parent.setMaxHeight(MAX_HEIGHT);
	}

	private void alignLeft() {
		spacer.toFront();
		userName.toBack();


		background.setId("background-left");
		text.setId("text-left");
		timestamp.setId("text-left");

	}

	private void alignRight() {
		spacer.toBack();
		userName.toFront();
		background.setId("background-right");
		text.setId("text-right");
		timestamp.setId("text-right");
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
	 *
	 * @param value value to clamp
	 * @param min   min output value
	 * @param max   max output value
	 * @return value clamped to the range
	 */
	private double clamp(double value, double min, double max) {
		return Math.min(Math.max(value, min), max);
	}

	private String convertTimestamp(long timestamp) {
		Instant instant = Instant.ofEpochMilli(timestamp);
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, zoneId);
		return String.format("%02d:%02d", zdt.getHour(), zdt.getMinute());
	}
}
