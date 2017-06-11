package Chatr.View.ChatList.NewChat.UserCellLarge;

import Chatr.Model.User;
import Chatr.View.Loader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

class UserCellLargeController extends Loader {

	@FXML
	private Label userName;
	@FXML
	private Label timestamp;
	@FXML
	private ImageView userImage;

	private User user;

	UserCellLargeController() {
		load(this);
	}

	@FXML
	private void initialize() {

	}

	void setInfo(User user) {
		reset();
		this.user = user;
		userName.setText(user.getUserName());
		String time = "online";
		timestamp.setText(time);
	}

	private void reset() {
		this.user = null;
		userName.setText("");
		timestamp.setText("");
	}
}
