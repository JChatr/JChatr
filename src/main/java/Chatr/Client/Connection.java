package Chatr.Client;

import Chatr.Converstation.Message;
import Chatr.Helper.CONFIG;
import Chatr.Helper.CRUD;
import Chatr.Helper.JSONTransformer;

import java.util.ArrayList;
import java.util.List;

public class Connection {

	/**
	 * start connection then read new messages
	 *
	 * @param conversationID ID of the conversation to update
	 * @param newest         newest message in the local copy of the conversation
	 * @return new Messages from the server
	 */
	public static List<Message> getNewMessages(String conversationID, Message newest) {
		List<String> received = new Client().get(buildRequest(CRUD.READ, conversationID, newest));
		return parseResponse(CRUD.READ, received);
	}

	/**
	 * @param conversationID ID of the conversation to post the message to
	 * @param message        Message to post
	 */
	public static void postMessage(String conversationID, Message message) {
		new Client().post(buildRequest(CRUD.CREATE, conversationID, message));
	}

	/**
	 * @param conversationID ID of the conversation to post the message to
	 * @param tmpData        TODO: replace this with the required server side data to store a conversation
	 */
	public static void createNewConversation(String conversationID, Message tmpData) {
		new Client().post(buildRequest(CRUD.UPDATE, conversationID, tmpData));
	}

	public static void deleteConversation(String conversationID) {
		new Client().post(buildRequest(CRUD.DELETE, conversationID, null));
	}

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
	private static String buildRequest(CRUD operation, String conID, Message message) {
		StringBuilder output = new StringBuilder();
		output.append(operation.name());
		output.append(CONFIG.separator);
		output.append(conID);
		output.append(CONFIG.separator);
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
	private static <T> List<T> parseResponse(CRUD operation, List<String> response) {
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

	}


//	@Override
//	public void run() {
//		while (true) {
//			List<Message> messages = getNewMessages();
//			if (!messages.isEmpty()) {
//				Terminal.display(messages);
//			}
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
}