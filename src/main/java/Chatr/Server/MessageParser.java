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
		if (!inCacheParsed.isEmpty()) {
			// need to get oldest & newest message to determinate the range of messages to send
			Message newest = inCacheParsed.get(0);
			for (Message m : inCacheParsed) {
				newest = (m.getTime() > newest.getTime()) ? m : newest;
			}
			List<Message> newer = dbCache.getNewer(newest.getTime());
			// print the messages that will be sent to the client
			if (!newer.isEmpty()) {
				dbCache.print();
				System.out.println("newer = " + newer);
			}
			return JSONConverter.toJSON(newer);
		} else {
			List<Message> newer = dbCache.getNewer(0L);
			if (!newer.isEmpty()) {
				dbCache.print();
				System.out.println("newer = " + newer);
			}
			return JSONConverter.toJSON(newer);
		}
	}
}
