package Chatr.Controller;

import Chatr.Client.Connection;
import Chatr.Helper.HashGen;
import Chatr.Model.Exceptions.*;
import Chatr.Model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to login into your user account or to create one.
 *
 * @author mk285, mf140
 */
public class Login {

	private static Logger log = LogManager.getLogger(Login.class);

	public static User loginUser(String userID, String password) {
		try {
			validateUser(userID, password);
			log.info("logged in user", userID);
		} catch (Exception e) {
			log.error("failed to Log in User" + userID, e);
		}
		return Connection.readUser(userID);
	}

	/**
	 * @param userID   desired UserID
	 * @param eMail    The user's email
	 * @param userName The user's username
	 * @param password The user's password
	 * @return Returns a new object of user
	 */
	public static User registerUser(String userID, String eMail, String userName, String password) {
		validateUser(userID, eMail, password);
		validateUniqueID(userID);

		User user = new User(userID);
		user.setUserName(userName);
		user.setEmail(eMail);
		user.setPassword(HashGen.hashPW(password));
		Connection.createUser(userID, user);
		log.info(String.format("registered User %s|%s", user.getUserID(), user.getUserName()));
		return user;
	}


	/**
	 * @param userID
	 * @param eMail
	 * @return
	 */
	private static boolean validateUser(String userID, String eMail, String password) {
		boolean valid = validateUserID(userID);
		valid &= validateEmail(eMail);
		valid &= validatePassword(password);
		return valid;
	}

	/**
	 * @param userID
	 * @return
	 */
	private static boolean validateUser(String userID, String password) {
		boolean valid = validateUserID(userID);
		valid &= validatePassword(password);
		return valid;
	}

	/**
	 * validates a userID
	 *
	 * @param userID The UserID you want to check
	 * @return If UserID is permitted, the method returns the ID with an '@' in front of it.
	 * @throws ValidationException thrown if the userID is invalid
	 */
	private static boolean validateUserID(String userID) throws UserIDException {
		final String userIDValidation = "^@[a-zA-Z0-9]{4,29}$";
		String errorMessage;
		if (userID.matches(userIDValidation)) {
			log.trace(String.format("Validated syntax of userID %s.", userID));
		} else {
			if (!userID.matches("[a-zA-Z0-9]+"))
				errorMessage = "The ID can only contain Letters & Numbers";
			else if (userID.length() < 4) {
				errorMessage = "The ID is too short";
			} else {
				errorMessage = "The ID is invalid";
			}
			log.error(errorMessage, userID);
			throw new UserIDException(errorMessage);
		}
		return true;
	}

	/**
	 *
	 * @param userName
	 * @return
	 * @throws UserNameException
	 */
	private static boolean validateUserName(String userName) throws UserNameException {
		// TODO write implementaion for this method
		return true;
	}
	/**
	 * checks if the userID exists on the server.
	 *
	 * @param userID ID to check for existance on the server
	 * @return if the userID is unique
	 * @throws UserIDException
	 */
	private static boolean validateUniqueID(String userID) throws UserIDException {
		User user;
		if ((user = Connection.readUser(userID)) != null) {
			log.trace(String.format("read User %s|%s from server", user.getUserID(), user.getUserName()));
			String errorMessage = "UserId is already in use.";
			log.error(errorMessage, userID);
			throw new UserIDException(errorMessage);
		}
		return true;
	}

	/**
	 * @param password
	 * @return
	 * @throws PasswordException
	 */
	private static boolean validatePassword(String password) throws PasswordException {
		if (password.length() < 5) {
			String errorMessage = "Password has to be 5 characters or longer";
			log.error(errorMessage);
			throw new PasswordException(errorMessage);
		}
		return true;
	}

	/**
	 * validates an email address according with 99 % correctness. Implements a simplified version of the RFC standard.
	 *
	 * @param email Email to validate
	 * @throws EmailException Exception is thrown if Email is not permitted.
	 */
	private static boolean validateEmail(String email) throws EmailException {
		Pattern emailRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = emailRegex.matcher(email);
		if (matcher.find()) {
			log.trace(String.format("Email %s is valid.", email));
			return true;
		} else {
			String errorMessage = "Email is invalid";
			log.error(errorMessage, email);
			throw new EmailException(errorMessage);
		}
	}
}




