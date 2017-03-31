package Chatr;

import Chatr.Client.Connection;
import Chatr.Converstation.User;

public class Login {
	
	public static User loginUser(String userID){
		User user = null;
		if( (user = Connection.readUser(userID) )!= null){
			return user;
		} else {
			user = new User(userID);
			return user;
		}
	}
}
