package Chatr;

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

	private static String email;
	private static Logger log = LogManager.getLogger(Login.class);
	/**
	 * Checks if user userID exists. Else creates user userID.
	 *
	 * @param userID The user to be checked or created.
	 * @return The user object.
	 */
	public static User loginUser(String userID) {
		int nickcounter = 0;
		while (!userID.startsWith("@") || userID.length() < 4) {
			nickcounter++;
			if (nickcounter == 5) {
				log.error(String.format("Nickname retry limit of %d reached", 5));
				throw new IllegalStateException();
			}
			Terminal.display("Nicknames must start with a '@' and have at least three chars! Try again!");
			userID = Terminal.getUserInput();
		}

		User user = null;
		if ((user = Connection.readUser(userID)) != null) {
			log.info(String.format("read User %s|%s from server", user.getUserID(), user.getUserName()));
			return user;
		} else {
			user = new User(userID);
			Terminal.display("Enter your display name: ");
			user.setUserName(Terminal.getUserInput());

			Terminal.display("Enter your email: ");
			email = Terminal.getUserInput();
			while (!user.syntaxEmail(email)) {
				System.out.println("Invalid input, please try again: ");
				email = Terminal.getUserInput();
			}
			user.setEmail(email);
			Connection.createUser(userID, user);
			log.info(String.format("created User %s|%s", user.getUserID(), user.getUserName()));
			return user;
		}
	}
}
