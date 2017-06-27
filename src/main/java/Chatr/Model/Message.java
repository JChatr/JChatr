package Chatr.Model;

import Chatr.Helper.Enums.ContentType;
import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class Message {
    private String sender;
    private String content;
    private boolean isEmpty = false;
    private ContentType contentType;
    // timestamp is always guaranteed to be set
    private final Long timestamp = System.currentTimeMillis();
    private Image gifImg;
    private int width;
    private int height;
    private static Logger log = LogManager.getLogger(Message.class);


    public Message() {
        isEmpty = true;
    }

    public Message(String sender, String content, ContentType contentType) {
        this.sender = sender;
        this.content = content;
        this.contentType = contentType;
    }

    public Message(String sender, String content, ContentType contentType, int width, int height, Image gifObj) {
        log.trace("Gif message created with width,height:" + width + "," + height + "and url: " + content);
        this.sender = sender;
        this.content = content;
        this.contentType = contentType;
        this.width = width;
        this.height = height;
        if (gifObj != null) {
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

    public ContentType getContentType() {
        return this.contentType;
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
