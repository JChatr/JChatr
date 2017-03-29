package Chatr.Server;

import Chatr.Converstation.Message;
import Chatr.Helper.CRUD;
import Chatr.Helper.JSONTransformer;

/**
 * Request wrapper internal to the server
 */
public class Request {
	private CRUD type;
	private String conversationID;
	private Message message;

	/**
	 * create new request object
	 *
	 * @param crud           String representation of the request type (CREATE, READ, UPDATE, DELETE)
	 * @param conversationID ID of the conversation to be modified
	 * @param message        attached message as a JSON string
	 */
	protected Request(String crud, String conversationID, String message) {
		this.type = parseType(crud);
		this.conversationID = conversationID;
		this.message = parseMessage(message, type);
	}

	/**
	 * parses the CURD string back to the enum Type
	 * TODO: throw something more descriptive than a RuntimeException
	 *
	 * @param crudRAW raw string to parse
	 * @return enum type of the string
	 */
	protected CRUD parseType(String crudRAW) {
		switch (crudRAW) {
			case "CREATE":
				return CRUD.CREATE;
			case "UPDATE":
				return CRUD.UPDATE;
			case "READ":
				return CRUD.READ;
			case "DELETE":
				return CRUD.DELETE;
			default:
				throw new RuntimeException();
		}
	}

	/**
	 * parses the Message String back to a Message Object
	 *
	 * @param message JSON representation of the Message Object
	 * @param type    Type of request made to the server
	 * @return Object representation of the Message object
	 */
	private Message parseMessage(String message, CRUD type) {
		if (CRUD.DELETE.compareTo(type) != 0) {
			return JSONTransformer.fromJSON(message, Message.class);
		}
		return new Message();
	}

	protected Message getMessage() {
		return this.message;
	}

	protected String getConversationID() {
		return this.conversationID;
	}

	protected CRUD getRequestType() {
		return this.type;
	}
}
