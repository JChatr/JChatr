package Chatr.Server;

import Chatr.Client.JSONConverter;
import Chatr.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by data on 18/03/17.
 */
public class MessageParser {
	private DBCache dbCache;
	private List<String> inCache;
	private List<Message> inCacheParsed;

	protected MessageParser(DBCache dbCache, List<String> inCache) {
		this.dbCache = dbCache;
		this.inCache = inCache;
		inCacheParsed = new ArrayList<>();
		storeMessages();
	}

	private void storeMessages() {
		for (String json : inCache) {
			Message m = JSONConverter.fromJSON(json, Message.class);
			inCacheParsed.add(m);
			dbCache.put(m.getTime(), m);
		}
	}

	@SuppressWarnings("Unchecked")
	protected List<String> getNewerMessages() {
			dbCache.print();
		if (!inCacheParsed.isEmpty()) {
			Message oldest = inCacheParsed.get(0);
			Message newest = oldest;
			for (Message m : inCacheParsed) {
				oldest = (m.getTime() < oldest.getTime()) ? m : oldest;
				newest = (m.getTime() > oldest.getTime()) ? m : oldest;
			}

			System.out.println("oldest = " + oldest);

			List<Message> newer = dbCache.getRange(oldest.getTime(), newest.getTime());
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
