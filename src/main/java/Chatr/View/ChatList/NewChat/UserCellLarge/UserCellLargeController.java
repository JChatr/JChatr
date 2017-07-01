package Chatr.View.ChatList.NewChat.UserCellLarge;

import Chatr.Model.User;
import Chatr.View.Loader;
import com.jfoenix.controls.JFXRippler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

class UserCellLargeController extends Loader {

	@FXML
	private Label userName;
	@FXML
	private Label timestamp;
	@FXML
	private ImageView userImage;
	@FXML
	private JFXRippler bg;
	@FXML
	private GridPane parent;

	private User user;
//	private byte selected;

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
		userName.setId("");
		timestamp.setText("");
	}
}
