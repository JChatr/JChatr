package Chatr.Model;

import Chatr.Helper.Enums.Status;
import Chatr.Helper.HashGen;
import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Objects;
import java.util.concurrent.Executors;

/**
 * Class to save and edit user data.
 *
 * @author mk285
 */
public class User {
	private String userName;
	private String userID;
	private ObjectProperty<Image> userImage;
	private String email;
	private Status status;
	private String password;
	private static Logger log = LogManager.getLogger(User.class);

	/**
	 * Constructor to create user object with the user name.
	 *
	 * @param userName The name of the user.
	 */
	public User(String userName, String userID, String email, String password) {
		this.userName = userName;
		this.userID = userID;
		this.email = email;
		this.status = Status.ONLINE;
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	/**
	 * This method is used to get the user name.
	 *
	 * @return Returns the user name.
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * This method is used to set the user name.
	 *
	 * @param newName The new user name.
	 */
	public User setUserName(String newName) {
		userName = newName;
		return this;
	}

	/**
	 * This method is used to load the user image from gravatar.
	 *
	 * @return Returns user image if cached. If not, tries to load a gravatar image or returns a default image.
	 */
	public ObjectProperty<Image> getImage() {
		if (userImage != null) return userImage;
		log.trace("UserImage is null");
		userImage = new SimpleObjectProperty<>();
		Executors.newSingleThreadExecutor().execute(() -> {
			String hash = HashGen.hashMD5(email);
			String url = "https://www.gravatar.com/avatar/" + hash + ".jpg?s=40&d=404";
			if (hash.equals("d41d8cd98f00b204e9800998ecf8427e")) {
				url = "/icons/default_user.png";
				log.trace("Email hash is empty. URL = default image");
			}
			Image img = new Image(url, 40, 40, true, false, false);
			log.trace("Trying to load image from gravatar!");
			if (img.isError()) {
				log.debug("Error in image. Loading default image.");
				img = new Image("/icons/default_user.png", 40, 40, true, false, false);
			}
			userImage.set(img);
		});
		log.trace("userImage loaded for " + userID);
		return userImage;
	}

	public String getEmail() {
		return this.email;
	}

	/**
	 * This method is used to get the user ID.
	 *
	 * @return Returns the user ID.
	 */
	public String getID() {
		return userID;
	}

	public Status getStatus() {
		return status;
	}

	public User setStatus(Status status) {
		this.status = status;
		return this;
	}

	@Override
	public String toString() {
		return userID;
	}

	@Override
	public boolean equals(Object o) {
		return !(o == null || !(o instanceof User)) &&
				Objects.equals(userID, o.toString());
	}

	@Override
	public int hashCode() {
		return Objects.hash(userID);
	}
}