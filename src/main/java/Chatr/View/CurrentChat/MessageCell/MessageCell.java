package Chatr.View.CurrentChat.MessageCell;

import Chatr.Model.Message;
import javafx.scene.control.ListCell;

/**
 * renders the Message items in the current Chat box
 */
public class MessageCell extends ListCell<Message> {
	private MessageCellController mp = new MessageCellController();

	@Override
	protected void updateItem(Message item, boolean empty) {
		super.updateItem(item, empty);
		if (empty || item == null) {
			setText(null);
			setGraphic(null);
			return;
		}
		mp.setInfo(item);
		setGraphic(mp.getView());
	}
}
