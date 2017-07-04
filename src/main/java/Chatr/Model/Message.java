package Chatr.Model;

import Chatr.Helper.Enums.MessageType;
import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class Message {
	private static Logger log = LogManager.getLogger(Message.class);
	private final Long timestamp = System.currentTimeMillis();
	private String sender;
	private String content;
	private boolean isEmpty = false;
	private MessageType messageType;
	private Image gifImg;
	private int width;
	private int height;

	public Message() {
		isEmpty = true;
	}

	public Message(String sender, String content, MessageType messageType) {
		this.sender = sender;
		this.content = content;
		this.messageType = messageType;
	}

	public Message(String sender, String content, MessageType messageType, int width, int height, Image gifImg) {
		log.trace("Gif message created with width,height:" + width + "," + height + "and url: " + content);
		this.sender = sender;
		this.content = content;
		this.messageType = messageType;
		this.width = width;
		this.height = height;
		if (gifImg != null) {
			this.gifImg = gifImg;
		}
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

	public Image getGifImg() {
		return this.gifImg;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public void setGifImg(Image gifImg) {
		this.gifImg = gifImg;
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
