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
		userName.setText(message.getSender());
		text.setText(message.getContent());
		timestamp.setText(Long.toString(message.getTime()));
		boolean align = false;
		if (!Manager.getUserName().contentEquals(message.getSender())) {
			localUserDisplay();
			align = true;
		}
		log.trace("aligned: " + true + " render Message: " + message.toString());
	}

	Parent getView() {
		return parent;
	}

	private void localUserDisplay() {
		spacer.toFront();
		userName.toBack();
	}

	private void addListeners() {
		text.textProperty().addListener((observable, oldValue, newValue) -> {
			double maxWidth = 600;
			double minWidth = 100;
			double height = 0;
			double width = TextUtils.computeTextWidth(text.getFont(), text.getText(), 0.0D) + 30;
			textBox.setPrefWidth(Math.min(width, maxWidth));
			if (width > maxWidth) {
				width = maxWidth;
				height = TextUtils.computeTextHeight(text.getFont(), text.getText(), width) + 70;
				parent.setPrefHeight(height);
				parent.setMinHeight(height);
			}
			System.out.println("width: " + width + " height: " + height);
		});
	}

	private void resetToDefault() {

	}

	private static class TextUtils {
		static final Text helper;
		static final double DEFAULT_WRAPPING_WIDTH;
		static final double DEFAULT_LINE_SPACING;
		static final String DEFAULT_TEXT;
		static final TextBoundsType DEFAULT_BOUNDS_TYPE;

		static {
			helper = new Text();
			DEFAULT_WRAPPING_WIDTH = helper.getWrappingWidth();
			DEFAULT_LINE_SPACING = helper.getLineSpacing();
			DEFAULT_TEXT = helper.getText();
			DEFAULT_BOUNDS_TYPE = helper.getBoundsType();
		}

		static double computeTextWidth(Font font, String text, double help0) {
			preProcess(font, text);
			double width = Math.min(helper.prefWidth(-1.0D), help0);
			helper.setWrappingWidth((int) Math.ceil(width));
			width = Math.ceil(helper.getLayoutBounds().getWidth());
			postProcess();
			return width;
		}

		static double computeTextHeight(Font font, String text, double help0) {
			preProcess(font, text);
			helper.setWrappingWidth((int) Math.ceil(help0));
			double height = Math.ceil(helper.getLayoutBounds().getHeight());
			postProcess();
			return height;
		}

		private static void preProcess(Font font, String text) {
			helper.setText(text);
			helper.setFont(font);
			helper.setWrappingWidth(0.0D);
			helper.setLineSpacing(0.0D);
		}

		private static void postProcess() {
			helper.setWrappingWidth(DEFAULT_WRAPPING_WIDTH);
			helper.setLineSpacing(DEFAULT_LINE_SPACING);
			helper.setText(DEFAULT_TEXT);
		}

	}
}
