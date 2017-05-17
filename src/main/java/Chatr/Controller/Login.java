package Chatr.Controller;

import Chatr.Client.Connection;
import Chatr.Converstation.Exceptions.ClientSyntaxException;
import Chatr.Converstation.Exceptions.UserIdInUseException;
import Chatr.Converstation.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class to login into your user account or to create one.
 *
 * @author mk285
 */
public class Login {

	private static Logger log = LogManager.getLogger(Login.class);

	public static User loginUser(String userIdInput, String passwordInput){
		log.info(String.format("Methodcall loginUser"));
		try{
			userIdAvailableCheck(userIdInput);
		}catch (){

		}
		User user = null;
		return user;

	}
	/**
	 *
	 * @param userIdInput The user's wished UserID
	 * @param eMailInput    The user's email
	 * @param usernameInput The user's username
	 * @param passwordInput The user's password
	 * @return Returns a new object of user
	 */
	public static User registerUser(String userIdInput, String eMailInput, String usernameInput, String passwordInput) {

		log.info(String.format("Methodcall Regi"));
		String userIdConfirmed = "";
		try{
			userIdAvailableCheck(userIdInput);
			userIdConfirmed = syntaxUserId(userIdInput);
			syntaxEmail(eMailInput);
		}catch(ClientSyntaxException e){
			e.printStackTrace();
			log.info(e.toString());
		}catch(UserIdInUseException e){
			e.printStackTrace();
			log.info(e.toString());
		}

		User user = null;

		user = new User(userIdConfirmed);
		user.setUserName(usernameInput);
		user.setEmail(eMailInput);
		user.setPassword(Chatr.Helper.HashGen.hashMD5(passwordInput));

		Connection.createUser(userIdInput, user);
		log.info(String.format("created User %s|%s", user.getUserID(), user.getUserName()));
		return user;
	}

	/**
	 *
	 * @param userIdUserInput The UserID you want to check
	 * @return If UserID is permitted, the method returns the ID with an '@' in front of it.
	 * @throws ClientSyntaxException  Exception is thrown if UserID isn't permitted.
	 */
	public static String syntaxUserId (String userIdUserInput)throws ClientSyntaxException{

		String errorMessage = "Invalid User-ID input.";
		String userIdWithAt = "@" + firstCharToLowerCase(userIdUserInput);
		if (userIdWithAt.matches("^@[a-z][a-zA-Z0-9]{4,28}$")) {
			log.info(String.format("UserId %s is permitted.", userIdWithAt));
			return userIdWithAt;
		} else {
			log.info(String.format("UserID %s is not permitted.", userIdWithAt));
			throw new ClientSyntaxException(errorMessage);
		}
	}

	//TODO implement Email regex
	/**
	 *
	 * @param email Email you want to check
	 * @throws ClientSyntaxException    Exception is thrown if Email is not permitted.
	 */
	public static void syntaxEmail(String email) throws ClientSyntaxException {
		if(email.contains(".") && email.contains("@")){
			log.info(String.format("Email %s is permitted.", email));
		}else{
			log.info(String.format("Email %s is not permitted.", email));
			throw new ClientSyntaxException("Your email isn't legal.");
		}
	}

	/**
	 *
	 * @param userIdUserInput   UserID you want to check
	 * @throws UserIdInUseException Exception is thrown if the UserID is already in Use.
	 */
	public static void userIdAvailableCheck(String userIdUserInput) throws UserIdInUseException{
		User user = null;
		if ((user = Connection.readUser(userIdUserInput)) != null) {
			log.info(String.format("read User %s|%s from server", user.getUserID(), user.getUserName()));
			log.info(String.format("UserId %s is already in use.", userIdUserInput));
			throw new UserIdInUseException("This User-ID is already in use.");
		}
		else{
			log.info(String.format("UserId %s is available.", userIdUserInput));
		}
	}

	/**
	 *
	 * @param input String in which you want to change the first char to lower case
	 * @return  String with first char to lower case
	 */
	public static String firstCharToLowerCase(String input) {
		if (input == null || input.length() == 0) {
			return input;
		}
		char c[] = input.toCharArray();
		c[0] = Character.toLowerCase(c[0]);
		return new String(c);
	}
}




