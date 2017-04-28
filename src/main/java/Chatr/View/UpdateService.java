package Chatr.View;

import Chatr.Controller.Manager;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

public class UpdateService extends ScheduledService<Void> {
	private ListProperty<String> property;
	private StringProperty chat = new SimpleStringProperty();
	private StringProperty users = new SimpleStringProperty();


	public UpdateService() {
		property = new SimpleListProperty<>();
		property.setValue(FXCollections.observableArrayList());
	}

	public ListProperty<String> getMessageProperty() {
		return property;
	}

	public StringProperty getChatNameProperty() {
		return chat;
	}

	public StringProperty getUsersProperty() {
		return users;
	}

	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				chat.setValue(Manager.getChatName());

				StringBuilder sb = new StringBuilder();
				Manager.getChatMembers().forEach(member -> {
					if (sb.length() < 50) {
						sb.append(member);
						sb.append(", ");
					}
				});
				users.setValue(sb.toString());

				Manager.getChatUpdates().forEach(message ->
						property.add(message.toString()));
				return null;
			}
		};
	}
}
