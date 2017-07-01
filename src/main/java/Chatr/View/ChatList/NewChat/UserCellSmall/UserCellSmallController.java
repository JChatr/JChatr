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

	public UserCellSmallController() {
		load(this);
	}

	@FXML
	private void initialize() {

	}

	public void setInfo(User user) {
		reset();
		img.imageProperty().bind(user.getImage());
		name.setText(user.getID());
	}

	private void reset() {
		img.imageProperty().unbind();
		img.setImage(null);
		name.setText("");
	}
}
