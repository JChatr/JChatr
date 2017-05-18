package Chatr.View.CurrentChat.MessageCell;

import Chatr.Controller.Manager;
import Chatr.Model.Message;
import Chatr.Helper.DateFormatter;
import Chatr.View.Loader;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;

/**
 * renders the Message items in the current Chat box
 */
class MessageCellController extends Loader {
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
	@FXML
	private ImageView userThumbnail;
	private static Logger log = LogManager.getLogger(MessageCellController.class);
	private final static int MAX_WIDTH = 600;
	private final static int MIN_WIDTH = 50;
	private final static int MAX_HEIGHT = Integer.MAX_VALUE;
	private final static int MIN_HEIGHT = 40;
	private final static int WIDTH_PADDING = 20;

	MessageCellController() {
		load(this);
		addListeners();
	}

	public void setInfo(Message message) {
		resetData();
		userName.setText(message.getSender());
		text.setText(message.getContent());
		String time = DateFormatter.convertTimestamp(message.getTime());
		timestamp.setText(time);
		if (!Manager.getLocalUserID().contentEquals(message.getSender())) {
			displayUserThumbnail(message.getSender());
			alignLeft(message);
		}
	}

	@Override
	public Parent getView() {
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
		userThumbnail.imageProperty().setValue(null);
		textBox.setPrefWidth(MIN_WIDTH);
		textBox.setMaxWidth(MAX_WIDTH);
		parent.setPrefHeight(MIN_HEIGHT);
		parent.setMaxHeight(MAX_HEIGHT);
	}

	/**
	 * aligns the message to the Right and changes the CSS appropriately
	 */
	private void alignLeft(Message message) {
		spacer.toFront();
		userName.toBack();
		background.setId("background-left");
		text.setId("text-left");
		timestamp.setId("text-left");
	}
	/**
	 * aligns the message to the Left and changes the CSS appropriately
	 */
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

	private void displayUserThumbnail(String sender){
		/* Version Bad
		Image gravatar = new Image(Manager.getUserImagePath(sender));
		userThumbnail.imageProperty().setValue(gravatar);
		*/

		userThumbnail.imageProperty().setValue(SwingFXUtils.toFXImage(Manager.getUserImage(sender), null));
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
}
