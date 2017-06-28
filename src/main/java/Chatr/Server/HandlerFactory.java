package Chatr.Server;

import Chatr.Helper.Enums.Request;
import Chatr.Model.Chat;
import Chatr.Model.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class HandlerFactory {
	public static Handler getInstance(Request requestType) {
		switch (requestType) {
			case MESSAGE: {
				return new MessageHandler();
			}
			case CONVERSATION: {
				return new ConversationHandler();
			}
			case USER: {
				return new UserHandler();
			}
			case USERS: {
				return new UserHandler();
			}
			case CONNECT: {
				return new ConnectHandler();
			}
			case LOGIN: {
				return new LoginHandler();
			}
			default: {
				return null;
			}
		}
	}

	private static class ConnectHandler extends Handler {
		@Override
		protected Collection<Transmission> process(Transmission request) {
			Set<Chat> chats = super.database.readAllChats(request.getLocalUserID());
			super.responses.add(request.setChats(chats));
			return super.responses;
		}
	}

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
									.filter(user -> !user.getUserID().equals(senderID))
									.collect(Collectors.toList())
					);
					break;
				}
				case DELETE: {
					super.database.deleteMessage(request.getChatID(), request.getMessage().getTime());
					Set<User> members = super.database.getChatMembers(request.getChatID());
					notify(request,
							members.stream()
									.filter(user -> !user.getUserID().equals(senderID))
									.collect(Collectors.toList())
					);
					break;
				}
			}

			return super.responses;

		}
	}

	private static class ConversationHandler extends Handler {
		@Override
		protected Collection<Transmission> process(Transmission request) {
			super.senderID = request.getLocalUserID();
			switch (request.getCRUD()) {
				case CREATE: {
					super.database.addChat(request.getChat());
					notify(request,
							members.stream()
									.filter(user -> !user.getUserID().equals(senderID))
									.collect(Collectors.toList())
					);
					for (String member : request.getUserIDs()) {
						if (!member.equals(super.senderID)) {
							try {
								Transmission response = (Transmission) request.clone();
								super.responses.add(response.setLocalUserID(member));
							} catch (CloneNotSupportedException e) {
							}
						}
					}
					break;
				}
				case UPDATE: {
					super.database.getChatMembers(request.getChatID(), request.getUserIDs());
					for (String member : request.getUserIDs()) {
						if (!member.equals(super.senderID)) {
							try {
								Transmission response = (Transmission) request.clone();
								super.responses.add(response.setLocalUserID(member));
							} catch (CloneNotSupportedException e) {
							}
						}
					}
					break;
				}
				case DELETE: {
					Set<User> members = super.database.getChatMembers(request.getChatID());
					super.database.deleteChat(request.getChatID());
					for (User member : members) {
						if (!member.equals(super.senderID)) {
							try {
								Transmission response = (Transmission) request.clone();
								super.responses.add(response.setLocalUserID(member.getUserID()));
							} catch (CloneNotSupportedException e) {
							}

						}
					}
					break;

				}

			}
			return super.responses;
		}
	}

	private static class UserHandler extends Handler {
		@Override
		protected Collection<Transmission> process(Transmission request) {
			super.senderID = request.getLocalUserID();
			switch (request.getRequestType()) {
				case USER: {
					switch (request.getCRUD()) {
						case CREATE:
							break;
						case READ: {
							User user = super.database.readUser(request.getUserID());
							super.responses.add(request.reset().setUser(user));
							break;
						}
						case UPDATE: {
							super.database.updateUser(request.getUser());
							for (User user : super.database.readUsers()) {
								if (!user.getUserID().equals(super.senderID)) {
									try {
										Transmission response = (Transmission) request.clone();
										super.responses.add(response.setLocalUserID(user.getUserID()));
									} catch (CloneNotSupportedException e) {
									}
								}
							}
						}
						case DELETE: {
							super.database.deleteUser(request.getUserID());
							super.responses.add(request);
						}
					}
				}

				case USERS: {
					Set<User> users = super.database.readUsers();
					super.responses.add(request.setUsers(users));
				}
			}
			return super.responses;
		}
	}

	public static class LoginHandler extends Handler {
		@Override
		protected Collection<Transmission> process(Transmission request) {
			return null;
		}


		protected Transmission processTransmission(Transmission request) {

			switch (request.getCRUD()) {
				case READ: {
					User user = super.database.readUser(request.getUserID());
					return request.setUser(user);
				}
				case CREATE: {
					boolean status = super.database.addUser(request.getUser());
					return request.setStatus(status);
				}
				default:
					return null;
			}

		}
	}

}


