package Chatr.Client;

import Chatr.Controller.Manager;
import Chatr.Helper.Enums.RequestType;
import Chatr.Model.Chat;
import Chatr.Model.User;
import Chatr.Server.Transmission;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.util.List;

class HandlerFactory {

	/**
	 *
	 * @param requestType Type you want to get
	 * @return Instanace of type you want to get
	 */
	static Handler getInstance(RequestType requestType) {
		switch (requestType) {
			case MESSAGE:
				return new MessageHandler(Manager.getUserChats());
			case CHAT:
				return new ConversationHandler(Manager.getUserChats());
			case USER:
				return new UserHandler(Manager.getUsers());
			default:
				return new DefaultHandler();
		}
	}


	/**
	 * Handler for all message related CRUD Requests
	 */
	private static class MessageHandler implements Handler {
		private ObservableList<Chat> chats;

		MessageHandler(ObservableList<Chat> userChats) {
			this.chats = userChats;
		}

		@Override
		public void process(Transmission transmission) {
			switch (transmission.getCRUD()) {
				case CREATE: {
					Platform.runLater(() -> chats.forEach(chat -> {
						if (chat.getID().equals(transmission.getChatID())) {
							chat.addMessage(transmission.getMessage());
						}
					}));
					break;
				}
				case READ:
				case UPDATE:
				case DELETE:
			}
		}
	}

	/**
	 * Handler for all chat related CRUD Requests
	 */
	private static class ConversationHandler implements Handler {
		private List<Chat> chats;

		ConversationHandler(List<Chat> userChats) {
			this.chats = userChats;
		}

		@Override
		public void process(Transmission transmission) {
			switch (transmission.getCRUD()) {
				case CREATE: {
					chats.add(transmission.getChat());
					break;
				}
				case READ:
				case UPDATE: {
					//No clear implementation of Update
					break;
				}
				case DELETE: {
					chats.removeIf(p -> p.equals(transmission.getChat()));
					break;
				}
			}
		}
	}

	/**
	 * Handler for all user related CRUD Requests
	 */
	private static class UserHandler implements Handler {
		private List<User> users;

		UserHandler(List<User> users) {
			this.users = users;
		}

		@Override
		public void process(Transmission transmission) {
			switch (transmission.getCRUD()) {
				case CREATE:
					//See LOGIN in Client
					break;
				case READ:
					//No clear implementation of READ
					break;
				case UPDATE: {
					for (int i = 0; i < users.size(); i++) {
						User user = users.get(i);
						if (user.equals(transmission.getUser()))
							users.set(i, transmission.getUser());
					}
				}
				case DELETE:
			}
		}
	}

	private static class DefaultHandler implements Handler {
		@Override
		public void process(Transmission transmission) {

		}
	}
}