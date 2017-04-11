package Chatr;

import Chatr.Client.Connection;
import Chatr.Converstation.User;
import Chatr.Helper.Terminal;
import sun.security.x509.X500Name;

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
		int nickcounter = 0;
		while(!userID.startsWith("@") || userID.length() < 4){
			nickcounter++;
			if(nickcounter == 5){
				throw new IllegalStateException();
			}
			System.out.println("Nicknames must start with a '@' and have at least three chars! Try again!");
			userID = Terminal.getUserInput();
		}

		User user = null;
		if( (user = Connection.readUser(userID) )!= null){
			return user;
		} else {
			user = new User(userID);
			System.out.print("Enter your display name: ");
			user.setUserName(Terminal.getUserInput());
			System.out.print("Enter your email: ");
			user.setEmail(Terminal.getUserInput());
			Connection.createUser(userID, user);
			return user;
		}
	}
}
