package Chatr.Controller;

import Chatr.Client.Connection;
import Chatr.Converstation.User;
import Chatr.Helper.Terminal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class to login into your user account or to create one.
 *
 * @author mk285
 */
public class Login {

	public static boolean successful = false;
	private static String email;
	private static Logger log = LogManager.getLogger(Login.class);
	static int nickcounter = 0;
	/**
	 * Checks if user userID exists. Else creates user userID.
	 *
	 * @param userID The user to be checked or created.
	 * @return The user object.
	 */

	//TODO implement password support

	public static User registerUser(String userID, String eMailInput, String usernameInput, String passwordInput) {



//		while (!userID.startsWith("@") || userID.length() < 4) {
//			nickcounter++;
//			if (nickcounter == 5) {
//				log.error(String.format("Nickname retry limit of %d reached", 5));
//				throw new IllegalStateException();
//			}
//			Terminal.display("Nicknames must start with a '@' and have at least three chars! Try again!");
//			userID = Terminal.getUserInput();
//		}
		// TODO implement User.syntaxUserId


		User user = null;
		if ((user = Connection.readUser(userID)) != null) {
			log.info(String.format("read User %s|%s from server", user.getUserID(), user.getUserName()));
			return user;

			// TODO return user already available
		} else {
			user = new User(userID);
			//Terminal.display("Enter your display name: ");
			user.setUserName(usernameInput);

			//Terminal.display("Enter your email: ");
			//nemail = Terminal.getUserInput();
			while (!user.syntaxEmail(eMailInput)) {

				//TODO Error emailsyntax


				//System.out.println("Invalid input, please try again: ");
				//email = Terminal.getUserInput();
			}
			user.setEmail(eMailInput);
			Connection.createUser(userID, user);
			log.info(String.format("created User %s|%s", user.getUserID(), user.getUserName()));

			successful = true;
			return user;
		}
	}
}
