package Chatr.Converstation;

import Chatr.Helper.HashGen;

/**
 * Class to save and edit user data.
 * @author mk285
 *
 */
public class User {

	private String userName = "";
	private String userID = HashGen.getID(false);
	public String userPicture = "";

	public String getUserName(){
		return userName;
	}
	
	public void setProfileImg(String imgPath){
		userPicture = imgPath;
	}
	
	public String getProfileImg(){
		return userPicture;
	}
	
	public void setUserName(String newName){
		userName = newName;
	}
	
	public String getUserID(){
		return userID;
	}
}
