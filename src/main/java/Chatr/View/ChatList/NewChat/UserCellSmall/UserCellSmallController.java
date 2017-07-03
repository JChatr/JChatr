package Chatr.View.ChatList.NewChat.UserCellSmall;

import Chatr.Model.User;
import Chatr.View.Loader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class UserCellSmallController extends Loader {

	@FXML
	private ImageView img;
	@FXML
	private Label name;

//	private User user;

	public UserCellSmallController() {
		load(this);
	}

	@FXML
	private void initialize() {

	}

	public void setInfo(User user) {
//		this.user = user;
//      img.setImage(user.getImage().get());
		name.setText(user.getID());
	}

	private void reset() {
		img.setImage(null);
		name.setText("");
	}
}
