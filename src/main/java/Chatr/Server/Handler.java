package Chatr.Server;

import Chatr.Model.User;
import Chatr.Server.Database.Database;

import java.util.*;

public abstract class Handler {
	protected Database database;
	protected List<Transmission> responses = new LinkedList<>();
	protected String senderID;

	Handler() {
		database = Database.getCachedDatabase();
	}

	protected abstract Collection<Transmission> process(Transmission request);

	protected void notify(final Transmission transmission, final Collection<User> users) {
		users.forEach(user -> responses.add(
				transmission.clone().setLocalUserID(user.getUserID()))
		);


		List<String> names = Arrays.asList("1a", "2b", "3c", "4d", "5e");
		names.stream()
				.map(x -> x.toUpperCase())
				.mapToInt(x -> x.pos(1))
				.filter(x -> x < 5);
	}
}


