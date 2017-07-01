package Chatr.Model;

import Chatr.Helper.Enums.MessageType;

import java.util.Objects;

public class Message {
	// timestamp is always guaranteed to be set
	private final Long timestamp = System.currentTimeMillis();
	private String sender;
	private String content;
	private boolean isEmpty = false;
	private MessageType messageType;

	public Message() {
		isEmpty = true;
	}

	public Message(String sender, String content, MessageType messageType) {
		this.sender = sender;
		this.content = content;
		this.messageType = messageType;
	}

	public String getContent() {
		return this.content;
	}

	public String getSender() {
		return this.sender;
	}

	public long getTime() {
		return this.timestamp;
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	public MessageType getMessageType() {
		return this.messageType;
	}

	// make debug printing easier
	@Override
	public String toString() {
		return String.format("%s | %5s | %s", timestamp, sender, content);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		return this.hashCode() == obj.hashCode();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(timestamp);
	}
}
