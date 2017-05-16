package Chatr.View.ChatList.ChatCell;

import Chatr.Converstation.Conversation;
import javafx.scene.control.ListCell;

public class ChatCell extends ListCell<Conversation> {
	private ChatCellController cp = new ChatCellController();

	@Override
	protected void updateItem(Conversation item, boolean empty) {
		super.updateItem(item, empty);
		if (empty || item == null) {
			setText(null);
			setGraphic(null);
			return;
		}
		cp.setInfo(item);
		setGraphic(cp.getView());
	}
}
