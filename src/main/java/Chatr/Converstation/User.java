package Chatr.Converstation;

import Chatr.Helper.HashGen;

/**
 * Class to save and edit user data.
 * @author mk285
 *
 */
public class User {

	private String userName = "";
	private String userID = "";
	//private String userID = HashGen.getID(false);
	public String userPicture = "";
		
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
	 * This method is used to set the path of the profile path.
	 */
	public void setProfileImg(String imgPath){
		userPicture = imgPath;
	}
	
	/**
	 * This method is used to get the path of the profile picture.
	 * @return Path of profile picture.
	 */
	public String getProfileImg(){
		return userPicture;
	}
	
	/**
	 * This method is used to set the user name.
	 * @param newName The new user name.
	 */
	public void setUserName(String newName){
		userName = newName;
	}
	
	/**
	 * This method is used to get the user ID.
	 * @return Returns the user ID.
	 */
	public String getUserID(){
		return userID;
	}
		
}
