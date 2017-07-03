package Chatr.View.ChatList;

import Chatr.Model.Chat;
import Chatr.Model.Message;
import Chatr.Model.User;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.util.List;

class SearchMediator {
	private ObservableList<Chat> displayChats;
	private ObservableList<Chat> linkChats;
	private StringProperty userInput;

	SearchMediator(ObservableList<Chat> chats, StringProperty userInput) {
		this.displayChats = chats;
		this.linkChats = FXCollections.observableArrayList();
		this.userInput = userInput;
		userInput.addListener((observable, oldValue, newValue) -> {
			System.out.println(newValue);

		});
	}

	public ObservableList<Chat> start() {
		linkChats.addListener((ListChangeListener<? super Chat>) c -> {
			c.next();
			displayChats.addAll(c.getAddedSubList());
			displayChats.removeAll(c.getRemoved());
			System.out.println("changed display");
		});
		userInput.addListener((observable, oldValue, newValue) -> {
			search(newValue, displayChats);
		});
		return linkChats;
	}

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
