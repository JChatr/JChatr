package Chatr.Helper;

import Chatr.Converstation.Message;

import java.util.ArrayList;
import java.util.List;

public class TransmissionProtocol {
	/**
	 * Request Format
	 * OPERATION : conversation ID : message JSON
	 * TODO: wrap message in Optional to avoid having to pass null
	 *
	 * @param operation operation to do on the Server
	 * @param conID     conversation ID
	 * @param message   message if CREATE or READ
	 * @return Request as a String
	 */
	public static String build(CRUD operation, String conID, Message message) {
		StringBuilder output = new StringBuilder();
		output.append(operation.name());
		output.append(CONFIG.SEPARATOR);
		output.append(conID);
		output.append(CONFIG.SEPARATOR);
		switch (operation) {
			case CREATE:
				output.append(JSONTransformer.toJSON(message));
				break;
			case READ:
				output.append(JSONTransformer.toJSON(message));
				break;
			case UPDATE:
				output.append(JSONTransformer.toJSON(message));
				break;
			case DELETE:
				break;
		}
		return output.toString();
	}

	/**
	 * parses the received JSONs to Message Objects
	 *
	 * @param operation Operation used when requesting the data form the server
	 * @param response  Response received from the server
	 * @param <T>       expected return Type (implicit ClassCast)
	 * @return Response parsed to the desired Object type
	 */
	public static <T> List<T> parse(CRUD operation, List<String> response) {
		switch (operation) {
			case READ:
				List<T> parse = new ArrayList<>();
				for (String line : response) {
					if (!line.trim().isEmpty()) {
						parse.add(JSONTransformer.fromJSON(line, Message.class));
					}
				}
				return parse;
			case CREATE:
				return new ArrayList<>();
			case UPDATE:
				return new ArrayList<>();
			case DELETE:
				return new ArrayList<>();
		}
		// this is the java c being overly concerned, doesn't recognize that this will never be reached
		return null;
	}
}
