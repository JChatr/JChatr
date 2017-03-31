package Chatr;

import Chatr.Client.Connection;
import Chatr.Converstation.User;

public class Login {
	
	public static User loginUser(String userID){
		if(Connection.readUser(userID) != null){
			return Connection.readUser(userID);
		} else {
			User u = new User("userName");
		}
		
		return new User("");
	}
}
