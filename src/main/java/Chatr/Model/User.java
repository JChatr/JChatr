package Chatr.Model;

import Chatr.Helper.Enums.Status;
import Chatr.Helper.HashGen;
import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
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
	private Image userImage;
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
	public Image getImage(){
		if(userImage == null) {
			String hash = HashGen.hashMD5(email);
			String url = "https://www.gravatar.com/avatar/" + hash + ".jpg?s=40&d=404";
			if (hash.equals("d41d8cd98f00b204e9800998ecf8427e") || !getURLAvailable(url)){
				url = "/icons/default_user.png";
			}
			userImage = new Image(url, 40, 40, true, false, true);
			log.trace("userImage loaded for " + userID);



		}
		return userImage;
	}

	private boolean getURLAvailable(String url){
		boolean available = true;
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("HEAD");
			int responseCode = connection.getResponseCode();
			if (responseCode != 200) {
				available = false;
			}
		} catch (IOException e){
			log.error("Exception in URLAvailable, no Internet" + e);
			available = false;
		}
		return available;
	}
	public String getPicturePath(){
		return ("https://www.gravatar.com/avatar/" + HashGen.hashMD5(email) + ".jpg?s=40&d=404");
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
		return Objects.equals(userID, o.toString());
	}

	@Override
	public int hashCode() {
		return Objects.hash(userID);
	}
}
