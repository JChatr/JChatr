package Chatr.Controller;

import Chatr.Client.Connection;
import Chatr.Helper.HashGen;
import Chatr.Model.Exceptions.*;
import Chatr.Model.User;
import Chatr.Model.ErrorMessagesValidation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to login into your user account or to create one.
 *
 * @author mk285, mf140
 */
public class Login{


	private static Logger log = LogManager.getLogger(Login.class);

	/**
	 * @param userID   the users UserID
	 * @param password the users password
	 * @return a users object
	 */
	public static User loginUser(String userID, String password) {
		validateUser(userID, password);
		User user = Connection.readUserLogin(userID);
		if (user == null) {
			String errorMessage = "UserID or password invalid";
			log.error(errorMessage);
			throw new UserIDException(errorMessage);
		}
		if(!HashGen.checkPW(password, user.getPassword())){
			String errorMessage = "UserID or password invalid";
			log.error(errorMessage);
			throw new UserIDException(errorMessage);
		}
		log.info("logged in user", userID);
		return user;
	}

	/**
	 * @param userID   desired UserID
	 * @param eMail    The user's email
	 * @param userName The user's username
	 * @param password The user's password
	 * @return Returns a new object of user
	 */
	public static User registerUser(String userID, String eMail, String userName, String password) {
		User user = new User(userID);
		user.setUserName(userName);
		user.setEmail(eMail);
		user.setPassword(HashGen.hashPW(password));
		Connection.createUserLogin(userID, user);
		log.info(String.format("registered User %s|%s", user.getUserID(), user.getUserName()));
		return user;
	}

	/**
	 * @param userID   The UserID you wanna validate
	 * @param eMail    the email you wanna validate
	 * @param password the password you wanna validate
	 * @param username the username you wanna validate
	 * @return ErrormessagesValidation Object; contains error Messages for the GUI
	 */
	public static ErrorMessagesValidation validateUser(String userID, String eMail, String password, String username) {
		ErrorMessagesValidation errorMessagesValidation = new ErrorMessagesValidation();
		boolean errorExisting = false;
		try {
			validateUserID(userID);
		} catch (UserIDException e) {
			errorMessagesValidation.setUserIdErrorMessage(e.getErrorMessage());
			errorExisting = true;
			log.error(e);
		}
		try {
			validateUniqueID(userID);
		} catch (UserIDException e) {
			errorMessagesValidation.setUserIdErrorMessage(e.getErrorMessage());
			errorExisting = true;
			log.error(e);
		}
		try {
			validateEmail(eMail);
		} catch (EmailException e) {
			errorMessagesValidation.setEmailErrorMessage(e.getErrorMessage());
			errorExisting = true;
			log.error(e);
		}
		try {
			validatePassword(password);
		} catch (PasswordException e) {
			errorMessagesValidation.setPasswordErrorMessage(e.getErrorMessage());
			errorExisting = true;
			log.error(e);
		}
		try {
			validateUserName(username);
		} catch (UserNameException e) {
			errorMessagesValidation.setUsernameErrorMessages(e.getErrorMessage());
			errorExisting = true;
			log.error(e);
		}
		errorMessagesValidation.setErrorexisting(errorExisting);
		return errorMessagesValidation;
	}

	/**
	 * @param userID   the users Userid
	 * @param password the users password
	 * @return true if the user is Valid
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
				errorMessage = "ID contains invalid characters.";
			else if (userID.length() < 4) {
				errorMessage = "ID is too short.";
			} else {
				errorMessage = "ID is invalid.";
			}
			log.error(errorMessage, userID);
			throw new UserIDException(errorMessage);
		}
		return true;
	}

	/**
	 * @param userName
	 * @return
	 * @throws UserNameException
	 */
	private static boolean validateUserName(String userName) throws UserNameException {
		final String userNameRegex = "^(.){5,20}$";
		String errorMessage;
		if (userName.matches(userNameRegex)) {
			log.trace(String.format("Validated syntax of username %s.", userName));
		} else {
			if (userName.length() < 5) {
				errorMessage = "Username is too short.";
			} else {
				errorMessage = "Username is too long.";
			}
			log.error(errorMessage, userName);
			throw new UserNameException(errorMessage);
		}
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
		if ((user = Connection.readUserLogin(userID)) != null) {
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
			String errorMessage = "Password is too short.";
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
			String errorMessage = "Email is invalid.";
			log.error(errorMessage, email);
			throw new EmailException(errorMessage);
		}
	}

}




