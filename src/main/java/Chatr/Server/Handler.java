package Chatr.Server;

import Chatr.Model.User;
import Chatr.Server.Database.Database;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public abstract class Handler {
	Database database;
	List<Transmission> responses;
	String senderID;

	Handler() {
		this.database = Database.getInstance();
		this.responses = new LinkedList<>();
	}

	/**
	 * processes the incoming request and returns all responses to be sent out to the specified users
	 *
	 * @param request request to be processed
	 * @return transmissions to be sent to users
	 */
	protected abstract Collection<Transmission> process(Transmission request);

	/**
	 * sends the given transmission to the to Users
	 *
	 * @param transmission transmission to sendAsync
	 * @param userIDs        users to sendAsync to
	 */
	void notify(final Transmission transmission, final Collection<String> userIDs) {
		userIDs.forEach(user -> notify(transmission, user));
	}

	/**
	 * sends the transmission to the user
	 *
	 * @param transmission transmission to sendAsync
	 * @param userID         user to sendAsync to
	 */
	void notify(final Transmission transmission, final String userID) {
		responses.add(transmission.clone()
				.setLocalUserID(userID)
		);
	}
}

