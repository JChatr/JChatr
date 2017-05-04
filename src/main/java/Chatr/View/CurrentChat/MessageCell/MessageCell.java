package Chatr.View.CurrentChat.MessageCell;

import Chatr.Converstation.Message;
import javafx.scene.control.ListCell;

/**
 * renders the Message items in the current Chat box
 */
public class MessageCell extends ListCell<Message> {
	private MessageCellPresenter mp;
	@Override
	protected void updateItem(Message item, boolean empty) {
		super.updateItem(item, empty);
		if (empty || item == null) {
			setText(null);
			setGraphic(null);
			return;
		}
//		if (mp == null)

			mp = new MessageCellPresenter();
		mp.setInfo(item);
		setGraphic(mp.getView());
	}
}
