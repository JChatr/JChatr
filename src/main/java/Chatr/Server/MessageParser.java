package Chatr.Server;

import Chatr.Converstation.Message;
import Chatr.Helper.JSONTransformer;

import java.util.ArrayList;
import java.util.List;

public class MessageParser {
	private L1Cache l1Cache;
	private List<String> inCache;
	private List<Message> inCacheParsed;

	// needs connection to the Database as well as the all the sent messages
	// from the client
	protected MessageParser(L1Cache l1Cache, List<String> inCache) {
		this.l1Cache = l1Cache;
		this.inCache = inCache;
		inCacheParsed = new ArrayList<>();
		storeMessages();
	}

	// parse the messages from JSON to Objects
	// store in the Database
	private void storeMessages() {
		for (String json : inCache) {
			Message m = JSONTransformer.fromJSON(json, Message.class);
			inCacheParsed.add(m);
			l1Cache.put(m.getTime(), m);
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
			List<Message> newer = l1Cache.getNewer(newest.getTime());
			// print the messages that will be sent to the client
			if (!newer.isEmpty()) {
				l1Cache.print();
				System.out.println("newer = " + newer);
			}
			return JSONTransformer.toJSON(newer);
		} else {
			List<Message> newer = l1Cache.getNewer(0L);
			if (!newer.isEmpty()) {
				l1Cache.print();
				System.out.println("newer = " + newer);
			}
			return JSONTransformer.toJSON(newer);
		}
	}
}
