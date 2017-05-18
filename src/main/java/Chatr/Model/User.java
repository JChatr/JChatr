package Chatr.Model;

import Chatr.Helper.Enums.Status;
import Chatr.Helper.HashGen;
import javafx.embed.swing.SwingFXUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Class to save and edit user data.
 * @author mk285
 *
 */
public class User {

	private String userName = "";
	private String userID = "";
	public BufferedImage userPicture;
	private String email = "";
	private Status status;
	private static Logger log = LogManager.getLogger(User.class);
	/**
	 * Constructor to create user object with the user name.
	 * @param userName The name of the user.
	 */
	public User(String userName){
		this.userName = userName;
		this.userID = userName;
	}

	/**
	 * This method is used to get the user name.
	 * @return Returns the user name.
	 */
	public String getUserName(){
		return userName;
	}
	


	/**
	 * This method is used to set the user name.
	 * @param newName The new user name.
	 */
	public User setUserName(String newName){
		userName = newName;
		return this;
	}
	
	/**
	 * This method is used to get the user ID.
	 * @return Returns the user ID.
	 */
	public String getUserID(){
		return userID;
	}
	
	public User setEmail(String email){
		this.email = email.toLowerCase();
		return this;
	}
	
	public boolean syntaxEmail(String email){
		if(email.contains(".") && email.contains("@")){
			return true;
		}else{
			return false;
		}
	}

	public Status getStatus() {
		return status;
	}

	public User setStatus(Status status) {
		this.status = status;
		return this;
	}


	public BufferedImage getPicture(){
		if(userPicture == null) {
			String hash = HashGen.hashMD5(email);
			try {
				URL urlPic = new URL("https://www.gravatar.com/avatar/" + hash + ".jpg?s=40&d=404");
				//hash.equals Checks if empty string was hashed
				if(urlPic.openConnection().getContentType().contains("text") || hash.equals("d41d8cd98f00b204e9800998ecf8427e")){
					userPicture = ImageIO.read(getClass().getResource("/icons/default_user.png"));
				}else{
					userPicture = ImageIO.read(new URL("https://www.gravatar.com/avatar/" + hash + ".jpg?s=40&d=404"));
				}

			} catch (IOException e) {
				log.error("Could not pull Gravatar, " + e);
			}
		}
		return userPicture;
	}

	public String getPicturePath(){
		return ("https://www.gravatar.com/avatar/" + HashGen.hashMD5(email) + ".jpg");
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
