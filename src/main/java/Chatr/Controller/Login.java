package Chatr.Controller;

import Chatr.Client.Connection;
import Chatr.Helper.HashGen;
import Chatr.Model.User;
import Chatr.Converstation.Exceptions.ClientSyntaxException;
import Chatr.Converstation.Exceptions.UserIdInUseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		}catch (Exception e){
			e.printStackTrace();
			log.info(e.toString());
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
		User user;
		log.info(String.format("Methodcall registerUser"));
		String userIdConfirmed = "";
		try{
			user = userIdAvailableCheck(userIdInput);
			userIdConfirmed = syntaxUserId(userIdInput);
			syntaxEmail(eMailInput);
		}catch(ClientSyntaxException | UserIdInUseException e){
			e.printStackTrace();
			log.info(e.toString());
		}

		user = new User(userIdConfirmed);
		user.setUserName(usernameInput);
		user.setEmail(eMailInput);
		user.setPassword(HashGen.hashMD5(passwordInput));

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
		userIdUserInput.toLowerCase();
		if(!userIdUserInput.startsWith("@")){
			userIdUserInput = "@" + userIdUserInput;
		}
		if (userIdUserInput.matches("^@[a-z][a-zA-Z0-9]{4,28}$")) {
			log.info(String.format("UserId %s is permitted.", userIdUserInput));
			return userIdUserInput;
		} else {
			log.info(String.format("UserID %s is not permitted.", userIdUserInput));
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
		if(emailRegex(email)){
			log.info(String.format("Email %s is permitted.", email));
		}else{
			log.info(String.format("Email %s is not permitted.", email));
			throw new ClientSyntaxException("Your email isn't legal.");
		}
	}

	private static boolean emailRegex(String email){
		Pattern emailRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = emailRegex.matcher(email);
		return matcher.find();
	}

	/**
	 *
	 * @param userIdUserInput   UserID you want to check
	 * @throws UserIdInUseException Exception is thrown if the UserID is already in Use.
	 */
	public static User userIdAvailableCheck(String userIdUserInput){
		User user;
		if ((user = Connection.readUser(userIdUserInput)) != null) {
			log.info(String.format("read User %s|%s from server", user.getUserID(), user.getUserName()));
			log.info(String.format("UserId %s is already in use.", userIdUserInput));
		}
		else{
			log.info(String.format("UserId %s is available.", userIdUserInput));
		}
		return user;
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




