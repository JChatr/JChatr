package Chatr.View.ChatList.ChatCell;

import Chatr.Model.Chat;
import javafx.scene.control.ListCell;

import java.util.ArrayList;

public class ChatCell extends ListCell<Chat> {
	private ChatCellController cp = new ChatCellController();

	@Override
	protected void updateItem(Chat item, boolean empty) {
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
