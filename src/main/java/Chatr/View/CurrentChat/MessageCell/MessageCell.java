package Chatr.View.CurrentChat.MessageCell;

import Chatr.Converstation.Message;
import javafx.scene.control.ListCell;

/**
 * renders the Message items in the current Chat box
 */
public class MessageCell extends ListCell<Message> {

	@Override
	protected void updateItem(Message item, boolean empty) {
		super.updateItem(item, empty);
		if (empty || item == null) {
			setText(null);
			setGraphic(null);
			return;
		}
		MessageCellPresenter v = new MessageCellPresenter();
		v.setInfo(item);
		setGraphic(v.getView());
	}
}
