package Chatr.View.CurrentChat.MessageCell;

import Chatr.Controller.Manager;
import Chatr.Helper.DateFormatter;
import Chatr.Model.Message;
import Chatr.View.Loader;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

	private final static int MAX_WIDTH = 600;
	private final static int MIN_WIDTH = 50;
	private final static int MAX_HEIGHT = Integer.MAX_VALUE;
	private  static int MIN_HEIGHT = 40;
	private final static int WIDTH_PADDING = 20;

	private static Logger log = LogManager.getLogger(MessageCellController.class);

	MessageCellController() {
		load(this);
		linkProperties();
		addListeners();
	}

	public void setInfo(Message message) {
		resetData();
		userThumbnail.setManaged(false);
		userName.setText(message.getSender());
		timestamp.setText(DateFormatter.convertTimestamp(message.getTime()));
		if (!Manager.getLocalUserID().contentEquals(message.getSender())) {
			userThumbnail.setManaged(true);
			displayUserThumbnail(message.getSender());
			alignLeft();
		}
		text.setText(message.getContent());
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
		userThumbnail.imageProperty().unbind();
		userThumbnail.setImage(null);
		textBox.setPrefWidth(MIN_WIDTH);
		textBox.setMaxWidth(MAX_WIDTH);
		parent.setPrefHeight(MIN_HEIGHT);
		parent.setMaxHeight(MAX_HEIGHT);
	}

	/**
	 * aligns the message to the Right and changes the CSS appropriately
	 */
	private void alignLeft() {
		spacer.toFront();
		userName.setVisible(true);
		userThumbnail.toBack();
		background.setId("background-left");
		text.setId("text-left");
		timestamp.setId("text-left");
		userName.setId("text-left");
		MIN_HEIGHT = 56;
	}

	/**
	 * aligns the message to the Left and changes the CSS appropriately
	 */
	private void alignRight() {
		spacer.toBack();
		userName.setVisible(false);
		background.setId("background-right");
		text.setId("text-right");
		timestamp.setId("text-right");
		userName.setId("text-right");
		MIN_HEIGHT = 37;
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
			textBox.setPrefWidth(width);
			textBox.setMaxWidth(width);
			double height = text.getLayoutBounds().getHeight();
			height = clamp(height, MIN_HEIGHT, MAX_HEIGHT);
			parent.setPrefHeight(height);
		});
	}

	private void linkProperties(){
		userName.managedProperty().bind(userName.visibleProperty());
	}

	private void displayUserThumbnail(String sender) {
		userThumbnail.imageProperty().unbind();
		userThumbnail.setImage(null);
		userThumbnail.imageProperty().bind(Manager.getUserImage(sender));
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
