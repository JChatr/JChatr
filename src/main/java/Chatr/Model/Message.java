package Chatr.Model;

import Chatr.Helper.Enums.ContentType;

import java.util.Objects;

public class Message {
	private String sender;
	private String content;
	private boolean isEmpty = false;
	private ContentType contentType;
	private double width;
	private double higth;
	// timestamp is always guaranteed to be set
	private final Long timestamp = System.currentTimeMillis();

	public Message() {
		isEmpty = true;
	}

	public Message(String sender, String content, ContentType contentType) {
		this.sender = sender;
		this.content = content;
		this.contentType = contentType;
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

	public ContentType getContentType(){
		return this.contentType;
	}

	public double getWidth(){return width;}

	public double getHigth(){return higth;}

	public void setWidth(double width){this.width = width;}

	public void setHigth(double higth){this.higth = higth;}

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
