package Chatr.Model;

import Chatr.Helper.Enums.Status;
import Chatr.Helper.HashGen;
import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Class to save and edit user data.
 *
 * @author mk285
 */
public class User {

	private String userName;
	//private String userID = HashGen.getID(false);
	private String userID;
	private BufferedImage userPicture;
	private String email;
	private Status status;
	private String password;
	private static Logger log = LogManager.getLogger(User.class);

	/**
	 * Constructor to create user object with the user name.
	 *
	 * @param userName The name of the user.
	 */
	public User(String userName) {
		this.userName = userName;
		this.userID = userName;
		email = "";
		status = Status.ONLINE;
		password = "";
	}

	public String getPassword() {
		return password;
	}

	public User setPassword(String password) {
		this.password = password;
		return this;
	}


	/**
	 * This method is used to get the user name.
	 *
	 * @return Returns the user name.
	 */
	public String getUserName() {
		return userName;
	}


	public BufferedImage getPicture() {
		if (userPicture == null) {
			String hash = HashGen.hashMD5(email);
			try {
				URL urlPic = new URL("https://www.gravatar.com/avatar/" + hash + ".jpg?s=40&d=404");
				String content = urlPic.openConnection().getContentType();
				//hash.equals Checks if empty string was hashed
				if (content == null || hash.equals("d41d8cd98f00b204e9800998ecf8427e") || content.contains("text")) {
					userPicture = ImageIO.read(getClass().getResource("/icons/default_user.png"));
					log.trace("Local user picture was used for user " + userID);
				} else {
					userPicture = ImageIO.read(new URL("https://www.gravatar.com/avatar/" + hash + ".jpg?s=40&d=404"));
					log.trace("Gravatar user picture was used for user " + userID);
				}

			} catch (IOException e) {
				log.error("Could not pull Gravatar or local picture, " + e);
			}
		}
		return userPicture;
	}

	public String getPicturePath() {
		return ("https://www.gravatar.com/avatar/" + HashGen.hashMD5(email) + ".jpg");
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
	 * This method is used to get the user ID.
	 *
	 * @return Returns the user ID.
	 */
	public String getUserID() {
		return userID;
	}

	public User setEmail(String email) {
		this.email = email.toLowerCase();
		return this;
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
		if (o == null) {
			return false;
		}
		return Objects.equals(userID, o.toString());
	}

	@Override
	public int hashCode() {
		return Objects.hash(userID);
	}
}