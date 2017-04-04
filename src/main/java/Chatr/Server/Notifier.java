package Chatr.Server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Notifier {
	private static Notifier instance;
	// | USER_ID | NOTIFICATIONS... |
	private Map<String, List<Transmission>>	notifications;

	private Notifier() {
		this.notifications = new HashMap<>();
	}

	protected static Notifier getNotifier() {
		return (instance == null) ? instance = new Notifier() : instance;
	}

	protected synchronized void notify(final List<String> userIDs, final Transmission notification) {
		userIDs.forEach(user -> {
			notifications.putIfAbsent(user, new LinkedList<>());
			notifications.get(user).add(notification);
		});
	}

	protected synchronized List<Transmission> checkNotifications(String userID) {
		List<Transmission> out = new LinkedList<>();
		for(Map.Entry<String, List<Transmission>> list : notifications.entrySet()) {
			if (userID.equals(list.getKey())){
				out = list.getValue();
				break;
			}
		}
		return out;
	}
}
