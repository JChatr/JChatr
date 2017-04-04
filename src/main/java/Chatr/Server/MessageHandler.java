package Chatr.Server;

import Chatr.Converstation.Conversation;
import Chatr.Converstation.Message;
import Chatr.Converstation.User;
import Chatr.Database.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * server side message logic
 */
public class MessageHandler {
	private Database database;
	private List<Transmission> requests;
	private List<Transmission> responses = new ArrayList<>();
	private Notifier notifier;

	/**
	 * Instantiates the MessageHandler
	 *
	 * @param requests request from the client
	 */
	protected MessageHandler(List<Transmission> requests) {
		this.database = Database.getCachedDatabase();
		this.requests = requests;
		this.notifier = Notifier.getNotifier();
	}

	protected List<Transmission> process() {
		routeRequests();
//		notifier.notify();
//		responses.add(notifier.checkNotifications());
		return getResponses();
	}

	/**
	 * Directs requests to the corresponding methods
	 */
	private void routeRequests() {
		for (Transmission request : requests) {
			switch (request.getRequestType()) {
				case MESSAGE:
					switch (request.getCRUD()) {
						case CREATE: {
							boolean status = database.addMessage(request.getConversationID(), request.getMessage());
							responses.add(request.reset().setStatus(status));
							break;}
						case READ: {
							List<Message> m = database.readNewerMessages(request.getConversationID(),
									request.getMessage().getTime());
							responses.add(request.reset().setMessages(m));
							break; }
						case UPDATE: {
							boolean status = database.updateMessage(request.getConversationID(), request.getMessage());
							responses.add(request.reset().setStatus(status));
							break; }
						case DELETE:{
							boolean status = database.deleteMessage(request.getConversationID(),
									request.getMessage().getTime());
							responses.add(request.reset().setStatus(status));
							break; }
					}
					break;
				case CONVERSATION:
					switch (request.getCRUD()) {
						case CREATE: {
							boolean status = database.addConversation(request.getConversationID(),
									request.getUserIDs());
							responses.add(request.reset().setStatus(status));
							break; }
						case READ: {
							Set<Conversation> c = database.readUserConversations(request.getUserID());
							responses.add(request.reset().setConversations(c));
							break; }
						case UPDATE: {
							boolean status = database.updateConversationUsers(request.getConversationID(), request.getUserIDs());
							responses.add(request.reset().setStatus(status));
							break; }
						case DELETE: {
							boolean status = database.deleteConversation(request.getConversationID());
							responses.add(request.reset().setStatus(status));
							break; }
					}
					break;
				case USER:
					switch (request.getCRUD()) {
						case CREATE:{
							boolean status = database.addUser(request.getUser());
							responses.add(request.reset().setStatus(status));
							break; }
						case READ: {
							Set<User> users = database.readUsers();
							responses.add(request.reset().setUsers(users));
							break; }
						case UPDATE: {
							boolean status = database.updateUser(request.getUser());
							responses.add(request.reset().setStatus(status));
							break; }
						case DELETE: {
							boolean status = database.deleteUser(request.getUser().getUserID());
							responses.add(request.reset().setStatus(status));
							break; }
					}
					break;
				case STATUS:
					break;
			}
		}
	}

	/**
	 * Returns a list of all responses generated by the input requests
	 *
	 * @return list of responses generated by the requests
	 */
	private List<Transmission> getResponses() {
//		database.print();
		return this.responses;
	}
}