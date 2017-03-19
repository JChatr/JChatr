package Chatr.Server;

import Chatr.Client.JSONConverter;
import Chatr.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageParser {
	private DBCache dbCache;
	private List<String> inCache;
	private List<Message> inCacheParsed;

	// needs connection to the Database as well as the all the sent messages
	// from the client
	protected MessageParser(DBCache dbCache, List<String> inCache) {
		this.dbCache = dbCache;
		this.inCache = inCache;
		inCacheParsed = new ArrayList<>();
		storeMessages();
	}

	// parse the messages from JSON to Objects
	// store in the Database
	private void storeMessages() {
		for (String json : inCache) {
			Message m = JSONConverter.fromJSON(json, Message.class);
			inCacheParsed.add(m);
			dbCache.put(m.getTime(), m);
		}
	}

	// find the newer messages and parse them to JSON
	@SuppressWarnings("Unchecked")
	protected List<String> getNewerMessages() {
		// print the entire content of the Database to the console
		dbCache.print();
		if (!inCacheParsed.isEmpty()) {
			// need to get oldest & newest message to determinate the range of messages to send
			Message oldest = inCacheParsed.get(0);
			Message newest = oldest;
			for (Message m : inCacheParsed) {
				oldest = (m.getTime() < oldest.getTime()) ? m : oldest;
				newest = (m.getTime() > oldest.getTime()) ? m : oldest;
			}
			System.out.println("oldest = " + oldest);

			List<Message> newer = dbCache.getRange(oldest.getTime(), newest.getTime());
			// print the messages that will be sent to the client
			System.out.println("newer = " + newer);
			return JSONConverter.toJSON(newer);
		} else {
			List<Message> newer = dbCache.getRange(0L, System.currentTimeMillis());
			System.out.println("oldest = " + null);
			System.out.println("newer = " + newer);
			return JSONConverter.toJSON(newer);
		}
	}
}
