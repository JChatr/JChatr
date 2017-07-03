package Chatr.View.ChatList;

import Chatr.Model.Chat;
import Chatr.Model.User;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.List;

class SearchMediator {
	private ObservableList<Chat> renderChats;
	private ObservableList<Chat> linkChats;
	private StringProperty userInput;

	/**
	 *
	 * @param chats
	 * @param userInput
	 */
	SearchMediator(ObservableList<Chat> chats, StringProperty userInput) {
		this.renderChats = chats;
		this.linkChats = FXCollections.observableArrayList();
		this.userInput = userInput;
		linkChats.addListener((ListChangeListener<? super Chat>) c -> {
			c.next();
			Platform.runLater(() -> {
				renderChats.addAll(c.getAddedSubList());
				renderChats.removeAll(c.getRemoved());
			});
		});
		userInput.addListener((observable, oldValue, newValue) -> {
			search(newValue, renderChats);
		});
	}

	/**
	 *
	 * @return
	 */
	public ObservableList<Chat> getLinks() {
		return linkChats;
	}

	/**
	 *
	 * @param search
	 * @param newChats
	 * @return
	 */
	private ObservableList<Chat> search(String search, ObservableList<Chat> newChats) {
		newChats.clear();
		if (search.trim().isEmpty()) {
			newChats.addAll(linkChats);
			return newChats;
		}
		linkChats.forEach(chat -> {
			String query = search.toLowerCase();
			if (chat.getName().get().toLowerCase().contains(query)
					|| chat.getID().toLowerCase().contains(query)
					|| containedInUsers(query, chat.getMembers())) {
				newChats.add(chat);
			}
		});
		System.out.println("linkChats = " + newChats);
		return newChats;
	}

	/**
	 * checks if the search String is contained in the given List of Users
	 * @param search search query
	 * @param users users to search through
	 * @return if the String was contained within
	 */
	private boolean containedInUsers(String search, List<User> users) {
		for (User user : users) {
			if (user.getID().contains(search)
					|| user.getUserName().contains(search)
					|| user.getEmail().contains(search)) {
				return true;
			}
		}
		return false;
	}
}
