package Chatr;

import Chatr.Client.Connection;
import Chatr.Converstation.User;

/**
 * Class to login into your user account or to create one.
 * @author mk285
 *
 */
public class Login {
	
	/**
	 * Checks if user userID exists. Else creates user userID.
	 * @param userID The user to be checked or created.
	 * @return The user object.
	 */
	public static User loginUser(String userID){
		User user = null;
		if( (user = Connection.readUser(userID) )!= null){
			return user;
		} else {
			user = new User(userID);
			Connection.createUser(userID, user);
			return user;
		}
	}
}
