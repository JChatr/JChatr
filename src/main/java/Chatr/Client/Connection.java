package Chatr.Client;

import Chatr.Converstation.Message;
import Chatr.Helper.CRUD;
import Chatr.Helper.TransmissionProtocol;

import java.util.List;

public class Connection {

	/**
	 * start connection then read new messages
	 *
	 * @param conversationID ID of the conversation to update
	 * @param newest         newest message in the local copy of the conversation
	 * @return new Messages from the server
	 */
	public static List<Message> updateConversation(String conversationID, Message newest) {
		List<String> received = new Client().get(TransmissionProtocol.build(CRUD.READ, conversationID, newest));
		return TransmissionProtocol.parse(CRUD.READ, received);
	}

	/**
	 * @param conversationID ID of the conversation to post the message to
	 * @param message        Message to post
	 */
	public static void postToConversation(String conversationID, Message message) {
		new Client().post(TransmissionProtocol.build(CRUD.CREATE, conversationID, message));
	}

	/**
	 * @param conversationID ID of the conversation to post the message to
	 * @param tmpData        TODO: replace this with the required server side data to store a conversation
	 */
	public static void createConversation(String conversationID, Message tmpData) {
		new Client().post(TransmissionProtocol.build(CRUD.UPDATE, conversationID, tmpData));
	}

	public static void deleteConversation(String conversationID) {
		new Client().post(TransmissionProtocol.build(CRUD.DELETE, conversationID, null));
	}
}