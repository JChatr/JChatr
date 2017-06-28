package Chatr.Controller;

import Chatr.Model.Chat;
import Chatr.Model.User;
import Chatr.Server.Transmission;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;

public abstract class HandlerClient {

	protected ObjectProperty<User> localUser;
	protected ListProperty<Chat> userChats;
	protected ListProperty<User> users;

	public abstract void processClient(Transmission t);
}
