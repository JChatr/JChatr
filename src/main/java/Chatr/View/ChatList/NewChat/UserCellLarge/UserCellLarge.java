package Chatr.View.ChatList.NewChat.UserCellLarge;

import Chatr.Model.User;
import javafx.scene.control.ListCell;

public class UserCellLarge extends ListCell<User> {
	private UserCellLargeController ucl = new UserCellLargeController();

	@Override
	protected void updateItem(User item, boolean empty) {
		super.updateItem(item, empty);
		if (empty || item == null) {
			setText(null);
			setGraphic(null);
			return;
		}
		ucl.setInfo(item);
		setGraphic(ucl.getView());
	}
}
