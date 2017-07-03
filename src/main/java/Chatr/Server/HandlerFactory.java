package Chatr.Server;

import Chatr.Helper.Enums.RequestType;
import Chatr.Model.Chat;
import Chatr.Model.Message;
import Chatr.Model.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class HandlerFactory {
	/**
	 * Returns the required Handler for the given requestType Type
	 *
	 * @param requestType the requestType to be processed
	 * @return a Handler for the given requestType
	 */
	static Handler getInstance(RequestType requestType) {
		switch (requestType) {
			case MESSAGE:
				return new MessageHandler();
			case CHAT:
				return new ChatHandler();
			case USER:
				return new UserHandler();
			case USERS:
				return new UsersHandler();
			case CONNECT:
				return new ConnectHandler();
			case LOGIN:
				return new LoginHandler();
			default:
				return null;

		}
	}

	/**
	 * Handler for all connection related CRUD Requests
	 */
	private static class ConnectHandler extends Handler {
		@Override
		protected Collection<Transmission> process(Transmission request) {
			super.senderID = request.getLocalUserID();
			Set<Chat> chats = super.database.readAllChats(request.getLocalUserID());
			notify(request.reset()
							.setChats(chats),
					senderID);
			return super.responses;
		}
	}


	/**
	 * Handler for all message related CRUD Requests
	 */
	private static class MessageHandler extends Handler {

		@Override
		protected Collection<Transmission> process(Transmission request) {
			super.senderID = request.getLocalUserID();
			switch (request.getCRUD()) {
				case CREATE: {
					super.database.addMessage(request.getChatID(), request.getMessage());
					Set<User> members = super.database.getChatMembers(request.getChatID());
					notify(request,
							members.stream()
									.filter(user -> !user.getID().equals(senderID))
									.map(User::getID)
									.collect(Collectors.toList())
					);
					break;
				}
				case READ: {
					List<Message> messages = super.database.readNewerMessages(request.getChatID(), request.getTimestamp());
					notify(request.reset().setMessages(messages),
							senderID);
					break;
				}
				case UPDATE: {
					boolean success = super.database.updateMessage(request.getChatID(), request.getMessage());
					notify(request.reset()
									.setStatus(success),
							senderID);
					break;
				}
				case DELETE: {
					super.database.deleteMessage(request.getChatID(), request.getMessage().getTime());
					Set<User> members = super.database.getChatMembers(request.getChatID());
					notify(request,
							members.stream()
									.filter(user -> !user.getID().equals(senderID))
									.map(User::getID)
									.collect(Collectors.toList())
					);
					break;
				}
			}
			return super.responses;
		}
	}


	/**
	 * Handler for all chat related CRUD Requests
	 */
	private static class ChatHandler extends Handler {
		@Override
		protected Collection<Transmission> process(Transmission request) {
			super.senderID = request.getLocalUserID();
			switch (request.getCRUD()) {
				case CREATE: {
					boolean success = super.database.addChat(request.getChat());
					Chat c= request.getChat();
					Set<User> members = super.database.getChatMembers(request.getChat().getID());
					for (User member : members){
						if(!member.getID().equals(senderID)){
							notify(request.reset().setStatus(success).setChat(
								super.database.readChat(c.getID(),member.getID()))
								,member.getID());
						}

					}
					/*notify(request.reset()
									.setStatus(success).setChat(c),
							members.stream()
									.filter(user -> !user.getID().equals(senderID))
									.map(User::getID)
									.collect(Collectors.toList())
					);*/
					break;
				}
				case READ: {
					Set<Chat> chats = super.database.readAllChats(request.getUserID());
					notify(request.reset()
									.setChats(chats),
							senderID);
					break;
				}
				case UPDATE: {
					boolean success = super.database.updateChat(request.getChat());
					Set<User> members = super.database.getChatMembers(request.getChat().getID());
					notify(request.reset()
									.setStatus(success),
							members.stream()
									.filter(user -> !user.getID().equals(senderID))
									.map(User::getID)
									.collect(Collectors.toList())
					);
					break;
				}
				case DELETE: {
					boolean success = super.database.deleteChat(request.getChatID());
					Set<User> members = super.database.getChatMembers(request.getChatID());
					notify(request.reset().setStatus(success),
							members.stream()
									.filter(user -> !user.getID().equals(senderID))
									.map(User::getID)
									.collect(Collectors.toList())
					);
					break;
				}
			}
			return super.responses;
		}
	}


	/**
	 * Handler for all user related CRUD Requests
	 */
	private static class UserHandler extends Handler {
		@Override
		protected Collection<Transmission> process(Transmission request) {
			super.senderID = request.getLocalUserID();
			switch (request.getCRUD()) {
				case CREATE:
					break;
				case READ: {
					User user = super.database.readUser(request.getUserID());
					notify(request.reset()
									.setUser(user),
							senderID);
					break;
				}
				case DELETE: {
					boolean success = super.database.deleteUser(request.getUserID());
					Set<User> users = database.readUsers();
					notify(request.reset()
									.setUserID(request.getUserID())
									.setStatus(success),
							users.stream()
									.filter(user -> !user.getID().equals(senderID))
									.map(User::getID)
									.collect(Collectors.toList()));
				}
			}
			return super.responses;
		}
	}


	/**
	 * Handler for all users related CRUD Requests
	 */
	private static class UsersHandler extends Handler {

		@Override
		protected Collection<Transmission> process(Transmission request) {
			super.senderID = request.getLocalUserID();
			switch (request.getCRUD()) {
				case READ: {
					notify(request.reset()
									.setUsers(database.readUsers()),
							senderID);
					break;
				}
			}
			return super.responses;
		}
	}


	/**
	 * Handler for all login related CRUD Requests
	 */
	private static class LoginHandler extends Handler {
		@Override
		protected Collection<Transmission> process(Transmission request) {
			super.senderID = request.getLocalUserID();
			switch (request.getCRUD()) {
				case READ: {
					User user = super.database.readUser(request.getUserID());
					notify(request.reset()
									.setUser(user),
							senderID);
					break;
				}
				case CREATE: {
					boolean status = super.database.addUser(request.getUser());
					notify(request.reset()
									.setStatus(status),
							senderID);
					break;
				}
				case UPDATE:
					break;
				case DELETE:
					break;
			}
			return super.responses;
		}
	}
}